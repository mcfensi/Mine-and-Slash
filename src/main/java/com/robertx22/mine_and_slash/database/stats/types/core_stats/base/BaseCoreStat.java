package com.robertx22.mine_and_slash.database.stats.types.core_stats.base;

import com.robertx22.mine_and_slash.database.stats.Stat;
import com.robertx22.mine_and_slash.saveclasses.ExactStatData;
import com.robertx22.mine_and_slash.saveclasses.StatData;
import com.robertx22.mine_and_slash.saveclasses.gearitem.gear_bases.TooltipInfo;
import com.robertx22.mine_and_slash.uncommon.capability.entity.EntityCap;
import com.robertx22.mine_and_slash.uncommon.enumclasses.Elements;
import com.robertx22.mine_and_slash.uncommon.localization.Styles;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseCoreStat extends Stat implements ICoreStat {

    @Override
    public StatGroup statGroup() {
        return StatGroup.CoreStat;
    }

    @Override
    public boolean IsPercent() {
        return false;
    }

    @Override
    public Elements getElement() {
        return null;
    }

    @Override
    public float amountToReach100Percent() {
        return 10;
    }

    public float getPercent(StatData data) {
        return data.getAverageValue() * 100;
    }

    public List<ITextComponent> getCoreStatTooltip(EntityCap.UnitData unitdata, StatData data) {

        TooltipInfo info = new TooltipInfo(unitdata, null);

        List<ITextComponent> list = new ArrayList<>();
        list.add(Styles.GREENCOMP()
            .appendText("Stats that benefit: "));
        getMods(data).forEach(x -> list.addAll(x.GetTooltipString(info)));
        return list;

    }

    public List<ExactStatData> getMods(StatData data) {
        return this.statsThatBenefit()
            .stream()
            .map(x -> x.ToExactStat((int) getPercent(data))
            )
            .collect(Collectors.toList());

    }

    @Override
    public void addToOtherStats(EntityCap.UnitData unitdata, StatData data) {
        getMods(data).forEach(x -> x.applyStats(unitdata));
    }

}


