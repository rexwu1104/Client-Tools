package clienttools.mixin.client;

import clienttools.bindings.KeyManager;
import clienttools.utils.storage.Constants;
import clienttools.utils.storage.GlobalStorage;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {
    @Inject(method = "keyPressed", at = @At("HEAD"))
    void injectKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        var key = KeyManager.INSTANCE.getBIND_OPENING_INVENTORY_KEY();
        if (key.matches(keyCode, scanCode))
            key.press();
    }
}
