package clienttools.utils.block

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class MultiBlockStructureDetector {
    abstract val priority: Int

    abstract fun reset()

    abstract fun isNeedDetect(pos: BlockPos, world: World): Boolean

    abstract fun detect(pos: BlockPos, world: World)

    abstract fun getSurfaces(): Iterable<BlockPos>
}