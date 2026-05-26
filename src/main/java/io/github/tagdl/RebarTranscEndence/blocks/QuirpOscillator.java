package io.github.tagdl.RebarTranscEndence.blocks;

import io.github.pylonmc.pylon.PylonFluids;
import io.github.pylonmc.pylon.util.PylonUtils;
import io.github.pylonmc.rebar.block.RebarBlock;
import io.github.pylonmc.rebar.block.base.RebarDirectionalBlock;
import io.github.pylonmc.rebar.block.base.RebarFluidBufferBlock;
import io.github.pylonmc.rebar.block.base.RebarGuiBlock;
import io.github.pylonmc.rebar.block.base.RebarLogisticBlock;
import io.github.pylonmc.rebar.block.base.RebarProcessor;
import io.github.pylonmc.rebar.block.base.RebarTickingBlock;
import io.github.pylonmc.rebar.block.base.RebarVirtualInventoryBlock;
import io.github.pylonmc.rebar.block.context.BlockBreakContext;
import io.github.pylonmc.rebar.block.context.BlockCreateContext;
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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.inventory.VirtualInventory;
import xyz.xenondevs.invui.inventory.event.ItemPostUpdateEvent;
import xyz.xenondevs.invui.inventory.event.ItemPreUpdateEvent;

import static io.github.pylonmc.pylon.util.PylonUtils.colorToTextColor;

