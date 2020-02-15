package com.robertx22.mine_and_slash.database.stats;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.robertx22.mine_and_slash.database.IGUID;
import com.robertx22.mine_and_slash.database.serialization.statmods.SerializableStatMod;
import com.robertx22.mine_and_slash.db_lists.Rarities;
import com.robertx22.mine_and_slash.db_lists.registry.SlashRegistryType;
import com.robertx22.mine_and_slash.db_lists.registry.empty_entries.EmptyStatMod;
import com.robertx22.mine_and_slash.onevent.data_gen.ISerializable;
import com.robertx22.mine_and_slash.onevent.data_gen.ISerializedRegistryEntry;
import com.robertx22.mine_and_slash.saveclasses.gearitem.gear_bases.Rarity;
import com.robertx22.mine_and_slash.uncommon.enumclasses.StatTypes;
import com.robertx22.mine_and_slash.uncommon.interfaces.IWeighted;
import com.robertx22.mine_and_slash.uncommon.interfaces.data_items.IRarity;

public abstract class StatMod implements IWeighted, IRarity, IGUID, ISerializedRegistryEntry<StatMod>,
        ISerializable<StatMod> {

    public static EmptyStatMod EMPTY = EmptyStatMod.getInstance();

    public float multiplier = 1F;

    public float sizeMultiplier() {
        return multiplier;
    }

    @Override
    public SlashRegistryType getSlashRegistryType() {
        return SlashRegistryType.STATMOD;
    }

    @Override
    public int Weight() {
        return this.getRarity().Weight();
    }

    @Override
    public Rarity getRarity() {
        return Rarities.Gears.get(getRarityRank());
    }

    @Override
    public int Tier() {
        return 0;
    }

    @Override
    public int getRarityRank() {
        return 1;
    }

    public abstract Stat GetBaseStat();

    public abstract float Min();

    public abstract float Max();

    public abstract StatTypes Type();

    @Override
    public String GUID() {
        return this.GetBaseStat().GUID() + "_" + this.Type().id;
    }

    public float getFloatByPercent(int percent) {
        return (Min() + (Max() - Min()) * percent / 100) * multiplier;
    }

    public float getFloatByPercentWithoutMin(int percent) {
        return (Max() * percent / 100) * multiplier;

    }

    @Override
    public JsonObject toJson() {

        JsonObject json = new JsonObject();

        json.add("min", new JsonPrimitive(Min()));
        json.add("max", new JsonPrimitive(Max()));
        json.add("multi", new JsonPrimitive(multiplier));
        json.add("stat", new JsonPrimitive(GetBaseStat().GUID()));
        json.add("type", new JsonPrimitive(Type().name()));
        json.add("guid", new JsonPrimitive(GUID()));

        return json;
    }

    @Override
    public StatMod fromJson(JsonObject json) {

        float min = json.get("min").getAsFloat();
        float max = json.get("max").getAsFloat();
        float multi = json.get("multi").getAsFloat();
        String stat = json.get("stat").getAsString();
        String guid = json.get("guid").getAsString();
        StatTypes type = StatTypes.valueOf(json.get("type").getAsString());

        return new SerializableStatMod(stat, min, max, type, multi, guid);
    }

    public StatMod multi(float multiplier) {
        this.multiplier = multiplier;
        return this;
    }

}