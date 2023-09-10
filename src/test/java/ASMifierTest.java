import dev.drtheo.byteflow.asm.InjectAt;
import org.objectweb.asm.util.ASMifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin("Dummy")
public class ASMifierTest {

    @Inject(method = "", at = @At(InjectAt.HEAD))
    public void test() {
        System.out.println("hI!");

        // actual stuff
        this.id$test();
    }

    public void id$test() {

    }
}
