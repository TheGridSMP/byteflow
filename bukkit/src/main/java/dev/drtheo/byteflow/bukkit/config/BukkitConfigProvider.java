package dev.drtheo.byteflow.bukkit.config;

import dev.drtheo.byteflow.config.ConfigData;
import dev.drtheo.byteflow.config.ConfigProvider;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.io.InputStreamReader;

public class BukkitConfigProvider extends ConfigProvider implements AutoCloseable {

    public static BukkitConfigProvider withPlugin(Plugin plugin) throws IllegalArgumentException {
        InputStream stream = plugin.getResource(plugin.getName() + ".mixins.json");

        if (stream == null)
            throw new IllegalArgumentException("InputStream cannot be null!");

        System.out.println(plugin.getClass().getClassLoader());
        return new BukkitConfigProvider(plugin.getName(), plugin.getClass().getClassLoader(), stream);
    }

    public BukkitConfigProvider(String id, ClassLoader loader, InputStream stream) {
        super(id, loader, () -> {
            FileConfiguration config = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(stream)
            );

            return new ConfigData(
                    config.getString("package"), config.getStringList("mixins").toArray(new String[0])
            );
        });
    }

    @Override
    public void close() {
        // dummy
    }
}
