package clienttools.utils.block

import net.minecraft.block.Blocks
import net.minecraft.inventory.Inventory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

object InventoryMultiBlockStructureDetector : MultiBlockStructureDetector() {
    private var pos: BlockPos = BlockPos.ORIGIN
    override val priority: Int
        get() = 1

    override fun reset() {
        pos = BlockPos.ORIGIN
    }

    override fun isNeedDetect(pos: BlockPos, world: World): Boolean {
        val block = world.getBlockState(pos)
        val blockEntity = world.getBlockEntity(pos)
        return blockEntity is Inventory || block.isOf(Blocks.ENDER_CHEST)
    }

    override fun detect(pos: BlockPos, world: World) {
        this.pos = pos
    }

    override fun getSurfaces(): Iterable<BlockPos> {
        return Direction.entries.filter { it.axis.isHorizontal }.map { pos.offset(it) }
    }
}