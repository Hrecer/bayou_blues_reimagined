package com.teamaurora.bayou_blues.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class DoubleCypressKneeBlock extends Block implements SimpleWaterloggedBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape SHAPE_LOWER = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    protected static final VoxelShape SHAPE_UPPER = Shapes.or(
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 4.0D, 15.0D),
            Block.box(3.0D, 4.0D, 3.0D, 13.0D, 16.0D, 13.0D)
    );
    @Nullable
    private final Supplier<Block> strippedBlock;

    public DoubleCypressKneeBlock(BlockBehaviour.Properties properties) {
        this(null, properties);
    }

    public DoubleCypressKneeBlock(@Nullable Supplier<Block> strippedBlock, BlockBehaviour.Properties properties) {
        super(properties);
        this.strippedBlock = strippedBlock;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(WATERLOGGED, false));
    }

    @Nullable
    @Override
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        if (toolAction == ToolActions.AXE_STRIP && this.strippedBlock != null) {
            Block stripped = this.strippedBlock.get();
            DoubleBlockHalf half = state.getValue(HALF);
            Level level = context.getLevel();
            BlockPos pos = context.getClickedPos();
            BlockPos otherPos = (half == DoubleBlockHalf.LOWER) ? pos.above() : pos.below();
            BlockState otherState = level.getBlockState(otherPos);
            if (!simulate && otherState.is(this)) {
                DoubleBlockHalf otherHalf = (half == DoubleBlockHalf.LOWER) ? DoubleBlockHalf.UPPER : DoubleBlockHalf.LOWER;
                BlockState newOther = stripped.defaultBlockState()
                        .setValue(HALF, otherHalf)
                        .setValue(WATERLOGGED, otherState.getValue(WATERLOGGED));
                level.setBlock(otherPos, newOther, 11);
            }
            return stripped.defaultBlockState()
                    .setValue(HALF, half)
                    .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
        }
        return super.getToolModifiedState(state, context, toolAction, simulate);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER ? SHAPE_LOWER : SHAPE_UPPER;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
        } else {
            BlockState belowState = level.getBlockState(pos.below());
            return belowState.getBlock() instanceof DoubleCypressKneeBlock && belowState.getValue(HALF) == DoubleBlockHalf.LOWER;
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        DoubleBlockHalf half = state.getValue(HALF);
        if (facing.getAxis() != Direction.Axis.Y || (half == DoubleBlockHalf.LOWER) != (facing == Direction.UP)
                || (facingState.getBlock() instanceof DoubleCypressKneeBlock && facingState.getValue(HALF) != half)) {
            if (half == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !state.canSurvive(level, currentPos)) {
                return Blocks.AIR.defaultBlockState();
            }
            if (state.getValue(WATERLOGGED)) {
                level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
            }
            return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
        }
        return Blocks.AIR.defaultBlockState();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        if (pos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(pos.above()).canBeReplaced(context)) {
            FluidState fluid = level.getFluidState(pos);
            return this.defaultBlockState()
                    .setValue(HALF, DoubleBlockHalf.LOWER)
                    .setValue(WATERLOGGED, fluid.is(FluidTags.WATER));
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BlockPos abovePos = pos.above();
        boolean waterAbove = level.getFluidState(abovePos).is(FluidTags.WATER);
        level.setBlock(abovePos, this.defaultBlockState()
                .setValue(HALF, DoubleBlockHalf.UPPER)
                .setValue(WATERLOGGED, waterAbove), 3);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            DoubleBlockHalf half = state.getValue(HALF);
            if (half == DoubleBlockHalf.UPPER) {
                BlockPos belowPos = pos.below();
                BlockState belowState = level.getBlockState(belowPos);
                if (belowState.getBlock() == state.getBlock() && belowState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                    BlockState replacement = belowState.getValue(WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                    level.setBlock(belowPos, replacement, 35);
                    level.levelEvent(player, 2001, belowPos, Block.getId(belowState));
                }
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return !state.getValue(WATERLOGGED);
    }
}
