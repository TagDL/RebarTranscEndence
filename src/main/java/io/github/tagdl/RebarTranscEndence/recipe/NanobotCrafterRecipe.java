package io.github.tagdl.RebarTranscEndence.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import io.github.pylonmc.rebar.config.ConfigSection;
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter;
import io.github.pylonmc.rebar.guide.button.ItemButton;
import io.github.pylonmc.rebar.recipe.ConfigurableRecipeType;
import io.github.pylonmc.rebar.recipe.FluidOrItem;
import io.github.pylonmc.rebar.recipe.RebarRecipe;
import io.github.pylonmc.rebar.recipe.RecipeInput;
import io.github.pylonmc.rebar.recipe.RecipeType;
import io.github.pylonmc.rebar.util.gui.GuiItems;
import io.github.tagdl.RebarTranscEndence.RebarTranscEndence;
import io.github.tagdl.RebarTranscEndence.RebarTranscEndenceItems;
import xyz.xenondevs.invui.gui.Gui;

public record NanobotCrafterRecipe(
        @NotNull NamespacedKey key,
        @NotNull List<RecipeInput.@Nullable Item> inputs,
        @NotNull ItemStack result
) implements RebarRecipe {

    public static final RecipeType<NanobotCrafterRecipe> RECIPE_TYPE = new ConfigurableRecipeType<>(new NamespacedKey(RebarTranscEndence.getInstance(), "nanobot_crafter")) {
        @Override
        protected @NotNull NanobotCrafterRecipe loadRecipe(@NotNull NamespacedKey key, @NotNull ConfigSection section) {
            List<String> shape = section.getOrThrow("shape", ConfigAdapter.LIST.from(ConfigAdapter.STRING));
            if (shape.size() != 3) {
                throw new IllegalArgumentException("Invalid shape size, must be 3");
            }
            for (String row : shape) {
                if (row.length() != 3) {
                    throw new IllegalArgumentException("Invalid shape row length, must be 3");
                }
            }
            Map<Character, RecipeInput.Item> itemMap = section.getOrThrow("key", ConfigAdapter.MAP.from(
                    ConfigAdapter.CHAR,
                    ConfigAdapter.RECIPE_INPUT_ITEM
            ));

            StringBuilder ingredientChars = new StringBuilder();
            ingredientChars.append(shape.get(0));
            ingredientChars.append(shape.get(1));
            ingredientChars.append(shape.get(2));
            List<RecipeInput.Item> inputs = new ArrayList<>(9);
            for (int i = 0; i < ingredientChars.length(); i++) {
                char c = ingredientChars.charAt(i);
                if (c == ' ') {
                    inputs.add(null);
                } else if (itemMap.containsKey(c)) {
                    inputs.add(itemMap.get(c));
                } else {
                    throw new IllegalArgumentException("Unknown character in shape: " + c);
                }
            }

            return new NanobotCrafterRecipe(
                    key,
                    inputs,
                    section.getOrThrow("result", ConfigAdapter.ITEM_STACK)
            );
        }
    };
    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }
    @Override
    public @NotNull List<RecipeInput> getInputs() {
        List<RecipeInput> inputResult = new ArrayList<>();
        for (RecipeInput.Item input : inputs) {
            if (input != null) {
                inputResult.add(input);
            }
        }
        return inputResult;
    }
    @Override
    public @NotNull List<FluidOrItem> getResults() {
        return List.of(FluidOrItem.of(result));
    }
    @Override
    public @NotNull Gui display() {
        return Gui.builder()
                .setStructure(
                        "# # # # # # # # #",
                        "# 0 1 2 # # # # #",
                        "# 3 4 5 # h # o #",
                        "# 6 7 8 # # # # #",
                        "# # # # # # # # #"
                )
                .addIngredient('#', GuiItems.backgroundBlack())
                .addIngredient('0', ItemButton.of(inputs.get(0)))
                .addIngredient('1', ItemButton.of(inputs.get(1)))
                .addIngredient('2', ItemButton.of(inputs.get(2)))
                .addIngredient('3', ItemButton.of(inputs.get(3)))
                .addIngredient('4', ItemButton.of(inputs.get(4)))
                .addIngredient('5', ItemButton.of(inputs.get(5)))
                .addIngredient('6', ItemButton.of(inputs.get(6)))
                .addIngredient('7', ItemButton.of(inputs.get(7)))
                .addIngredient('8', ItemButton.of(inputs.get(8)))
                .addIngredient('h', ItemButton.of(RebarTranscEndenceItems.NANOBOT_CRAFTER))
                .addIngredient('o', ItemButton.of(result))
                .build();
    }
}
