package io.github.tagdl.RebarTranscEndence.recipe;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import io.github.pylonmc.pylon.PylonFluids;
import io.github.pylonmc.rebar.config.ConfigSection;
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter;
import io.github.pylonmc.rebar.guide.button.FluidButton;
import io.github.pylonmc.rebar.guide.button.ItemButton;
import io.github.pylonmc.rebar.i18n.RebarArgument;
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder;
import io.github.pylonmc.rebar.recipe.ConfigurableRecipeType;
import io.github.pylonmc.rebar.recipe.FluidOrItem;
import io.github.pylonmc.rebar.recipe.RebarRecipe;
import io.github.pylonmc.rebar.recipe.RecipeInput;
import io.github.pylonmc.rebar.recipe.RecipeType;
import io.github.pylonmc.rebar.util.gui.GuiItems;
import io.github.pylonmc.rebar.util.gui.unit.UnitFormat;
import io.github.tagdl.RebarTranscEndence.RebarTranscEndence;
import io.github.tagdl.RebarTranscEndence.RebarTranscEndenceItems;
import io.github.tagdl.RebarTranscEndence.blocks.ZotReverser;
import xyz.xenondevs.invui.gui.Gui;

public record ZotReverserRecipe(
        @NotNull NamespacedKey key,
        @NotNull RecipeInput.Item input,
        @NotNull ItemStack result
) implements RebarRecipe {
    public static final RecipeType<ZotReverserRecipe> RECIPE_TYPE = new ConfigurableRecipeType<>(new NamespacedKey(RebarTranscEndence.getInstance(), "zot_reverser")) {
        @Override
        protected @NotNull ZotReverserRecipe loadRecipe(@NotNull NamespacedKey key, @NotNull ConfigSection section) {
            return new ZotReverserRecipe(
                    key,
                    section.getOrThrow("input", ConfigAdapter.RECIPE_INPUT_ITEM),
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
        return List.of(input);
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
                        "# # # # # # # # #",
                        "# i l # h p o # #",
                        "# # # # # # # # #",
                        "# # # # # # # # #"
                )
                .addIngredient('#', GuiItems.backgroundBlack())
                .addIngredient('i', ItemButton.of(input))
                .addIngredient('h', new ItemButton(RebarTranscEndenceItems.ZOT_REVERSER))
                .addIngredient('o', ItemButton.of(result))
                .addIngredient('p', GuiItems.progressCyclingItem(ZotReverser.timeconsume,
                    ItemStackBuilder.of(Material.CLOCK)
                            .name(net.kyori.adventure.text.Component.translatable(
                                    "rebartranscendence.guide.recipe.zot_reverser",
                                    RebarArgument.of("time", UnitFormat.SECONDS.format(ZotReverser.timeconsume / 20.0))
                            ))
                ))
                .addIngredient('l', new FluidButton(
                    ZotReverser.fluidPerCraft * ZotReverser.timeconsume, PylonFluids.OBSCYRA))
                .build();
    }
}
