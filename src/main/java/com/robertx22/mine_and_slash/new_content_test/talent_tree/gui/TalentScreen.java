package com.robertx22.mine_and_slash.new_content_test.talent_tree.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.robertx22.mine_and_slash.db_lists.registry.SlashRegistry;
import com.robertx22.mine_and_slash.mmorpg.MMORPG;
import com.robertx22.mine_and_slash.mmorpg.Ref;
import com.robertx22.mine_and_slash.network.sync_cap.CapTypes;
import com.robertx22.mine_and_slash.network.sync_cap.RequestSyncCapToClient;
import com.robertx22.mine_and_slash.new_content_test.talent_tree.TalentConnection;
import com.robertx22.mine_and_slash.new_content_test.talent_tree.TalentPoint;
import com.robertx22.mine_and_slash.saveclasses.gearitem.gear_bases.TooltipInfo;
import com.robertx22.mine_and_slash.uncommon.capability.EntityCap;
import com.robertx22.mine_and_slash.uncommon.capability.PlayerTalentsCap.IPlayerTalentsData;
import com.robertx22.mine_and_slash.uncommon.datasaving.Load;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.GuiUtils;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.GuiUtils.PointF;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.TooltipUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TalentScreen extends Screen {

    Minecraft mc;
    EntityCap.UnitData data;

    IPlayerTalentsData talents;

    public float scrollX = 0;
    public float scrollY = 0;

    private static final ResourceLocation TEXTURE = new ResourceLocation(Ref.MODID, "textures/gui/talents/talent_frame.png");
    private static final ResourceLocation SPACE = new ResourceLocation(Ref.MODID, "textures/gui/talents/space.png");
    private static final ResourceLocation LINES = new ResourceLocation(Ref.MODID, "textures/gui/talents/lines.png");

    public static int sizeX = 318;
    public static int sizeY = 233;

    public TalentScreen() {
        super(new StringTextComponent(""));
        this.mc = Minecraft.getInstance();
        this.data = Load.Unit(mc.player);

        MMORPG.sendToServer(new RequestSyncCapToClient(CapTypes.TALENTS));
        this.talents = Load.talents(mc.player);
    }

    @Override
    public void init(Minecraft mc, int x, int y) {
        super.init(mc, x, y);

        for (TalentPoint talent : SlashRegistry.Talents().getList()) {
            this.addButton(new TalentPointButton(talent, data));
        }

    }

    @Override
    public boolean mouseDragged(double x, double y, int ticks, double dragX,
                                double dragY) {
        this.scrollX += dragX;
        this.scrollY += dragY;
        return true;

    }

    List<TalentPointButton> getTalentButtons() {
        List<TalentPointButton> list = new ArrayList<>();
        for (Widget w : this.buttons) {
            if (w instanceof TalentPointButton) {
                list.add((TalentPointButton) w);
            }
        }
        return list;

    }

    @Override
    public void render(int x, int y, float ticks) {

        if (mc.player.ticksExisted % 50 == 0) {
            MMORPG.sendToServer(new RequestSyncCapToClient(CapTypes.TALENTS));
            this.talents = Load.talents(mc.player);
        }

        super.render(x, y, ticks);

        drawSpace();

        List<TalentPointButton> list = getTalentButtons();

        renderConnections(list);

        for (TalentPointButton but : list) {
            but.renderButton(x, y, ticks, (int) scrollX, (int) scrollY);
        }

        drawBorders();

        renderTooltips(list, x, y);
    }

    public void renderTooltips(List<TalentPointButton> list, int mouseX, int mouseY) {

        TooltipInfo info = new TooltipInfo();

        list.forEach(button -> {

            if (button.isInsideSlot(scrollX, scrollY, mouseX, mouseY)) {
                this.renderTooltip(TooltipUtils.compsToStrings(button.talentPoint.effect.GetTooltipString(info)), mouseX, mouseY, mc.fontRenderer);
            }
        });

    }

    @Override
    public boolean mouseClicked(double x, double y, int ticks) {
        getTalentButtons().forEach(t -> t.onClick(scrollX, scrollY, (int) x, (int) y));
        return super.mouseClicked(x, y, ticks);

    }

    private void renderConnections(List<TalentPointButton> list) {

        Set<TalentConnection> connections = this.talents.getConnections();

        for (TalentConnection connection : connections) {

            Optional<TalentPointButton> but1 = list.stream()
                    .filter(button -> button.talentPoint.GUID()
                            .equals(connection.one.GUID()))
                    .findAny();

            Optional<TalentPointButton> but2 = list.stream()
                    .filter(button -> button.talentPoint.GUID()
                            .equals(connection.two.GUID()))
                    .findAny();

            if (but1.isPresent() && but2.isPresent()) {
                renderConnection(but1.get(), but2.get(), connection);
            }
        }

    }

    private boolean shouldRender(int x, int y) {

        int offsetX = mc.mainWindow.getScaledWidth() / 2 - sizeX / 2;
        int offsetY = mc.mainWindow.getScaledHeight() / 2 - sizeY / 2;

        if (x >= offsetX && x < offsetX + sizeX - 6) {
            if (y >= offsetY && y < offsetY + sizeY - 6) {
                return true;
            }
        }

        return false;

    }

    private void renderConnection(TalentPointButton one, TalentPointButton two,
                                  TalentConnection connection) {

        int x1 = one.getMiddleX((int) scrollX);
        int y1 = one.getMiddleY((int) scrollY);

        int x2 = two.getMiddleX((int) scrollX);
        int y2 = two.getMiddleY((int) scrollY);

        int size = 6;

        float spacing = size + size / 2;

        List<PointF> points = GuiUtils.generateCurve(new PointF(x1, y1), new PointF(x2, y2), 100f, spacing, true);

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(LINES);

        for (PointF point : points) {

            int x = (int) (point.x - ((float) size / 2));
            int y = (int) (point.y - ((float) size / 2));

            if (shouldRender(x, y)) {
                blit(x, y, 0, connection.allocationStatus.spriteOffsetX, 0.0F, size, size, 256, 256);
            }
        }

    }

    protected void drawSpace() {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int offsetX = mc.mainWindow.getScaledWidth() / 2 - sizeX / 2;
        int offsetY = mc.mainWindow.getScaledHeight() / 2 - sizeY / 2;
        Minecraft.getInstance().getTextureManager().bindTexture(SPACE);
        blit(offsetX + 3, offsetY + 3, this.blitOffset, 0.0F, 0.0F, sizeX - 6, sizeY - 6, 2048, 2048);
    }

    protected void drawBorders() {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int offsetX = mc.mainWindow.getScaledWidth() / 2 - sizeX / 2;
        int offsetY = mc.mainWindow.getScaledHeight() / 2 - sizeY / 2;
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
        blit(offsetX, offsetY, this.blitOffset, 0.0F, 0.0F, sizeX, sizeY, 256, 512);

    }

}