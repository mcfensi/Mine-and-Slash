package com.robertx22.mine_and_slash.database.stats.stat_types.game_changers;

import com.robertx22.mine_and_slash.database.stats.Stat;
import com.robertx22.mine_and_slash.database.stats.stat_types.defense.Armor;
import com.robertx22.mine_and_slash.database.stats.stat_types.generated.ElementalPene;
import com.robertx22.mine_and_slash.saveclasses.StatData;
import com.robertx22.mine_and_slash.uncommon.capability.EntityCap;
import com.robertx22.mine_and_slash.uncommon.enumclasses.Elements;
import com.robertx22.mine_and_slash.uncommon.interfaces.IAffectsStats;

public class RecklessBlows extends BaseGameChangerTrait implements IAffectsStats {

    private RecklessBlows() {
    }

    public static final RecklessBlows INSTANCE = new RecklessBlows();

    @Override
    public String locDescForLangFile() {
        return "All your penetrations are doubled, but your armor is halved.";
    }

    @Override
    public String getIconPath() {
        return "game_changers/reckless_blows";
    }

    @Override
    public String locNameForLangFile() {
        return "Reckless Blows";
    }

    @Override
    public String GUID() {
        return "reckless_blows_trait";
    }

    @Override
    public void affectStats(EntityCap.UnitData data, StatData statData) {

        for (Stat stat : new ElementalPene(Elements.Physical).generateAllPossibleStatVariations()) {
            data.getUnit().getStat(stat).Flat *= 2;
        }

        data.getUnit().getStat(Armor.GUID).Flat /= 2;
    }
}