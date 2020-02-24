package com.robertx22.mine_and_slash.uncommon.capability.world;

import com.robertx22.mine_and_slash.mmorpg.Ref;
import com.robertx22.mine_and_slash.saveclasses.dungeon_dimension.DungeonData;
import com.robertx22.mine_and_slash.saveclasses.dungeon_dimension.DungeonDimensionData;
import com.robertx22.mine_and_slash.saveclasses.item_classes.MapItemData;
import com.robertx22.mine_and_slash.uncommon.capability.bases.BaseProvider;
import com.robertx22.mine_and_slash.uncommon.capability.bases.BaseStorage;
import com.robertx22.mine_and_slash.uncommon.capability.bases.ICommonCap;
import com.robertx22.mine_and_slash.uncommon.datasaving.base.LoadSave;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class WorldMapCap {

    public static final ResourceLocation RESOURCE = new ResourceLocation(Ref.MODID, "adventure_map");

    @CapabilityInject(IWorldMapData.class)
    public static final Capability<IWorldMapData> Data = null;

    public interface IWorldMapData extends ICommonCap {

        float getLootMultiplier(BlockPos pos);

        float getExpMultiplier(BlockPos pos);

        int getLevel(BlockPos pos);

        int getTier(BlockPos pos);

        MapItemData getMap(BlockPos pos);

        void init(MapItemData map, BlockPos pos);

        boolean shouldDeleteFolderOnServerShutdown();

        DungeonDimensionData getData();
    }

    @Mod.EventBusSubscriber
    public static class EventHandler {

        @SubscribeEvent
        public static void onEntityConstruct(AttachCapabilitiesEvent<World> event) {
            event.addCapability(RESOURCE, new Provider());
        }

    }

    public static class Provider extends BaseProvider<IWorldMapData> {

        @Override
        public IWorldMapData defaultImpl() {
            return new DefaultImpl();
        }

        @Override
        public Capability<IWorldMapData> dataInstance() {
            return Data;
        }
    }

    static String EVENTS_LOC = Ref.MODID + ":events";
    static String DATA_LOC = Ref.MODID + ":data";

    public static class DefaultImpl implements IWorldMapData {

        DungeonDimensionData data = new DungeonDimensionData();

        @Override
        public CompoundNBT saveToNBT() {

            CompoundNBT nbt = new CompoundNBT();

            if (data != null) {
                LoadSave.Save(data, nbt, DATA_LOC);
            }

            return nbt;

        }

        @Override
        public void loadFromNBT(CompoundNBT nbt) {

            data = LoadSave.Load(DungeonDimensionData.class, new DungeonDimensionData(), nbt, DATA_LOC);

            if (data == null) {
                data = new DungeonDimensionData();
            }

        }

        @Override
        public void init(MapItemData map, BlockPos pos) {

            DungeonData d = new DungeonData();
            d.mapData = map;

            data.setupNew(d, pos);

        }

        @Override
        public boolean shouldDeleteFolderOnServerShutdown() {
            return data.getDungeonsAmount() > 5000;
        }

        @Override
        public DungeonDimensionData getData() {
            return data;
        }

        @Override
        public float getLootMultiplier(BlockPos pos) {

            return this.getMap(pos)
                .getBonusLootMulti();

        }

        @Override
        public float getExpMultiplier(BlockPos pos) {
            return 1 + getMap(pos).tier * 0.05F;
        }

        @Override
        public int getLevel(BlockPos pos) {
            return this.getMap(pos).level;
        }

        @Override
        public int getTier(BlockPos pos) {
            return this.getMap(pos).tier;
        }

        @Override
        public MapItemData getMap(BlockPos pos) {
            return data.getData(pos).mapData;
        }

    }

    public static class Storage extends BaseStorage<IWorldMapData> {

    }

}
