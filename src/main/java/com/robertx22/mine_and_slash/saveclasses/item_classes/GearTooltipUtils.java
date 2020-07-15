package com.robertx22.mine_and_slash.saveclasses.item_classes;

import com.robertx22.mine_and_slash.config.forge.ClientContainer;
import com.robertx22.mine_and_slash.database.gearitemslots.bases.BaseGearType;
import com.robertx22.mine_and_slash.database.rarities.GearRarity;
import com.robertx22.mine_and_slash.database.unique_items.IUnique;
import com.robertx22.mine_and_slash.saveclasses.ExactStatData;
import com.robertx22.mine_and_slash.saveclasses.gearitem.gear_bases.FinalizedGearStatReq;
import com.robertx22.mine_and_slash.saveclasses.gearitem.gear_bases.IGearPartTooltip;
import com.robertx22.mine_and_slash.saveclasses.gearitem.gear_bases.TooltipInfo;
import com.robertx22.mine_and_slash.saveclasses.item_classes.tooltips.MergedStats;
import com.robertx22.mine_and_slash.uncommon.capability.entity.EntityCap.UnitData;
import com.robertx22.mine_and_slash.uncommon.localization.CLOC;
import com.robertx22.mine_and_slash.uncommon.localization.Styles;
import com.robertx22.mine_and_slash.uncommon.localization.Words;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.TooltipUtils;
import com.robertx22.mine_and_slash.uncommon.wrappers.SText;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class GearTooltipUtils {

    public static void BuildTooltip(GearItemData gear, ItemStack stack, List<ITextComponent> tooltip, UnitData data) {

        List<ITextComponent> tip = tooltip;

        TooltipInfo info = new TooltipInfo(data, gear.getRarity()
            .StatPercents());

        BaseGearType slot = gear.GetBaseGearType();

        tip.clear();

        tip.add(gear.GetDisplayName(stack));

        if (!gear.isIdentified()) {

            tip.add(new SText(""));

            tip.add(Styles.GRAYCOMP()
                .appendSibling(Words.ItemIsUnidentified.locName()));
            tip.add(Styles.GRAYCOMP()
                .appendSibling(Words.UseAnIdentifyScroll.locName()));

            tip.add(new SText(""));

            return;
        }

        if (gear.baseStats != null) {
            tip.addAll(gear.baseStats.GetTooltipString(info, gear));
        }

        TextFormatting reqColor = TextFormatting.GRAY;
        TextFormatting reqNumberColor = TextFormatting.WHITE;

        if (!gear.meetsStatRequirements(data) || gear.level > data.getLevel()) {
            reqColor = TextFormatting.RED;
            reqNumberColor = TextFormatting.RED;
        }

        FinalizedGearStatReq req = gear.getStatRequirements();
        String reqtext = reqColor + "(Level " + reqNumberColor + gear.level;

        int dex = req.dexterity;
        int str = req.strength;
        int intr = req.intelligence;

        if (str > 0) {
            reqtext += reqColor + ", STR " + reqNumberColor + "" + str;
        }
        if (intr > 0) {
            reqtext += reqColor + ", INT " + reqNumberColor + "" + intr;
        }
        if (dex > 0) {
            reqtext += reqColor + ", DEX " + reqNumberColor + "" + dex;
        }
        reqtext += ")";

        tip.add(new SText(""));
        tip.add(new SText(reqtext));
        tip.add(new SText(""));

        if (gear.implicitStats != null) {
            tip.addAll(gear.implicitStats.GetTooltipString(info, gear));
        }

        tip.add(new StringTextComponent(""));

        List<IGearPartTooltip> list = new ArrayList<IGearPartTooltip>();

        if (gear.uniqueStats != null) {
            tip.addAll(gear.uniqueStats.GetTooltipString(info, gear));
        }

        tip.add(new StringTextComponent(""));

        List<ExactStatData> stats = new ArrayList<>();

        gear.affixes.getAllAffixes()
            .forEach(x -> stats.addAll(x.GetAllStats(gear)));

        MergedStats merged = new MergedStats(stats, info);
        list.add(merged);

        int n = 0;
        for (IGearPartTooltip part : list) {
            if (part != null) {
                tip.addAll(part.GetTooltipString(info, gear));

                if (n == list.size() - 1) {
                    gear.affixes.getAllAffixes()
                        .forEach(x -> {
                            if (x.isSocketAndEmpty()) {
                                tip.add(new SText(TextFormatting.YELLOW + "[Socket]"));
                            }
                        });
                }

                tip.add(new StringTextComponent(""));
            }
            n++;
        }

        if (Screen.hasShiftDown()) {
            if (!gear.isSalvagable) {
                tip.add(Styles.REDCOMP()
                    .appendSibling(Words.Unsalvagable.locName()));
            }
        }

        if (gear.is_unique) {
            IUnique unique = gear.uniqueStats.getUnique();

            List<String> lores = TooltipUtils.cutIfTooLong(CLOC.translate(unique.locDesc()));
            tip.add(new StringTextComponent(""));

            int i = 0;
            for (String desc : lores) {
                ITextComponent comp = new SText(gear.getRarity()
                    .textFormatting() + "");

                if (i == 0) {
                    comp.appendText("'");
                }
                comp.appendText(desc);

                if (i == lores.size() - 1) {
                    comp.appendText("'");
                }
                i++;
                tip.add(comp);

            }
        }

        tip.add(new SText(""));

        GearRarity rarity = gear.getRarity();
        tip.add(TooltipUtils.rarity(rarity));

        //tip.add(new SText(""));

        tip.add(new SText(TextFormatting.GRAY + "Durability: " + (stack.getMaxDamage() - stack.getDamage()) + "/" + stack.getMaxDamage()));

        //tip.add(new SText(""));

        if (Screen.hasShiftDown() == false) {
            tooltip
                .add(Styles.BLUECOMP()
                    .appendSibling(CLOC.tooltip("press_shift_more_info")));
        }

        List<ITextComponent> tool = TooltipUtils.removeDoubleBlankLines(tip,
            ClientContainer.INSTANCE.REMOVE_EMPTY_TOOLTIP_LINES_IF_MORE_THAN_X_LINES
                .get()
        );
        tip.clear();
        tip.addAll(tool);

    }
}
