package com.robertx22.mine_and_slash.onevent.data_gen.providers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.robertx22.mine_and_slash.mmorpg.Ref;
import com.robertx22.mine_and_slash.onevent.data_gen.ISerializedRegistryEntry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class SlashDataProvider<T extends ISerializedRegistryEntry> implements IDataProvider {

    private final Logger LOGGER = LogManager.getLogger();
    private final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private DataGenerator generator;
    String category;
    List<T> list;

    public SlashDataProvider(DataGenerator gen, List<T> list, String category) {
        this.generator = gen;
        this.list = list;
        this.category = category;
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        generateAll(cache);
    }

    @Override
    public String getName() {
        return category;
    }

    private Path resolve(Path path, ISerializedRegistryEntry object) {
        return path.resolve("data/" + Ref.MODID + "/" + category + "/" + object.formattedGUID() + ".json");
    }

    protected void generateAll(DirectoryCache cache) {

        Path path = this.generator.getOutputFolder();

        for (ISerializedRegistryEntry entry : list) {
            Path target = resolve(path, entry);
            try {
                IDataProvider.save(GSON, cache, entry.getSerializer().toJson(entry), target);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
