package clienttools.mixin.client;

import clienttools.utils.Constants;
import clienttools.utils.GlobalStorage;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.Set;

@Mixin(GameJoinS2CPacket.class)
public class GameJoinS2CPacketMixin {
    @Inject(method = "<init>(IZLnet/minecraft/world/GameMode;Lnet/minecraft/world/GameMode;Ljava/util/Set;Lnet/minecraft/registry/DynamicRegistryManager$Immutable;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/registry/RegistryKey;JIIIZZZZLjava/util/Optional;I)V", at = @At("RETURN"))
    void injectInit(
        int playerEntityId,
        boolean bl,
        GameMode previousGameMode,
        GameMode gameMode,
        Set set,
        DynamicRegistryManager.Immutable immutable,
        RegistryKey registryKey,
        RegistryKey registryKey2,
        long sha256seed,
        int maxPlayers,
        int chunkLoadDistance,
        int i,
        boolean bl2,
        boolean bl3,
        boolean bl4,
        boolean bl5,
        Optional optional,
        int j,
        CallbackInfo ci
    ) {
        GlobalStorage.INSTANCE.set(Constants.STORAGE_SEED_KEY, sha256seed);
    }
}
