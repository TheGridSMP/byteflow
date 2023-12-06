package dev.drtheo.byteflow.bukkit;

import dev.drtheo.byteflow.ByteFlow;
import dev.drtheo.byteflow.annotation.Mixin;
import dev.drtheo.byteflow.bukkit.config.BukkitConfigProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class ByteFlowPlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        ByteFlow.Builder builder = ByteFlow.builder();
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            try (BukkitConfigProvider provider = BukkitConfigProvider.withPlugin(plugin)) {
                builder.withConfig(provider);
            } catch (IllegalArgumentException e) {
                // oh welp, it wasnt a mixin-dependant plugin
            }
        }

        System.out.println(Mixin.class);
        System.out.println(this.getClass().getClassLoader());

        builder.build().ifPresent(flow -> {
            try {
                long start = System.nanoTime();
                flow.apply();

                this.getLogger().info("Mixed-in! (took " + ((System.nanoTime() - start) / 1000) + "ms)");
            } catch (Exception e) {
                this.getLogger().severe(e.toString());
            }
        });
    }
}
