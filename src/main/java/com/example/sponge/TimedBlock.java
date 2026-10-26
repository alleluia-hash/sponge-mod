package com.example.sponge;

import java.util.function.Supplier;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** A block that counts server ticks and can change into another block when its timer runs out. */
public class TimedBlock extends BlockWithEntity {
    private final long limitTicks;
    private final Supplier<Block> next;

    public TimedBlock(AbstractBlock.Settings settings, long limitTicks, Supplier<Block> next) {
        super(settings);
        this.limitTicks = limitTicks;
        this.next = next;
    }

    /** 0 means no timer. */
    public long limitTicks() {
        return limitTicks;
    }

    /** The block this one turns into, or null. */
    public Block nextBlock() {
        return next == null ? null : next.get();
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TimedBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient) {
            return null;
        }
        return checkType(type, ModBlocks.TIMED_BE, (w, pos, s, be) -> be.tick(w, pos, s));
    }
}
