package com.robertx22.database.stat_types.elementals.pene;

import java.util.Arrays;
import java.util.List;

import com.robertx22.stats.IStatEffect;
import com.robertx22.stats.IStatEffects;
import com.robertx22.stats.Stat;
import com.robertx22.stats.StatEffects.ElementalPeneEffect;
import com.robertx22.uncommon.enumclasses.Elements;

public class FirePene extends Stat implements IStatEffects {
	public static String GUID = "Fire Penetration";

	@Override
	public List<IStatEffect> GetEffects() {
		return Arrays.asList(new ElementalPeneEffect());
	}

	public FirePene() {
	}

	@Override
	public String Guid() {
		return GUID;
	}

	@Override
	public boolean ScalesToLevel() {
		return true;
	}

	@Override
	public Elements Element() {
		return Elements.Fire;
	}

	@Override
	public boolean IsPercent() {
		return false;
	}

}