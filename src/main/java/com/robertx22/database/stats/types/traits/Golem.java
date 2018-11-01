package com.robertx22.database.stats.types.traits;

import com.robertx22.saveclasses.Unit;
import com.robertx22.stats.IAffectsOtherStats;
import com.robertx22.stats.Trait;

public class Golem extends Trait implements IAffectsOtherStats {

	public static String GUID = "Golem";

	@Override
	public String Name() {
		return GUID;
	}

	@Override
	public void TryAffectOtherStats(Unit unit) {

		unit.health().Multi += 10;

	}

}