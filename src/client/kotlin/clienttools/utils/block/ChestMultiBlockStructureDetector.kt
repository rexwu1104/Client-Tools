package clienttools.utils.block

import net.minecraft.block.Blocks
import net.minecraft.block.ChestBlock
import net.minecraft.block.enums.ChestType
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

object ChestMultiBlockStructureDetector : MultiBlockStructureDetector() {
    private val surfaces = arrayListOf<BlockPos>()

    override val priority: Int
        get() = Int.MAX_VALUE

    override fun reset() {
        surfaces.clear()
    }

    override fun isNeedDetect(pos: BlockPos, world: World): Boolean {
        val block = world.getBlockState(pos)
        return block.isOf(Blocks.CHEST) || block.isOf(Blocks.TRAPPED_CHEST)
    }

    override fun detect(pos: BlockPos, world: World) {
        val block = world.getBlockState(pos)
        val facing = block[ChestBlock.FACING]
        val positions = hashSetOf(pos)
        when (block[ChestBlock.CHEST_TYPE]) {
            ChestType.LEFT -> {
                positions.add(pos.offset(facing.rotateYClockwise()))
            }
            ChestType.RIGHT -> {
                positions.add(pos.offset(facing.rotateYCounterclockwise()))
            }
            else -> Unit
        }

        val horizontals = Direction.entries.filter { it.axis.isHorizontal }
        val surfaces = positions.flatMap { structPos -> horizontals.map { structPos.offset(it) } }
        this.surfaces.addAll(surfaces)
    }

    override fun getSurfaces(): Iterable<BlockPos> {
        return surfaces.asIterable()
    }
}