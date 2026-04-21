package net.bunten.enderscape.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.bunten.enderscape.client.world.EndFlashParameters;
import net.minecraft.client.renderer.EndFlashState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndFlashState.class)
public abstract class EndFlashStateMixin {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void Enderscape$disableFlashTick(long l, CallbackInfo info) {
        if (!EndFlashParameters.enabled()) info.cancel();
    }

    @ModifyConstant(method = "calculateFlashParameters", constant = @Constant(longValue = 600L))
    private long Enderscape$changeFlashFrequency(long original) {
        return EndFlashParameters.frequencyInTicks();
    }

    @ModifyConstant(method = "calculateIntensity", constant = @Constant(longValue = 600L))
    private long Enderscape$changeVanillaCurveFrequency(long original) {
        return EndFlashParameters.frequencyInTicks();
    }

    @ModifyReturnValue(method = "getIntensity", at = @At("RETURN"))
    public float Enderscape$disableFlashIntensity(float original) {
        if (!EndFlashParameters.enabled()) return 0.0F;
        return original;
    }

    @ModifyReturnValue(method = "flashStartedThisTick", at = @At("RETURN"))
    public boolean Enderscape$disableFlashSound(boolean original) {
        if (!EndFlashParameters.enabled()) return false;
        return original;
    }
}
