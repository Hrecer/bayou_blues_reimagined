package com.teamaurora.bayou_blues.core.other;

import com.teamaurora.bayou_blues.common.entity.CypressBoatEntity;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

import javax.annotation.Nonnull;

/**
 * Dispenser behavior for the cypress boat, mirroring the vanilla boat's.
 *
 * @see net.minecraft.core.dispenser.BoatDispenseItemBehavior
 */
public class CypressBoatDispenseBehavior extends DefaultDispenseItemBehavior {
    private final DefaultDispenseItemBehavior defaultBehavior = new DefaultDispenseItemBehavior();

    @Override
    @Nonnull
    public ItemStack execute(BlockSource source, @Nonnull ItemStack stack) {
        Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
        Level level = source.getLevel();
        double offset = 0.5625D + (double) com.teamaurora.bayou_blues.core.registry.BayouBluesEntities.CYPRESS_BOAT.get().getWidth() / 2.0D;
        double x = source.x() + (double) direction.getStepX() * offset;
        double y = source.y() + (double) ((float) direction.getStepY() * 1.125F);
        double z = source.z() + (double) direction.getStepZ() * offset;
        net.minecraft.core.BlockPos blockPos = source.getPos().relative(direction);
        double yOffset;
        if (level.getFluidState(blockPos).is(FluidTags.WATER)) {
            yOffset = 1.0D;
        } else {
            if (!level.getBlockState(blockPos).isAir() || !level.getFluidState(blockPos.below()).is(FluidTags.WATER)) {
                return this.defaultBehavior.dispense(source, stack);
            }
            yOffset = 0.0D;
        }

        CypressBoatEntity boat = new CypressBoatEntity(level, x, y + yOffset, z);
        boat.setYRot(direction.toYRot());
        level.addFreshEntity(boat);
        stack.shrink(1);
        return stack;
    }
}
