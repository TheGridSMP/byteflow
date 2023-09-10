package org.spongepowered.asm.mixin.injection;

public @interface Inject {
    String method();
    At at();
}
