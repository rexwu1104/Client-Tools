package clienttools.tools.inventory

import clienttools.tools.inventory.Inventories.PositionProcessor
import clienttools.utils.block.BlockSurfaceScanner
import clienttools.utils.block.ChestMultiBlockStructureDetector
import clienttools.utils.block.InventoryMultiBlockStructureDetector
import clienttools.utils.block.filters.ItemFrameFilter
import clienttools.utils.block.filters.WallSignBlockFilter
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.entity.SignBlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.decoration.ItemFrameEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

class Inventories(inventoryPositions: List<BlockPos>) {
    private val inventories = IdentityHashMap<Metadata, Inventory>()

    init {
        val client = MinecraftClient.getInstance()
        val world = client.world!!
        inventoryPositions.forEach { blockPos ->
            val blockState = world.getBlockState(blockPos)
            val blockEntity = world.getBlockEntity(blockPos)!!
            val inventory = processors[blockState.block]?.toInventory(blockPos, world) ?: blockEntity as Inventory
            val metadata = getMetadata(blockPos, world)
            inventories[metadata] = inventory
        }
    }

    private fun getMetadata(pos: BlockPos, world: World): Metadata {
        return BlockSurfaceScanner.scan(pos, world, Metadata.Collector)
    }

    override fun toString(): String {
        return "[${inventories.entries.joinToString { "${it.key}: ${it.value}" }}]"
    }

    companion object {
        private val processors = IdentityHashMap<Block, PositionProcessor>()

        init {
            BlockSurfaceScanner.addDetector(ChestMultiBlockStructureDetector)
            BlockSurfaceScanner.addDetector(InventoryMultiBlockStructureDetector)
            addProcessor(Blocks.ENDER_CHEST) { _, _ ->
                MinecraftClient.getInstance().player!!.enderChestInventory
            }
        }

        inline fun addProcessor(target: Block, crossinline block: (BlockPos, World) -> Inventory) {
            addProcessor(target,
                PositionProcessor { pos, world -> block(pos, world) })
        }

        fun addProcessor(target: Block, processor: PositionProcessor) {
            processors[target] = processor
        }
    }

    @JvmRecord
    data class Metadata(private val icon: ItemStack, private val tooltip: String/*, private val pos: BlockPos*/) {
        object Collector : BlockSurfaceScanner.ResultCollector<Metadata>() {
            override val filters = arrayListOf(
                ItemFrameFilter,
                WallSignBlockFilter
            )

            override fun collect(): Metadata {
                val iconTarget = targets.find { it is BlockSurfaceScanner.Target.Entity } as? BlockSurfaceScanner.Target.Entity
                val icon = (iconTarget?.entity as? ItemFrameEntity)?.heldItemStack ?: ItemStack.EMPTY
                val signTargets = targets.filterIsInstance<BlockSurfaceScanner.Target.BlockEntity>()
                val tooltip = signTargets
                    .map { it.blockEntity as SignBlockEntity }
                    .flatMap { sign -> sign.frontText.getMessages(true).map { it.string } }
                    .joinToString("\n")
                    .trim()
                return Metadata(icon, tooltip).apply {
                    targets.clear()
                }
            }
        }
    }

    fun interface PositionProcessor {
        fun toInventory(pos: BlockPos, world: World): Inventory
    }
}