package io.github.tagdl.RebarTranscEndence.blocks;

import static io.github.pylonmc.pylon.util.PylonUtils.colorToTextColor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import io.github.pylonmc.rebar.block.RebarBlock;
import io.github.pylonmc.rebar.block.base.RebarDirectionalBlock;
import io.github.pylonmc.rebar.block.base.RebarInventoryBlock;
import io.github.pylonmc.rebar.block.base.RebarLogisticBlock;
import io.github.pylonmc.rebar.block.base.RebarRecipeProcessor;
import io.github.pylonmc.rebar.block.base.RebarSimpleMultiblock;
import io.github.pylonmc.rebar.block.base.RebarVirtualInventoryBlock;
import io.github.pylonmc.rebar.block.context.BlockBreakContext;
import io.github.pylonmc.rebar.block.context.BlockCreateContext;
import io.github.pylonmc.rebar.config.Config;
import io.github.pylonmc.rebar.config.Settings;
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter;
import io.github.pylonmc.rebar.i18n.RebarArgument;
import io.github.pylonmc.rebar.logistics.LogisticGroupType;
import io.github.pylonmc.rebar.recipe.RecipeInput;
import io.github.pylonmc.rebar.util.MachineUpdateReason;
import io.github.pylonmc.rebar.util.gui.GuiItems;
import io.github.pylonmc.rebar.waila.WailaDisplay;
import io.github.tagdl.RebarTranscEndence.RebarTranscEndence;
import io.github.tagdl.RebarTranscEndence.RebarTranscEndenceKeys;
import io.github.tagdl.RebarTranscEndence.recipe.NanobotCrafterRecipe;
import net.kyori.adventure.text.Component;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.inventory.VirtualInventory;

public class NanobotCrafter extends RebarBlock implements
        RebarDirectionalBlock,
        RebarSimpleMultiblock,
        RebarVirtualInventoryBlock,
        RebarLogisticBlock,
        RebarRecipeProcessor<NanobotCrafterRecipe>,
        RebarInventoryBlock
{
    private static final Config settings = Settings.get(RebarTranscEndenceKeys.NANOBOT_CRAFTER);
    public static final int timeconsume = Math.round(settings.getOrThrow("seconds-consume", ConfigAdapter.FLOAT) * 20);
    private final VirtualInventory inputInventory = new VirtualInventory(9);
    public NanobotCrafter(@NotNull Block block, @NotNull BlockCreateContext context) {
        super(block, context);
        this.setFacing(context.getFacing());
        this.setMultiblockDirection(context.getFacing());
        setRecipeType(NanobotCrafterRecipe.RECIPE_TYPE);
    }
    public NanobotCrafter(@NotNull Block block, @NotNull PersistentDataContainer pdc) {
        super(block, pdc);
    }
    @Override
    public void postInitialise() {
        inputInventory.addPreUpdateHandler(event -> {
            if (!(event.getUpdateReason() instanceof MachineUpdateReason)) {
                if (event.isRemove() || event.isSwap()) {
                    event.setCancelled(isProcessingRecipe());
                }
            }
        });
        createLogisticGroup("input", LogisticGroupType.INPUT, inputInventory);
    }
    @Override
    public @NotNull Map<Vector3i, MultiblockComponent> getComponents() {
        Map<Vector3i, MultiblockComponent> map = new HashMap<>();
        map.put(new Vector3i(2, 0, 0), MultiblockComponent.of(RebarTranscEndenceKeys.NANOBOT_OUTPUT_HATCH));
        map.put(new Vector3i(1, 0, 0), MultiblockComponent.of(RebarTranscEndenceKeys.NANOBOT_LAUNCHER));
        map.put(new Vector3i(0, 1, 0), MultiblockComponent.of(Material.END_ROD));
        return map;
    }
    @Override
    public @NotNull Gui createGui() {
        return Gui.builder()
                .setStructure(
                        "# # # A A A # # #",
                        "# # # A A A # # #",
                        "# # # A A A # # #"
                )
                .addIngredient('#', GuiItems.background())
                .addIngredient('A', inputInventory)
                .build();
    }
    public void tryStartRecipe(@NotNull NanobotCrafterRecipe recipe, @NotNull Player player, @NotNull Block block) {
        startRecipe(recipe, timeconsume);
        for (int i = 0; i < recipe.inputs().size(); i++) {
            if (recipe.inputs().get(i) == null) continue;
            inputInventory.setItem(new MachineUpdateReason(), i,
                inputInventory.getItem(i).asQuantity(inputInventory.getItemAmount(i) - recipe.inputs().get(i).getAmount()));
        }
        for (int j = 0; j < 4; j++) {
            int current = j;
            Bukkit.getScheduler().runTaskLater(RebarTranscEndence.getInstance(), () -> {
                if (block.getChunk().isLoaded()) {
                    player.getWorld().playEffect(block.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
                    player.getWorld().playEffect(block.getLocation(), Effect.ENDER_SIGNAL, 1);
                    if (current < 3) {
                        player.getWorld().playSound(block.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
                    } else {
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
                        finishRecipe();
                    }
                }
            }, j * Math.round(timeconsume / 3));
        }
    }
    public NanobotCrafterRecipe getRecipe(){
        if (isProcessingRecipe()) return null;
        if (inputInventory.isEmpty()) return null;
        NanobotCrafterRecipe result = null;
        for (NanobotCrafterRecipe recipe : NanobotCrafterRecipe.RECIPE_TYPE) {
            boolean find = true;
            for (int i = 0; i < recipe.inputs().size(); i++) {
                RecipeInput.Item input = recipe.inputs().get(i);
                if (input.matches(inputInventory.getItem(i).asOne())) {
                    find = true;
                    continue;
                }
                find = false;
                break;
            }
            if (find) {
                result = recipe;
                break;
            }
        }
        if (result != null) {
            if (!getMultiblockComponentOrThrow(NanobotOutpuHatch.class,
                        new Vector3i(2, 0, 0)).inventory.canHold(result.result())) {
                result = null;
            }
        }
        return result;
    }
    @Override
    public void onRecipeFinished(@NotNull NanobotCrafterRecipe recipe) {
        getMultiblockComponentOrThrow(NanobotOutpuHatch.class, new Vector3i(2, 0, 0))
                .inventory
                .addItem(new MachineUpdateReason(), recipe.result());
    }
    @Override
    public @NotNull Map<@NotNull String, @NotNull VirtualInventory> getVirtualInventories() {
        return Map.of("input", inputInventory);
    }
    @Override
    public void onBreak(@NotNull List<@NotNull ItemStack> drops, @NotNull BlockBreakContext context) {
        RebarVirtualInventoryBlock.super.onBreak(drops, context);
    }
    @Override
    public void postLoad() {
        if (isProcessingRecipe()) {
            finishRecipe();
        }
    }
    @Override
    public @Nullable WailaDisplay getWaila(@NotNull Player player) {
        return new WailaDisplay(isProcessingRecipe() 
            ? Component.translatable("rebartranscendence.item.nanobot_crafter.waila.running")
                .arguments(
                    RebarArgument.of("result", getCurrentRecipe().result().effectiveName().color(colorToTextColor(Color.LIME))))
            : Component.translatable("rebartranscendence.item.nanobot_crafter.waila.not_running"));
    }
}
