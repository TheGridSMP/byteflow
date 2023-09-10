package dev.drtheo.byteflow.bukkit.config;

import dev.drtheo.byteflow.config.ConfigData;
import dev.drtheo.byteflow.config.ConfigProvider;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.io.InputStreamReader;

public class BukkitConfigProvider extends ConfigProvider {

    public BukkitConfigProvider(JavaPlugin plugin) {
        this(plugin.getName(), plugin.getResource(plugin.getName() + ".mixins.json"));
    }

    public BukkitConfigProvider(String id, InputStream stream) {
        super(id, () -> {
            FileConfiguration config = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(stream)
            );

            return new ConfigData(
                    config.getString("package"), config.getStringList("mixins").toArray(new String[0])
            );
        });
    }
}
