package dev.drtheo.byteflow.commons.mixer;

import dev.drtheo.byteflow.commons.util.TransformQuery;
import dev.drtheo.byteflow.mixer.Mixer;
import net.bytebuddy.agent.ByteBuddyAgent;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AgentMixer implements Mixer {

    private static final String ATTACH_MOD_PATH = "jmods/jdk.attach.jmod";
    private final Instrumentation instrumentation;

    private final MixinClassTransformer transformer;
    private final List<Class<?>> retransformed = new ArrayList<>();

    private boolean finished = false;

    private AgentMixer(Instrumentation instrumentation) {
        this.transformer = new MixinClassTransformer(this);
        this.instrumentation = instrumentation;
    }

    public static AgentMixer create() {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        File javaHome = new File(System.getProperty("java.home"));

        if (loader instanceof URLClassLoader urlLoader) {
            try {
                Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                method.setAccessible(true);

                File toolsJar = new File(javaHome, "lib/tools.jar");
                if (!toolsJar.exists())
                    throw new RuntimeException("Not running with JDK!");

                method.invoke(urlLoader, toolsJar.toURI().toURL());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else {
            Path attachMod = javaHome.toPath().resolve(ATTACH_MOD_PATH);

            if (Files.notExists(attachMod)) {
                throw new RuntimeException("Not running with JDK!");
            }
        }

        return new AgentMixer(ByteBuddyAgent.install());
    }

    @Override
    public void prepare() {
        this.instrumentation.addTransformer(this.transformer, true);
    }

    @Override
    public void mix(Class<?> clazz, byte[] bytes) {
        this.transformer.addQuery(new TransformQuery(clazz, bytes));
        this.retransformed.add(clazz);
    }

    @Override
    public void finish() {
        try {
            this.instrumentation.retransformClasses(this.retransformed.toArray(new Class[0]));

            while (!this.finished) {
                Thread.sleep(200L);
            }
        } catch (UnmodifiableClassException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected void markFinished() {
        this.instrumentation.removeTransformer(this.transformer);
        this.finished = true;
    }
}
