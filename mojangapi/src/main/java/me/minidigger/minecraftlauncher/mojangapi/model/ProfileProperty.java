package me.minidigger.minecraftlauncher.mojangapi.model;

public class ProfileProperty {

    private String name;
    private String value;
    // optional
    private String signature;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getSignature() {
        return signature;
    }

    @Override
    public String toString() {
        return "ProfileProperty [name=" + name + ", value=" + value + ", signature=" + signature + "]";
    }
}
