package com.robertx22.database.map_affixes.beneficial.ele_dmg;

import java.util.Arrays;
import java.util.List;

import com.robertx22.database.map_affixes.bases.BaseBeneficialEleAffix;
import com.robertx22.database.map_mods.bonus.ele_dmg.BonusWaterDamageMap;
import com.robertx22.saveclasses.gearitem.StatModData;

public class BonusWaterDamageAffix extends BaseBeneficialEleAffix {

	@Override
	public String Name() {
		return "BonusWaterDamageAffix";
	}

	@Override
	public List<StatModData> Stats(int percent) {
		return Arrays.asList(StatModData.NewStatusEffect(percent, new BonusWaterDamageMap()));
	}

}
