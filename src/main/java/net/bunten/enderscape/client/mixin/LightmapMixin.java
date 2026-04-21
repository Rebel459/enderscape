package net.bunten.enderscape.client.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.client.renderer.EnderscapeRenderPipelines;
import net.bunten.enderscape.client.renderer.LightingStyle;
import net.bunten.enderscape.registry.EnderscapeEnvironmentAttributes;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.Lightmap;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Lightmap.class)
public class LightmapMixin {

    @Unique
    private final EnderscapeConfig Enderscape$config = EnderscapeConfig.getInstance();

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/buffers/Std140Builder;putVec3(Lorg/joml/Vector3fc;)Lcom/mojang/blaze3d/buffers/Std140Builder;", ordinal = 2))
    private Vector3fc Enderscape$changeAmbientLightColor(Vector3fc value) {
        Vector3f base = new Vector3f(value);
        Minecraft minecraft = Minecraft.getInstance();

        ClientLevel level = minecraft.level;
        if (level != null && level.dimension().equals(Level.END) && Enderscape$config.lightingStyle != LightingStyle.VANILLA) {
            switch (Enderscape$config.lightingStyle) {
                case IMPROVED -> base = new Vector3f(0.92F, 1.2F, 0.92F);
                case MIDNIGHT -> base = new Vector3f(0.98F, 1.2F, 0.98F);
            }
        }

        float gamma = minecraft.options.gamma().get().floatValue();
        float ambientLightFactor = minecraft.gameRenderer.getMainCamera().attributeProbe().getValue(EnderscapeEnvironmentAttributes.AMBIENT_LIGHT_FACTOR, 1.0F);
        float scaledAmbientLight = ambientLightFactor * (1.0F - 1.0F * gamma * (1.0F - ambientLightFactor));

        return base.mul(scaledAmbientLight);
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setPipeline(Lcom/mojang/blaze3d/pipeline/RenderPipeline;)V"))
    private RenderPipeline Enderscape$changeEndLightmap(RenderPipeline pipeline) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && level.dimension().equals(Level.END) && Enderscape$config.lightingStyle != LightingStyle.VANILLA) {
            return switch (Enderscape$config.lightingStyle) {
                case IMPROVED -> EnderscapeRenderPipelines.IMPROVED_LIGHTMAP;
                case MIDNIGHT -> EnderscapeRenderPipelines.MIDNIGHT_LIGHTMAP;
                case VANILLA -> pipeline;
            };
        }
        return pipeline;
    }
}
