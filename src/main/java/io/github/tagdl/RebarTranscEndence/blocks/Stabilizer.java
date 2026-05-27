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
import io.github.pylonmc.rebar.block.base.RebarProcessor;
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
import io.github.tagdl.RebarTranscEndence.items.UnstableIngot;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.inventory.VirtualInventory;
import xyz.xenondevs.invui.inventory.event.ItemPostUpdateEvent;
import xyz.xenondevs.invui.inventory.event.ItemPreUpdateEvent;

public class Stabilizer extends RebarBlock implements
        RebarDirectionalBlock,
        RebarFluidBufferBlock,
        RebarVirtualInventoryBlock,
        RebarLogisticBlock,
        RebarTickingBlock,
        RebarProcessor,
        RebarInventoryBlock
{
    private static final Config settings = Settings.get(RebarTranscEndenceKeys.STABILIZER);
    public static final double buffer = settings.getOrThrow("buffer", ConfigAdapter.INTEGER);
    public static final double fluidPerCraft = settings.getOrThrow("fluid-per-craft", ConfigAdapter.INTEGER);
    public static final int timeconsume = Math.round(settings.getOrThrow("seconds-consume", ConfigAdapter.FLOAT) * 20);

    public final ItemStackBuilder inputStack = ItemStackBuilder.gui(Material.PURPLE_STAINED_GLASS_PANE, getKey() + ":input")
            .name(Component.translatable("rebartranscendence.gui.stabilizer.input"));
    private final ItemStackBuilder progressItemStackBuilder = ItemStackBuilder.of(Material.CLOCK)
            .name(Component.translatable("rebartranscendence.gui.stabilizer.progress"));
    private final ProgressItem progressItem = new ProgressItem(progressItemStackBuilder, false);
    private final VirtualInventory cooldownInventory = new VirtualInventory(1);
    private final VirtualInventory ingotInventory = new VirtualInventory(1);
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
    public Stabilizer(@NotNull Block block, @NotNull BlockCreateContext context) {
        super(block, context);
        setTickInterval(20);
        this.setFacing(context.getFacing());
        createFluidPoint(FluidPointType.INPUT, BlockFace.NORTH, context, false);
        createFluidBuffer(PylonFluids.OBSCYRA, buffer, true, false);
    }
    public Stabilizer(@NotNull Block block, @NotNull PersistentDataContainer pdc) {
        super(block, pdc);
    }
    @Override
    public void postInitialise() {
        setProcessProgressItem(progressItem);
        cooldownInventory.addPreUpdateHandler(event -> onCooldownUpdate(event));
        ingotInventory.addPreUpdateHandler(event -> onIngotUpdate(event));
        cooldownInventory.addPostUpdateHandler(event -> onPostUpdate(event));
        ingotInventory.addPostUpdateHandler(event -> onPostUpdate(event));
        outputInventory.addPreUpdateHandler(event -> onOutputUpdate(event));
        createLogisticGroup("input2", LogisticGroupType.INPUT, cooldownInventory);
        createLogisticGroup("input1", LogisticGroupType.INPUT, ingotInventory);
        createLogisticGroup("output", LogisticGroupType.OUTPUT, outputInventory);
    }
    public void onIngotUpdate(ItemPreUpdateEvent event){
        if (event.isAdd()) event.setCancelled(!(RebarItem.fromStack(event.getNewItem()) instanceof UnstableIngot));
    }
    public void onCooldownUpdate(ItemPreUpdateEvent event){
        if (event.isAdd()) event.setCancelled(!(event.getNewItem().isSimilar(RebarTranscEndenceItems.QUIRP_CONDENSATE.clone())));
    }
    public void onPostUpdate(ItemPostUpdateEvent event){
        if (!(event.getUpdateReason() instanceof MachineUpdateReason)) {
            if (isProcessing()) stopProcess();
        }
    }
    public void onOutputUpdate(ItemPreUpdateEvent event){
        if (event.isAdd()) event.setCancelled(!(event.getUpdateReason() instanceof MachineUpdateReason));
    }
    @Override
    public @NotNull Gui createGui() {
        return Gui.builder()
                .setStructure(
                        "A A A A A # B B B",
                        "A I A C A P B O B",
                        "A A A A A # B B B"
                )
                .addIngredient('#', GuiItems.background())
                .addIngredient('A', inputStack)
                .addIngredient('B', GuiItems.output())
                .addIngredient('P', progressItem)
                .addIngredient('C', cooldownInventory)
                .addIngredient('I', ingotInventory)
                .addIngredient('O', outputInventory)
                .build();
    }
    @Override
    public void tick() {
        if (getBlock() == null || !getBlock().getChunk().isLoaded()) return;
        if (outputInventory == null) return;
        if (ingotInventory.isEmpty() || cooldownInventory.isEmpty()) return;
        if (!outputInventory.isEmpty()) if (!canRun()) return;
        if (fluidAmount(PylonFluids.OBSCYRA) < fluidPerCraft) return;
        if (isProcessing()) {
            progressItem.notifyWindows();
            removeFluid(PylonFluids.OBSCYRA, fluidPerCraft);
            progressProcess(getTickInterval());
            return;
        }
        startProcess(timeconsume);
    }
    @Override
    public void onProcessFinished() {
        progressItem.notifyWindows();
        UnstableIngot unstableIngot = (UnstableIngot) RebarItem.fromStack(ingotInventory.getItem(0).asOne());
        unstableIngot.setAmount(unstableIngot.getAmount() - 25);
        outputInventory.addItem(new MachineUpdateReason(), 
            unstableIngot.getAmount() == 0 ? RebarTranscEndenceItems.STABLE_INGOT.clone() : unstableIngot.getStack()
        );
        ingotInventory.setItem(new MachineUpdateReason(), 0, 
            ingotInventory.getItem(0).asQuantity(ingotInventory.getItemAmount(0) - 1));
        cooldownInventory.setItem(new MachineUpdateReason(), 0, 
            cooldownInventory.getItem(0).asQuantity(cooldownInventory.getItemAmount(0) - 1));
    }
    @Override
    public @NotNull Map<@NotNull String, @NotNull VirtualInventory> getVirtualInventories() {
        return Map.of("input1", ingotInventory, "input2", cooldownInventory, "output", outputInventory);
    }
    @Override
    public void onBreak(@NotNull List<@NotNull ItemStack> drops, @NotNull BlockBreakContext context) {
        RebarVirtualInventoryBlock.super.onBreak(drops, context);
        RebarFluidBufferBlock.super.onBreak(drops, context);
    }
    public boolean canRun() {
        if (outputInventory.canHold(RebarTranscEndenceItems.STABLE_INGOT.clone())) {
            if (RebarItem.fromStack(ingotInventory.getItem(0)) instanceof UnstableIngot unstableIngot) {
                return unstableIngot.getAmount() == 25;
            }
        }
        if (RebarItem.fromStack(outputInventory.getItem(0)) instanceof UnstableIngot unstableIngot_output) {
            if (RebarItem.fromStack(ingotInventory.getItem(0)) instanceof UnstableIngot unstableIngot) {
                return unstableIngot.getAmount() - 25 == unstableIngot_output.getAmount();
            }
        }
        return false;
    }
    @Override
    public @Nullable WailaDisplay getWaila(@NotNull Player player) {
        return new WailaDisplay(isProcessing()
            ? Component.translatable("rebartranscendence.item.stabilizer.waila.running")
                .arguments(
                    RebarArgument.of("time", UnitFormat.SECONDS.format(
                        Math.round(progressItem.getTotalTime().toSeconds() * (1.0 - progressItem.getProgress())))),
                    RebarArgument.of("input-bar", PylonUtils.createFluidAmountBar(
                        fluidAmount(PylonFluids.OBSCYRA),
                        fluidCapacity(PylonFluids.OBSCYRA),
                        20,
                        TextColor.fromHexString("#000000")
                )))
            : Component.translatable("rebartranscendence.item.stabilizer.waila.not_running")
                .arguments(RebarArgument.of("input-bar", PylonUtils.createFluidAmountBar(
                        fluidAmount(PylonFluids.OBSCYRA),
                        fluidCapacity(PylonFluids.OBSCYRA),
                        20,
                        TextColor.fromHexString("#000000")
                )))
        );
    }
}