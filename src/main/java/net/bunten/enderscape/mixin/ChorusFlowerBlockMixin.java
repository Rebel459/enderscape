package net.bunten.enderscape.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChorusFlowerBlock.class)
public abstract class ChorusFlowerBlockMixin extends BlockBehaviour {
    public ChorusFlowerBlockMixin(Properties settings) {
        super(settings);
    }

    @WrapOperation(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z", ordinal = 0))
    private boolean Enderscape$randomTick1(BlockState instance, TagKey tagKey, Operation<Boolean> original) {
        return original.call(instance, tagKey) || original.call(instance, EnderscapeBlockTags.CHORUS_VEGETATION_PLANTABLE_ON);
    }

    @WrapOperation(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z", ordinal = 1))
    private boolean Enderscape$randomTick2(BlockState instance, TagKey tagKey, Operation<Boolean> original) {
        return original.call(instance, tagKey) || original.call(instance, EnderscapeBlockTags.CHORUS_VEGETATION_PLANTABLE_ON);
    }

    @WrapOperation(method = "canSurvive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z", ordinal = 0))
    private boolean Enderscape$canSurvive(BlockState instance, TagKey tagKey, Operation<Boolean> original) {
        return original.call(instance, tagKey) || original.call(instance, EnderscapeBlockTags.CHORUS_VEGETATION_PLANTABLE_ON);
    }
}