package com.example.sponge;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ModBlocks {
    private ModBlocks() {}

    /** One Minecraft day in ticks. */
    public static final long DAY = 24000L;

    // Normal sponge dries after 1 day.
    public static final Block SPONGE_NORMAL = register("sponge_normal",
        new TimedBlock(AbstractBlock.Settings.copy(Blocks.SPONGE), DAY, () -> ModBlocks.SPONGE_DRIED));

    // Dried sponge: soaks itself when it touches water (see TimedBlockEntity).
    public static final Block SPONGE_DRIED = register("sponge_dried",
        new TimedBlock(AbstractBlock.Settings.copy(Blocks.SPONGE), 0L, null));

    // Soaked sponge dries after 3 days.
    public static final Block SPONGE_SOAKED = register("sponge_soaked",
        new TimedBlock(AbstractBlock.Settings.copy(Blocks.WET_SPONGE), 3 * DAY, () -> ModBlocks.SPONGE_DRIED));

    // Vanilla planks can't be changed without a mixin, so this block stands in for them.
    // After 5 days it turns into Garbage Oak.
    public static final Block AGING_PLANKS = register("aging_planks",
        new TimedBlock(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS), 5 * DAY, () -> ModBlocks.GARBAGE_OAK));

    public static final Block GARBAGE_OAK = register("garbage_oak",
        new Block(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)));

    // Placed by the soaking sponge. Not a real fluid, so you can't swim in it, but standing in it poisons you.
    public static final Block DIRTY_WATER = registerHidden("dirty_water",
        new Block(AbstractBlock.Settings.create().strength(0.2F).nonOpaque().noCollision()));

    public static BlockEntityType<TimedBlockEntity> TIMED_BE;

    private static Block register(String name, Block block) {
        Identifier id = new Identifier(SpongeMod.MOD_ID, name);
        Registry.register(Registries.BLOCK, id, block);
        Registry.register(Registries.ITEM, id, new BlockItem(block, new Item.Settings()));
        return block;
    }

    private static Block registerHidden(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(SpongeMod.MOD_ID, name), block);
    }

    public static void init() {
        TIMED_BE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(SpongeMod.MOD_ID, "timed"),
            FabricBlockEntityTypeBuilder.create(TimedBlockEntity::new,
                SPONGE_NORMAL, SPONGE_DRIED, SPONGE_SOAKED, AGING_PLANKS).build());

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.add(SPONGE_NORMAL);
            entries.add(SPONGE_DRIED);
            entries.add(SPONGE_SOAKED);
            entries.add(AGING_PLANKS);
            entries.add(GARBAGE_OAK);
        });
    }
}
