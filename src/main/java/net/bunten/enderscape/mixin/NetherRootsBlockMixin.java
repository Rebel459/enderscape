package net.bunten.enderscape.mixin;

import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.minecraft.world.level.block.NetherRootsBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(NetherRootsBlock.class)
public abstract class NetherRootsBlockMixin extends BlockBehaviour {
    public NetherRootsBlockMixin(Properties settings) {
        super(settings);
    }

    @Inject(at = @At("HEAD"), method = "mayPlaceOn", cancellable = true)
    protected void mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos, CallbackInfoReturnable<Boolean> info) {
        if (floor.is(EnderscapeBlockTags.OVERGROWTH_BLOCKS)) {
            info.setReturnValue(true);
        }
    }
}