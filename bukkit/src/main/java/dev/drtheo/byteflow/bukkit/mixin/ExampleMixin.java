package dev.drtheo.byteflow.bukkit.mixin;

import dev.drtheo.byteflow.annotation.Mixin;
import dev.drtheo.byteflow.annotation.injection.At;
import dev.drtheo.byteflow.annotation.injection.Inject;
import dev.drtheo.byteflow.annotation.injection.InjectAt;

@Mixin("org.bukkit.craftbukkit.v1_20_R2.CraftServer")
public class ExampleMixin {
    /*@Inject(at = @At(InjectAt.RETURN), method = "getVersion")
    public String getVersion(String returned) {
        System.out.println("Hello World from a mixin. Returned value: " + returned);
        throw null;
    }*/

    @Inject(at = @At(InjectAt.HEAD), method = "getVersion")
    public String getVersion(String returned) {
        System.out.println("Hello World from a mixin. Returned value: " + returned);
        throw null;
    }
}