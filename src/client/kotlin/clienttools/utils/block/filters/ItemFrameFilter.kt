package clienttools.utils.block.filters

import clienttools.utils.block.BlockSurfaceScanner
import net.minecraft.entity.decoration.ItemFrameEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.world.World

object ItemFrameFilter : BlockSurfaceScanner.Filter() {
    private var itemFrame: ItemFrameEntity? = null

    override fun allow(pos: BlockPos, world: World): Boolean {
        val itemFrames = world.getEntitiesByClass(ItemFrameEntity::class.java, Box(pos)) { !it.heldItemStack.isEmpty }
        if (itemFrames.isEmpty())
            return false

        itemFrame = itemFrames[0]
        return true
    }

    override fun target(): BlockSurfaceScanner.Target {
        return BlockSurfaceScanner.Target.Entity(itemFrame!!)
    }
}