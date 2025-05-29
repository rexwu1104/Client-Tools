package clienttools.utils.block

import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

object BlockSurfaceScanner {
    private val detectors = PriorityQueue<MultiBlockStructureDetector>(
        compareByDescending { it.priority }
    )

    fun <T> scan(startPos: BlockPos, world: World, collector: ResultCollector<T>): T {
        val detector = detectors.first { it.isNeedDetect(startPos, world) }.also { it.reset() }
        val surfaces = detector.detect(startPos, world).run { detector.getSurfaces() }
        for (surface in surfaces) {
            collector.add(surface, world)
        }

        return collector.collect()
    }

    fun addDetector(detector: MultiBlockStructureDetector) {
        detectors.add(detector)
    }

    abstract class ResultCollector<T> {
        protected abstract val filters: List<Filter>
        protected val targets: ArrayList<Target> = arrayListOf()

        abstract fun collect(): T

        fun add(pos: BlockPos, world: World): Boolean {
            return filters.filter {
                if (it.allow(pos, world))
                    targets.add(it.target())
                else
                    false
            }.isNotEmpty()
        }
    }

    sealed interface Target {
        val pos: BlockPos

        data class Block(
            override val pos: BlockPos,
            val state: BlockState
        ) : Target

        data class BlockEntity(
            val blockEntity: net.minecraft.block.entity.BlockEntity
        ) : Target {
            override val pos: BlockPos = blockEntity.pos
        }

        data class Entity(
            val entity: net.minecraft.entity.Entity
        ) : Target {
            override val pos: BlockPos = entity.blockPos
        }
    }

    abstract class Filter {
        abstract fun allow(pos: BlockPos, world: World): Boolean

        abstract fun target(): Target
    }
}