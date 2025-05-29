package clienttools.utils.block.filters

import clienttools.utils.block.BlockSurfaceScanner
import net.minecraft.block.WallSignBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object WallSignBlockFilter : BlockSurfaceScanner.Filter() {
    private var blockEntity: BlockEntity? = null

    override fun allow(pos: BlockPos, world: World): Boolean {
        val blockState = world.getBlockState(pos)
        if (blockState.block !is WallSignBlock)
            return false

        blockEntity = world.getBlockEntity(pos)!!
        return true
    }

    override fun target(): BlockSurfaceScanner.Target {
        return BlockSurfaceScanner.Target.BlockEntity(blockEntity!!)
    }
}