package dev.drtheo.byteflow.bukkit.mixin;

import dev.drtheo.byteflow.annotation.Mixin;
import dev.drtheo.byteflow.annotation.injection.At;
import dev.drtheo.byteflow.annotation.injection.Inject;
import dev.drtheo.byteflow.annotation.injection.InjectAt;
import org.bukkit.Bukkit;

@Mixin("net.minecraft.world.entity.EntityLiving")
public class EntityMixin {

    @Inject(
            at = @At(InjectAt.HEAD), method = "c"
    )
    public void c(float f) {
        Bukkit.broadcastMessage("Finished setting health");
        throw null;
    }
}
