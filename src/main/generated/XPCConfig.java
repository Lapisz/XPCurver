package net.lapisz.xpcurver.config;

import blue.endless.jankson.Jankson;
import io.wispforest.owo.config.ConfigWrapper;
import io.wispforest.owo.config.Option;
import io.wispforest.owo.util.Observable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class XPCConfig extends ConfigWrapper<net.lapisz.xpcurver.config.XPCConfigModel> {

    public final Keys keys = new Keys();

    private final Option<java.lang.String> level_up_xp_1_15 = this.optionForKey(this.keys.level_up_xp_1_15);
    private final Option<java.lang.String> level_up_xp_16_30 = this.optionForKey(this.keys.level_up_xp_16_30);
    private final Option<java.lang.String> level_up_xp_31_plus = this.optionForKey(this.keys.level_up_xp_31_plus);

    private XPCConfig() {
        super(net.lapisz.xpcurver.config.XPCConfigModel.class);
    }

    private XPCConfig(Consumer<Jankson.Builder> janksonBuilder) {
        super(net.lapisz.xpcurver.config.XPCConfigModel.class, janksonBuilder);
    }

    public static XPCConfig createAndLoad() {
        var wrapper = new XPCConfig();
        wrapper.load();
        return wrapper;
    }

    public static XPCConfig createAndLoad(Consumer<Jankson.Builder> janksonBuilder) {
        var wrapper = new XPCConfig(janksonBuilder);
        wrapper.load();
        return wrapper;
    }

    public java.lang.String level_up_xp_1_15() {
        return level_up_xp_1_15.value();
    }

    public void level_up_xp_1_15(java.lang.String value) {
        level_up_xp_1_15.set(value);
    }

    public java.lang.String level_up_xp_16_30() {
        return level_up_xp_16_30.value();
    }

    public void level_up_xp_16_30(java.lang.String value) {
        level_up_xp_16_30.set(value);
    }

    public java.lang.String level_up_xp_31_plus() {
        return level_up_xp_31_plus.value();
    }

    public void level_up_xp_31_plus(java.lang.String value) {
        level_up_xp_31_plus.set(value);
    }


    public static class Keys {
        public final Option.Key level_up_xp_1_15 = new Option.Key("level_up_xp_1_15");
        public final Option.Key level_up_xp_16_30 = new Option.Key("level_up_xp_16_30");
        public final Option.Key level_up_xp_31_plus = new Option.Key("level_up_xp_31_plus");
    }
}

