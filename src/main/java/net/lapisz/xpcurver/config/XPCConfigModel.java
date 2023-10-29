package net.lapisz.xpcurver.config;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.Sync;

@Modmenu(modId = "xpcurver")
@Config(name="xpcurver", wrapperName="XPCConfig")
public class XPCConfigModel {
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public String level_up_xp_1_15 = "7 + level * 2";

    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public String level_up_xp_16_30 = "37 + (level - 15) * 5";

    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public String level_up_xp_31_plus = "112 + (level - 30) * 9";
}
