package net.bunten.enderscape.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(targets = "net.minecraft.world.entity.monster.Shulker$ShulkerAttackGoal")
public abstract class ShulkerAttackGoalMixin extends Goal {

    @Shadow(aliases = "this$0")
    @Final
    private Shulker shulker;

    @Shadow
    private int attackTime;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo ci) {
        if (EnderscapeConfig.getInstance().shulkerBulletEnforceCountLimit > 0 && this.attackTime % 20 == 0) {
            List<Entity> entities = shulker.level().getEntities(shulker, shulker.getBoundingBox().inflate(50), entity -> entity instanceof ShulkerBullet bullet && bullet.getOwner() == shulker);
            if (entities.size() >= EnderscapeConfig.getInstance().shulkerBulletEnforceCountLimit) ci.cancel();
        }
    }
}