package dev.drtheo.byteflow.bukkit;

import dev.drtheo.byteflow.ByteFlow;
import dev.drtheo.byteflow.bukkit.config.BukkitConfigProvider;
import dev.drtheo.byteflow.commons.mixer.AgentMixer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public final class ByteFlowPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        ByteFlow.Builder builder = ByteFlow.builder();
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            try (BukkitConfigProvider provider = BukkitConfigProvider.withPlugin(plugin)) {
                builder.withConfig(provider).withMixer(AgentMixer::create);
            } catch (IllegalArgumentException e) {
                // oh welp, it wasnt a mixin-dependant plugin
            }
        }

        Optional<ByteFlow> flow = builder.build();

        flow.ifPresent(byteFlow -> this.getServer().getScheduler().runTaskAsynchronously(this, () -> {
            try {
                long start = System.currentTimeMillis();
                byteFlow.apply();

                this.getLogger().info("Mixed-in! (took " + (System.currentTimeMillis() - start) + "ms)");
                System.out.println("Version: " + Bukkit.getServer().getVersion());
            } catch (Exception e) {
                this.getLogger().severe(e.getMessage());
                e.printStackTrace();
            }
        }));
    }
}
