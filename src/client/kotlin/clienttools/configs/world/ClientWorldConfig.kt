package clienttools.configs.world

import clienttools.utils.HashUtils
import net.minecraft.world.dimension.DimensionType

class ClientWorldConfig(
    seed256Hash: Long,
    dimension: DimensionType,
    worldPath: String,
) : WorldConfig() {
    @OptIn(ExperimentalStdlibApi::class)
    override val hash: String =
        HashUtils.sha256Hash("C" + seed256Hash.toHexString() + "|" + dimension + "|" + worldPath)

    override fun getType(): Type {
        return Type.Client
    }
}