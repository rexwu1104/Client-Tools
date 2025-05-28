package clienttools.configs.world

import clienttools.utils.HashUtils
import net.minecraft.world.dimension.DimensionType

class ServerWorldConfig(
    seed256Hash: Long,
    dimension: DimensionType,
    address: String,
) : WorldConfig() {
    @OptIn(ExperimentalStdlibApi::class)
    override val hash: String =
        HashUtils.sha256Hash("S" + seed256Hash.toHexString() + "|" + dimension + "|" + address)

    override fun getType(): Type {
        return Type.Server
    }
}