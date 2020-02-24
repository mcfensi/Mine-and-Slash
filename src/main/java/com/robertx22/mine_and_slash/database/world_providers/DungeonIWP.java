package com.robertx22.mine_and_slash.database.world_providers;

import com.robertx22.mine_and_slash.database.map_affixes.beneficial.BonusEleDmgAffix;
import com.robertx22.mine_and_slash.saveclasses.mapitem.MapAffixData;
import com.robertx22.mine_and_slash.uncommon.enumclasses.Elements;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class DungeonIWP extends BaseWorldProvider {

    public DungeonIWP(World world, DimensionType type) {
        super(world, type);
    }

    @Override
    public String GUID() {
        return "dungeon";
    }

    @Override
    public List<MapAffixData> getMapAffixes() {
        return Arrays.asList(new MapAffixData(new BonusEleDmgAffix(Elements.Thunder), 100));
    }

    @Override
    public BiFunction<World, DimensionType, ? extends Dimension> classFactory() {
        return DungeonIWP::new;
    }

    @Override
    public String locNameForLangFile() {
        return "Dungeon Dimension";
    }

}