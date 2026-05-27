package io.github.tagdl.RebarTranscEndence.blocks;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.github.pylonmc.pylon.PylonFluids;
import io.github.pylonmc.pylon.util.PylonUtils;
import io.github.pylonmc.rebar.block.RebarBlock;
import io.github.pylonmc.rebar.block.base.RebarDirectionalBlock;
import io.github.pylonmc.rebar.block.base.RebarFluidBufferBlock;
import io.github.pylonmc.rebar.block.base.RebarInventoryBlock;
import io.github.pylonmc.rebar.block.base.RebarLogisticBlock;
import io.github.pylonmc.rebar.block.base.RebarRecipeProcessor;
import io.github.pylonmc.rebar.block.base.RebarTickingBlock;
import io.github.pylonmc.rebar.block.base.RebarVirtualInventoryBlock;
import io.github.pylonmc.rebar.block.context.BlockBreakContext;
import io.github.pylonmc.rebar.block.context.BlockCreateContext;
import io.github.pylonmc.rebar.config.Config;
import io.github.pylonmc.rebar.config.Settings;
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter;
import io.github.pylonmc.rebar.fluid.FluidPointType;
import io.github.pylonmc.rebar.i18n.RebarArgument;
import io.github.pylonmc.rebar.item.RebarItem;
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder;
import io.github.pylonmc.rebar.logistics.LogisticGroupType;
import io.github.pylonmc.rebar.util.MachineUpdateReason;
import io.github.pylonmc.rebar.util.gui.GuiItems;
import io.github.pylonmc.rebar.util.gui.ProgressItem;
import io.github.pylonmc.rebar.util.gui.unit.UnitFormat;
import io.github.pylonmc.rebar.waila.WailaDisplay;
import io.github.tagdl.RebarTranscEndence.RebarTranscEndenceItems;
import io.github.tagdl.RebarTranscEndence.RebarTranscEndenceKeys;
import io.github.tagdl.RebarTranscEndence.recipe.QuirpCyclerRecipe;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.inventory.VirtualInventory;
import xyz.xenondevs.invui.inventory.event.ItemPostUpdateEvent;
import xyz.xenondevs.invui.inventory.event.ItemPreUpdateEvent;