import java.util.List;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class QuirpOscillator extends RebarBlock implements 
        RebarDirectionalBlock,
        RebarProcessor,
        RebarLogisticBlock,
        RebarVirtualInventoryBlock,
        RebarGuiBlock,
        RebarFluidBufferBlock,
        RebarTickingBlock
{

    public final double buffer = getSettings().getOrThrow("buffer", ConfigAdapter.INTEGER);
    public final double fluidPerCraft = getSettings().getOrThrow("fluid-per-craft", ConfigAdapter.INTEGER);
    public final int secondsconsume = Math.round(getSettings().getOrThrow("seconds-consume", ConfigAdapter.FLOAT) * 20);    

    public final ItemStackBuilder polarizerStack = ItemStackBuilder.gui(Material.PURPLE_STAINED_GLASS_PANE, getKey() + ":polarizer")
            .name(Component.translatable("rebartranscendence.gui.quirp_oscillator.polarizer"));
    private final ItemStackBuilder progressItemStackBuilder = ItemStackBuilder.of(Material.CLOCK)
            .name(Component.translatable("rebartranscendence.gui.quirp_oscillator.progress"));
    private final ProgressItem progressItem = new ProgressItem(progressItemStackBuilder, false);
    private final VirtualInventory polarizerInventory = new VirtualInventory(1);
    private final VirtualInventory outputInventory = new VirtualInventory(3);
    private double[] weight = new double[]{0.25, 0.25, 0.25, 0.25}; //up down left right
    private ItemStack itemStack = ItemStack.empty();
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
    public QuirpOscillator(@NotNull Block block, @NotNull BlockCreateContext context) {
        super(block, context);
        setTickInterval(20);
        this.setFacing(context.getFacing());
        createFluidPoint(FluidPointType.INPUT, BlockFace.NORTH, context, false);
        createFluidBuffer(PylonFluids.OBSCYRA, buffer, true, false);
    }
    public QuirpOscillator(@NotNull Block block, @NotNull PersistentDataContainer pdc) {
        super(block, pdc);
    }
    @Override
    public void postInitialise() {
        setProcessProgressItem(progressItem);
        polarizerInventory.addPreUpdateHandler(event -> onPolarizerPreUpdate(event));
        polarizerInventory.addPostUpdateHandler(event -> onPolarizerUpdate(event));
        outputInventory.addPreUpdateHandler(event -> onOutputUpdate(event));
        createLogisticGroup("polarizer", LogisticGroupType.INPUT, polarizerInventory);
        createLogisticGroup("output", LogisticGroupType.OUTPUT, outputInventory);
    }
    private void onPolarizerPreUpdate(ItemPreUpdateEvent event) {
        if (event.isAdd()) {
            boolean tempbool = event.getNewItem().isSimilar(RebarTranscEndenceItems.VERTICAL_POLARIZER.clone())
                || event.getNewItem().isSimilar(RebarTranscEndenceItems.HORIZONTAL_POLARIZER.clone());
            event.setCancelled(!tempbool);
        }
    }
    private void onPolarizerUpdate(ItemPostUpdateEvent event) {
        weight = event.getNewItem() == null 
            ? new double[]{0.25, 0.25, 0.25, 0.25}
            : event.getNewItem().isSimilar(RebarTranscEndenceItems.VERTICAL_POLARIZER.clone())
                ? new double[]{0.4, 0.4, 0.1, 0.1}
                : new double[]{0.1, 0.1, 0.4, 0.4};
    }
    private void onOutputUpdate(ItemPreUpdateEvent event) {
        if (event.isAdd()) event.setCancelled(!(event.getUpdateReason() instanceof MachineUpdateReason));
    }
    @Override
    public @NotNull Gui createGui() {
        return Gui.builder()
                .setStructure(
                        "A A A # B B B B B",
                        "A I A P B O O O B",
                        "A A A # B B B B B"
                )
                .addIngredient('#', GuiItems.background())
                .addIngredient('A', polarizerStack)
                .addIngredient('B', GuiItems.output())
                .addIngredient('P', progressItem)
                .addIngredient('I', polarizerInventory)
                .addIngredient('O', outputInventory)
                .build();
    }
    @Override
    public void tick(){
        if (getBlock() == null || !getBlock().getChunk().isLoaded()) return;
        if (getBlock().getWorld().getEnvironment() != World.Environment.THE_END) return;
        if (outputInventory == null) return;
        if (!outputInventory.hasEmptySlot()) return;
        if (fluidAmount(PylonFluids.OBSCYRA) < fluidPerCraft) return;
        if (isProcessing()) {
            progressItem.notifyWindows();
            removeFluid(PylonFluids.OBSCYRA, fluidPerCraft);
            progressProcess(getTickInterval());
            return;
        }
        startProcess(secondsconsume);
    }
    @Override
    public void onProcessFinished() {
        ItemStack[] itemStacks = new ItemStack[]{
            RebarTranscEndenceItems.QUIRP_UP.clone(),
            RebarTranscEndenceItems.QUIRP_DOWN.clone(),
            RebarTranscEndenceItems.QUIRP_LEFT.clone(),
            RebarTranscEndenceItems.QUIRP_RIGHT.clone(),
        };
        Double weightNow = 0.0;
        Double randomDouble = Math.random();
        for (int i = 0; i < weight.length; i++) {
            weightNow += weight[i];
            if (weightNow >= randomDouble) {
                if (outputInventory.canHold(itemStacks[i])) {
                    outputInventory.addItem(new MachineUpdateReason(), itemStacks[i]);
                    setOutput(itemStacks[i]);
                }
                break;
            }
        }
    }
    public void setOutput(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
    }
    public Component getOutput() {
        return this.itemStack == null || this.itemStack.isEmpty()
            ? Component.translatable("rebartranscendence.item.quirp_oscillator.waila.none_item").color(colorToTextColor(Color.RED))
            : this.itemStack.effectiveName().color(colorToTextColor(Color.LIME));
    }
    @Override
    public @NotNull Map<@NotNull String, @NotNull VirtualInventory> getVirtualInventories() {
        return Map.of("polarizer", polarizerInventory, "output", outputInventory);
    }
    @Override
    public void onBreak(@NotNull List<@NotNull ItemStack> drops, @NotNull BlockBreakContext context) {
        RebarVirtualInventoryBlock.super.onBreak(drops, context);
        RebarFluidBufferBlock.super.onBreak(drops, context);
    }
    @Override
    public @Nullable WailaDisplay getWaila(@NotNull Player player) {
        return new WailaDisplay(isProcessing()
            ? Component.translatable("rebartranscendence.item.quirp_oscillator.waila.running")
                .arguments(
                    RebarArgument.of("time", UnitFormat.SECONDS.format(
                        Math.round(progressItem.getTotalTime().toSeconds() * (1.0 - progressItem.getProgress())))),
                    RebarArgument.of("input-bar", PylonUtils.createFluidAmountBar(
                        fluidAmount(PylonFluids.OBSCYRA),
                        fluidCapacity(PylonFluids.OBSCYRA),
                        20,
                        TextColor.fromHexString("#000000")
                )))
            : Component.translatable("rebartranscendence.item.quirp_oscillator.waila.not_running")
                .arguments(
                    RebarArgument.of("output", getOutput()),
                    RebarArgument.of("input-bar", PylonUtils.createFluidAmountBar(
                        fluidAmount(PylonFluids.OBSCYRA),
                        fluidCapacity(PylonFluids.OBSCYRA),
                        20,
                        TextColor.fromHexString("#000000")
                )))
        );
    }
}
