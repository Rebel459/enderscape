package net.bunten.enderscape.registry;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.item.crafting.ToolFuelingRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class EnderscapeRecipeSerializers {

    public static final RecipeSerializer<ToolFuelingRecipe> TOOL_FUELING = register("tool_fueling", new RecipeSerializer<>(MapCodec.unit(new ToolFuelingRecipe()), StreamCodec.unit(new ToolFuelingRecipe())));

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String string, S serializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Enderscape.id(string), serializer);
    }
}