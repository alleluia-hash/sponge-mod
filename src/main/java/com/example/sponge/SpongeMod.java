package com.example.sponge;

import net.fabricmc.api.ModInitializer;

public class SpongeMod implements ModInitializer {
    public static final String MOD_ID = "sponge_mod";

    @Override
    public void onInitialize() {
        ModBlocks.init();
        DirtyWaterPoison.register();
    }
}
