package dev.drtheo.byteflow.commons.config;

import com.google.gson.Gson;
import dev.drtheo.byteflow.commons.config.data.JsonConfigData;
import dev.drtheo.byteflow.config.ConfigData;
import dev.drtheo.byteflow.config.ConfigProvider;

import java.io.*;

public class JsonConfigProvider extends ConfigProvider {

    private static final Gson gson = new Gson();

    public JsonConfigProvider(String id, InputStream stream) {
        super(id, () -> {
            JsonConfigData data = gson.fromJson(
                    new InputStreamReader(stream), JsonConfigData.class
            );

            return new ConfigData(data.getPackage(), data.getMixins());
        });
    }
}
