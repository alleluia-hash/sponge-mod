package com.example.sponge;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class TimedBlockEntity extends BlockEntity {
    private long age;

    public TimedBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.TIMED_BE, pos, state);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (!(state.getBlock() instanceof TimedBlock timed)) {
            return;
        }
        age++;

        // Dried sponge: check for water once a second. If it touches water, it soaks.
        if (state.isOf(ModBlocks.SPONGE_DRIED)) {
            if (world.getTime() % 20 == 0 && touchesWater(world, pos)) {
                soak(world, pos);
            }
            return;
        }

        // Timed blocks: change into the next block once the timer runs out.
        if (timed.limitTicks() > 0 && age >= timed.limitTicks() && timed.nextBlock() != null) {
            world.setBlockState(pos, timed.nextBlock().getDefaultState());
        }
    }

    private static boolean touchesWater(World world, BlockPos pos) {
        for (Direction d : Direction.values()) {
            if (world.getFluidState(pos.offset(d)).isIn(FluidTags.WATER)) {
                return true;
            }
        }
        return false;
    }

    /** The sponge becomes soaked, and the water around it turns into dirty water. */
    private static void soak(World world, BlockPos pos) {
        world.setBlockState(pos, ModBlocks.SPONGE_SOAKED.getDefaultState());
        for (Direction d : Direction.values()) {
            BlockPos n = pos.offset(d);
            if (world.getFluidState(n).isIn(FluidTags.WATER)) {
                world.setBlockState(n, ModBlocks.DIRTY_WATER.getDefaultState());
            }
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putLong("Age", age);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        age = nbt.getLong("Age");
    }
}
