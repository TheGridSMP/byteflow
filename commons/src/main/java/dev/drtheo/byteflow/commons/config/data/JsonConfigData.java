package dev.drtheo.byteflow.commons.config.data;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class JsonConfigData {

    @SerializedName("package")
    private final String pkg;
    private final String[] mixins;

    public JsonConfigData(String pkg, String[] mixins) {
        this.pkg = pkg;
        this.mixins = mixins;
    }

    public String getPackage() {
        return this.pkg;
    }

    public String[] getMixins() {
        return this.mixins;
    }
}
