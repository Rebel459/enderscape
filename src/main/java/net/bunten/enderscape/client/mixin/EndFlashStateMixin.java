package net.bunten.enderscape.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.bunten.enderscape.client.world.EndFlashParameters;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EndFlashState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndFlashState.class)
public abstract class EndFlashStateMixin {

    @Shadow
    private int offset;

    @Shadow
    private int duration;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void Enderscape$disableFlashTick(long clockTime, CallbackInfo info) {
        if (!EndFlashParameters.enabled()) {
            info.cancel();
            return;
        }

        if (clockTime == 0L) {
            Minecraft client = Minecraft.getInstance();
            if (client.level != null) {
                long realTime = client.level.getGameTime();
                ((EndFlashState)(Object)this).tick(realTime);
                info.cancel();
            }
        }
    }

    @ModifyExpressionValue(
            method = "calculateFlashParameters",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Mth;randomBetweenInclusive(Lnet/minecraft/util/RandomSource;II)I",
                    ordinal = 1
            )
    )
    private int changeDuration(int original) {
        if (EndFlashParameters.updatedVisuals()) return original * 3;
        return original;
    }

    @Inject(method = "calculateIntensity", at = @At("HEAD"), cancellable = true)
    public void Enderscape$changeIntensityCurve(long clockTime, CallbackInfoReturnable<Float> info) {
        if (clockTime == 0L) {
            info.setReturnValue(0.0F);
            return;
        }
        if (EndFlashParameters.updatedVisuals()) {
            info.setReturnValue(EndFlashParameters.getIntensity(offset, duration, clockTime % EndFlashParameters.frequencyInTicks()));
        }
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
