package com.peeko32213.unusualprehistory.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockRexHead extends Block {
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    private static final VoxelShape UP_SHAPE = Shapes.or(Block.box(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D),
            Block.box(4.0D, 6.0D, 4.0D, 12.0D, 16.0D, 12.0D));
    private static final VoxelShape DOWN_SHAPE = Shapes.or(Block.box(0.0D, 9.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 10.0D, 12.0D));
    private static final VoxelShape SOUTH_SHAPE = Shapes.or(Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 7.0D),
            Block.box(4.0D, 4.0D, 6.0D, 12.0D, 12.0D, 16.0D));
    private static final VoxelShape NORTH_SHAPE = Shapes.or(Block.box(0.0D, 0.0D, 9.0D, 16.0D, 16.0D, 16.0D),
            Block.box(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 10.0D));
    private static final VoxelShape EAST_SHAPE = Shapes.or(Block.box(0.0D, 0.0D, 0.0D, 7.0D, 16.0D, 16.0D),
            Block.box(6.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D));
    private static final VoxelShape WEST_SHAPE = Shapes.or(Block.box(9.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 4.0D, 4.0D, 10.0D, 12.0D, 12.0D));

    public BlockRexHead() {
        super(Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace());
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public VoxelShape getShape(BlockState p_54561_, BlockGetter p_54562_, BlockPos p_54563_, CollisionContext p_54564_) {
        switch(p_54561_.getValue(FACING)) {
            case NORTH:
                return NORTH_SHAPE;
            case SOUTH:
                return SOUTH_SHAPE;
            case EAST:
                return EAST_SHAPE;
            case WEST:
                return WEST_SHAPE;
            case UP:
                return UP_SHAPE;
            default:
                return DOWN_SHAPE;
        }
    }
}