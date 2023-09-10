package org.spongepowered.asm.mixin.injection;

import dev.drtheo.byteflow.asm.InjectAt;

public @interface At {
    InjectAt value();
}
