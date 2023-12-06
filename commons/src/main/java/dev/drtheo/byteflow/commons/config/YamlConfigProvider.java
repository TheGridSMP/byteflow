package dev.drtheo.byteflow.commons.config;

import dev.drtheo.byteflow.config.ConfigData;
import dev.drtheo.byteflow.config.ConfigProvider;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class YamlConfigProvider extends ConfigProvider {

    private static final Yaml yaml = new Yaml();

    @SuppressWarnings("unchecked")
    public YamlConfigProvider(String id, ClassLoader loader, InputStream stream) {
        super(id, loader, () -> {
            Map<String, Object> map = yaml.load(stream);

            return new ConfigData(
                    (String) map.get("package"), ((List<String>) map.get("mixins")).toArray(new String[0])
            );
        });
    }
}