public class QuirpCycler extends RebarBlock implements
        RebarDirectionalBlock,
        RebarTickingBlock,
        RebarFluidBufferBlock,
        RebarLogisticBlock,
        RebarRecipeProcessor<QuirpCyclerRecipe>,
        RebarVirtualInventoryBlock,
        RebarInventoryBlock
{
    private static final Config settings = Settings.get(RebarTranscEndenceKeys.QUIRP_CYCLER);
    public static final double buffer = settings.getOrThrow("buffer", ConfigAdapter.INTEGER);
    public static final double fluidPerCraft = settings.getOrThrow("fluid-per-craft", ConfigAdapter.INTEGER);
    public static final int timeconsume = Math.round(settings.getOrThrow("seconds-consume", ConfigAdapter.FLOAT) * 20);

    public final ItemStackBuilder zotStack = ItemStackBuilder.gui(Material.PURPLE_STAINED_GLASS_PANE, getKey() + ":zot")
            .name(Component.translatable("rebartranscendence.gui.quirp_cycler.zot"));
    private final VirtualInventory zotInventory = new VirtualInventory(1);
    private final VirtualInventory outputInventory = new VirtualInventory(1);
    public static class Item extends RebarItem {

        public final double fluidPerCraft = getSettings().getOrThrow("fluid-per-craft", ConfigAdapter.INTEGER);
        public final double buffer = getSettings().getOrThrow("buffer", ConfigAdapter.INTEGER);

        public Item(@NotNull ItemStack stack) {
            super(stack);
        }
        @Override
        public @NotNull List<RebarArgument> getPlaceholders() {
            return List.of(
                    RebarArgument.of("fluid-per-craft", UnitFormat.MILLIBUCKETS.format(fluidPerCraft)),
                    RebarArgument.of("buffer", UnitFormat.MILLIBUCKETS.format(buffer))
            );
        }
    }
    public QuirpCycler(@NotNull Block block, @NotNull BlockCreateContext context) {
        super(block, context);
        setTickInterval(20);
        this.setFacing(context.getFacing());
        createFluidPoint(FluidPointType.INPUT, BlockFace.NORTH, context, false);
        createFluidBuffer(PylonFluids.OBSCYRA, buffer, true, false);
        setRecipeType(QuirpCyclerRecipe.RECIPE_TYPE);
        setRecipeProgressItem(new ProgressItem(GuiItems.background()));
    }
    public QuirpCycler(@NotNull Block block, @NotNull PersistentDataContainer pdc) {
        super(block, pdc);
    }
    @Override
    public void postInitialise() {
        zotInventory.addPreUpdateHandler(event -> onZotPreUpdate(event));
        zotInventory.addPostUpdateHandler(event -> onZotUpdate(event));
        outputInventory.addPreUpdateHandler(event -> onOutputUpdate(event));
        outputInventory.addPostUpdateHandler(event -> tryStartRecipe());
        createLogisticGroup("zot", LogisticGroupType.INPUT, zotInventory);
        createLogisticGroup("output", LogisticGroupType.OUTPUT, outputInventory);
    }
    public void onZotPreUpdate(ItemPreUpdateEvent event){
        if (event.isAdd()) event.setCancelled(event.getNewItem().isSimilar(reverseZot(event.getNewItem())));
    }
    public void onZotUpdate(ItemPostUpdateEvent event){
        if (!(event.getUpdateReason() instanceof MachineUpdateReason)) {
            tryStartRecipe();
        }
    }
    public void onOutputUpdate(ItemPreUpdateEvent event){
        if (event.isAdd()) event.setCancelled(!(event.getUpdateReason() instanceof MachineUpdateReason));
    }
    @Override
    public @NotNull Gui createGui() {
        return Gui.builder()
                .setStructure(
                        "A A A # # # B B B",
                        "A I A # P # B O B",
                        "A A A # # # B B B"
                )
                .addIngredient('#', GuiItems.background())
                .addIngredient('A', zotStack)
                .addIngredient('B', GuiItems.output())
                .addIngredient('P', getRecipeProgressItem())
                .addIngredient('I', zotInventory)
                .addIngredient('O', outputInventory)
                .build();
    }
    @Override
    public void tick() {
        if (getBlock() == null || !getBlock().getChunk().isLoaded()) return;
        if (outputInventory == null) return;
        if (fluidAmount(PylonFluids.OBSCYRA) < fluidPerCraft) return;
        if (isProcessingRecipe()) {
            removeFluid(PylonFluids.OBSCYRA, fluidPerCraft);
            progressRecipe(getTickInterval());
            return;
        }     
    }
    @Override
    public void onRecipeFinished(@NotNull QuirpCyclerRecipe recipe) {
        getRecipeProgressItem().setItem(GuiItems.background());
        outputInventory.addItem(new MachineUpdateReason(), recipe.result());
    }
    public void tryStartRecipe() {
        if (isProcessingRecipe()) return;
        if (zotInventory.isEmpty()) return;
        ItemStack itemStack = zotInventory.getItem(0).clone();
        if (itemStack == null) return;
        if (getLastRecipe() != null && tryStartRecipe(getLastRecipe(), itemStack)) return;

        for (QuirpCyclerRecipe recipe : QuirpCyclerRecipe.RECIPE_TYPE) {
            if (tryStartRecipe(recipe, itemStack)) return;
        }
    }
    public boolean tryStartRecipe(@NotNull QuirpCyclerRecipe recipe, ItemStack itemStack) {
        if (!recipe.input().matches(itemStack)) return false; //continue loop
        if (!outputInventory.canHold(recipe.result())) return false; //stop loop

        startRecipe(recipe, timeconsume);
        getRecipeProgressItem().setItem(ItemStackBuilder.of(itemStack.asOne()).clearLore());
        zotInventory.setItem(new MachineUpdateReason(), 0, itemStack.subtract(recipe.input().getAmount()));
        return true;
    }
    private ItemStack reverseZot(ItemStack itemStack) {
        ItemStack resulItemStack = itemStack;
        if (itemStack.isSimilar(RebarTranscEndenceItems.QUIRP_DOWN.clone())) resulItemStack = RebarTranscEndenceItems.QUIRP_UP.clone();
        if (itemStack.isSimilar(RebarTranscEndenceItems.QUIRP_UP.clone())) resulItemStack = RebarTranscEndenceItems.QUIRP_DOWN.clone();
        if (itemStack.isSimilar(RebarTranscEndenceItems.QUIRP_RIGHT.clone())) resulItemStack = RebarTranscEndenceItems.QUIRP_LEFT.clone();
        if (itemStack.isSimilar(RebarTranscEndenceItems.QUIRP_LEFT.clone())) resulItemStack = RebarTranscEndenceItems.QUIRP_RIGHT.clone();
        return resulItemStack;
    }
    @Override
    public void postLoad() {
        if (isProcessingRecipe()) {
            finishRecipe();
        }
    }
    @Override
    public @NotNull Map<@NotNull String, @NotNull VirtualInventory> getVirtualInventories() {
        return Map.of("zot", zotInventory, "output", outputInventory);
    }
    @Override
    public void onBreak(@NotNull List<@NotNull ItemStack> drops, @NotNull BlockBreakContext context) {
        RebarVirtualInventoryBlock.super.onBreak(drops, context);
        RebarFluidBufferBlock.super.onBreak(drops, context);
    }
    @Override
    public @Nullable WailaDisplay getWaila(@NotNull Player player) {
        return new WailaDisplay(isProcessingRecipe()
            ? Component.translatable("rebartranscendence.item.quirp_cycler.waila.running")
                .arguments(
                    RebarArgument.of("result", getCurrentRecipe().result().effectiveName()),
                    RebarArgument.of("process", UnitFormat.SECONDS.format(getRecipeTicksRemaining() / 20)),
                    RebarArgument.of("input-bar", PylonUtils.createFluidAmountBar(
                        fluidAmount(PylonFluids.OBSCYRA),
                        fluidCapacity(PylonFluids.OBSCYRA),
                        20,
                        TextColor.fromHexString("#000000")
                )))
            : Component.translatable("rebartranscendence.item.quirp_cycler.waila.not_running")
                .arguments(RebarArgument.of("input-bar", PylonUtils.createFluidAmountBar(
                        fluidAmount(PylonFluids.OBSCYRA),
                        fluidCapacity(PylonFluids.OBSCYRA),
                        20,
                        TextColor.fromHexString("#000000")
                )))
        );
    }
}
