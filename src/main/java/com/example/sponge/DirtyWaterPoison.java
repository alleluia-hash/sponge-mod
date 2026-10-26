package com.example.sponge;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

/** Counts how long each player stands in dirty water, then poisons them for that long when they leave. */
public final class DirtyWaterPoison {
    private DirtyWaterPoison() {}

    private static final Map<UUID, Integer> TICKS_IN_WATER = new HashMap<>();

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                BlockPos feet = player.getBlockPos();
                boolean inside = player.getWorld().getBlockState(feet).isOf(ModBlocks.DIRTY_WATER)
                    || player.getWorld().getBlockState(feet.up()).isOf(ModBlocks.DIRTY_WATER);
                UUID id = player.getUuid();

                if (inside) {
                    TICKS_IN_WATER.merge(id, 1, Integer::sum);
                } else {
                    Integer ticks = TICKS_IN_WATER.remove(id);
                    if (ticks != null) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, ticks));
                    }
                }
            }
        });

        ServerPlayConnectionEvents.DISCONNECT.register(
            (handler, server) -> TICKS_IN_WATER.remove(handler.player.getUuid()));
    }
}
