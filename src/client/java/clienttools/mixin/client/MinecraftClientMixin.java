package clienttools.mixin.client;

import clienttools.utils.storage.Constants;
import clienttools.utils.storage.GlobalStorage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "setScreen", at = @At("RETURN"))
    void injectSetScreen(Screen screen, CallbackInfo ci) {
        GlobalStorage.INSTANCE.set(Constants.STORAGE_INGAME_SCREEN, screen instanceof HandledScreen<?>);
    }
}
