package com.teamaurora.bayou_blues.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class WoodPostBlock extends RotatedPillarBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty CHAIN_DOWN = BooleanProperty.create("chain_down");
    public static final BooleanProperty CHAIN_UP = BooleanProperty.create("chain_up");
    public static final BooleanProperty CHAIN_NORTH = BooleanProperty.create("chain_north");
    public static final BooleanProperty CHAIN_SOUTH = BooleanProperty.create("chain_south");
    public static final BooleanProperty CHAIN_WEST = BooleanProperty.create("chain_west");
    public static final BooleanProperty CHAIN_EAST = BooleanProperty.create("chain_east");
    public static final BooleanProperty[] CHAIN_PROPERTIES = new BooleanProperty[]{
            CHAIN_DOWN, CHAIN_UP, CHAIN_NORTH, CHAIN_SOUTH, CHAIN_WEST, CHAIN_EAST
    };

    private static final VoxelShape SHAPE_X = Block.box(0.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D);
    private static final VoxelShape SHAPE_Y = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private static final VoxelShape SHAPE_Z = Block.box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 16.0D);

    @Nullable
    private final Supplier<Block> strippedBlock;

    public WoodPostBlock(BlockBehaviour.Properties properties) {
        this(null, properties);
    }

    public WoodPostBlock(@Nullable Supplier<Block> strippedBlock, BlockBehaviour.Properties properties) {
        super(properties);
        this.strippedBlock = strippedBlock;
        BlockState defaultState = this.stateDefinition.any()
                .setValue(AXIS, Direction.Axis.Y)
                .setValue(WATERLOGGED, false);
        for (BooleanProperty prop : CHAIN_PROPERTIES) {
            defaultState = defaultState.setValue(prop, false);
        }
        this.registerDefaultState(defaultState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, WATERLOGGED, CHAIN_DOWN, CHAIN_UP, CHAIN_NORTH, CHAIN_SOUTH, CHAIN_WEST, CHAIN_EAST);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(AXIS)) {
            case X -> SHAPE_X;
            case Z -> SHAPE_Z;
            default -> SHAPE_Y;
        };
    }

    private boolean shouldConnectChain(BlockGetter level, BlockPos pos, Direction dir) {
        BlockState neighbor = level.getBlockState(pos.relative(dir));
        if (neighbor.getBlock() instanceof ChainBlock && neighbor.getValue(ChainBlock.AXIS) == dir.getAxis()) {
            return true;
        }
        if (dir == Direction.DOWN && neighbor.getBlock() instanceof LanternBlock && neighbor.getValue(LanternBlock.HANGING)) {
            return true;
        }
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        FluidState fluid = level.getFluidState(pos);
        BlockState state = this.defaultBlockState()
                .setValue(AXIS, context.getClickedFace().getAxis())
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
        for (Direction dir : Direction.values()) {
            state = state.setValue(CHAIN_PROPERTIES[dir.ordinal()], shouldConnectChain(level, pos, dir));
        }
        return state;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return state.setValue(CHAIN_PROPERTIES[facing.ordinal()], shouldConnectChain(level, currentPos, facing));
    }

    @Nullable
    @Override
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        if (toolAction == ToolActions.AXE_STRIP && this.strippedBlock != null) {
            BlockState stripped = this.strippedBlock.get().defaultBlockState()
                    .setValue(AXIS, state.getValue(AXIS))
                    .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
            for (BooleanProperty prop : CHAIN_PROPERTIES) {
                stripped = stripped.setValue(prop, state.getValue(prop));
            }
            return stripped;
        }
        return super.getToolModifiedState(state, context, toolAction, simulate);
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
