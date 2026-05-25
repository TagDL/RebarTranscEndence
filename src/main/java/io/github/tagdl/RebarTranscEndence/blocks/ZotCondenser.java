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
import xyz.xenondevs.invui.inventory.event.ItemPreUpdateEvent;

public class ZotCondenser extends RebarBlock implements
        RebarProcessor,
        RebarDirectionalBlock,
        RebarFluidBufferBlock,
        RebarTickingBlock,
        RebarVirtualInventoryBlock,
        RebarLogisticBlock,
        RebarGuiBlock
{
    public final double buffer = getSettings().getOrThrow("buffer", ConfigAdapter.INTEGER);
    public final double fluidPerCraft = getSettings().getOrThrow("fluid-per-craft", ConfigAdapter.INTEGER);
    public final int secondsconsume = Math.round(getSettings().getOrThrow("seconds-consume", ConfigAdapter.FLOAT) * 20); 

    public final ItemStackBuilder zotStack = ItemStackBuilder.gui(Material.PURPLE_STAINED_GLASS_PANE, getKey() + ":zot")
            .name(Component.translatable("rebartranscendence.gui.zot_condenser.zot"));
    private final ItemStackBuilder progressItemStackBuilder = ItemStackBuilder.of(Material.CLOCK)
            .name(Component.translatable("rebartranscendence.gui.zot_condenser.progress"));
    private final ProgressItem progressItem = new ProgressItem(progressItemStackBuilder, false);
    private final VirtualInventory zotInventory = new VirtualInventory(2);
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
    public ZotCondenser(@NotNull Block block, @NotNull BlockCreateContext context) {
        super(block, context);
        setTickInterval(20);
        this.setFacing(context.getFacing());
        createFluidPoint(FluidPointType.INPUT, BlockFace.NORTH, context, false);
        createFluidBuffer(PylonFluids.OBSCYRA, buffer, true, false);
    }
    public ZotCondenser(@NotNull Block block, @NotNull PersistentDataContainer pdc) {
        super(block, pdc);
    }
    @Override
    public void postInitialise() {
        setProcessProgressItem(progressItem);
        zotInventory.addPreUpdateHandler(event -> onZotUpdate(event));
        outputInventory.addPreUpdateHandler(event -> onOutputUpdate(event));
        createLogisticGroup("zot", LogisticGroupType.INPUT, zotInventory);
        createLogisticGroup("output", LogisticGroupType.OUTPUT, outputInventory);
    }
    public void onZotUpdate(ItemPreUpdateEvent event){
        if (event.isAdd()) { //only receive opposite direction's zot
            if (event.getNewItem().isSimilar(reverseZot(event.getNewItem()))) {
                event.setCancelled(true);
            } else {
                ItemStack otherItemStack = event.getSlot() == 1 ? zotInventory.getItem(0) : zotInventory.getItem(1);
                if (otherItemStack != null && !otherItemStack.isEmpty()) {
                    ItemStack reverseItemStack = reverseZot(otherItemStack.asOne());
                    event.setCancelled(reverseItemStack == null || reverseItemStack.isEmpty() 
                        ? false
                        : !event.getNewItem().isSimilar(reverseItemStack));
                }
            }

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
                        "A I A I A P B O B",
                        "A A A A A # B B B"
                )
                .addIngredient('#', GuiItems.background())
                .addIngredient('A', zotStack)
                .addIngredient('B', GuiItems.output())
                .addIngredient('P', progressItem)
                .addIngredient('I', zotInventory)
                .addIngredient('O', outputInventory)
                .build();
    }
    @Override
    public void tick() {
        if (getBlock() == null || !getBlock().getChunk().isLoaded()) return;
        if (outputInventory == null) return;
        if (!outputInventory.canHold(RebarTranscEndenceItems.ZOT_COOL_DOWN.clone())) return;
        if (fluidAmount(PylonFluids.OBSCYRA) < fluidPerCraft) return;
        if (isProcessing()) {
            progressItem.notifyWindows();
            removeFluid(PylonFluids.OBSCYRA, fluidPerCraft);
            progressProcess(getTickInterval());
            return;
        }
        if (zotInventory.containsSimilar(RebarTranscEndenceItems.ZOT_DOWN.clone()) 
                    && zotInventory.containsSimilar(RebarTranscEndenceItems.ZOT_UP.clone())) {
            zotInventory.setItem(new MachineUpdateReason(), 0, 
                zotInventory.getItem(0).asQuantity(zotInventory.getItemAmount(0) - 1));
            zotInventory.setItem(new MachineUpdateReason(), 1, 
                zotInventory.getItem(1).asQuantity(zotInventory.getItemAmount(1) - 1));
        } else if (zotInventory.containsSimilar(RebarTranscEndenceItems.ZOT_RIGHT.clone()) 
                    && zotInventory.containsSimilar(RebarTranscEndenceItems.ZOT_LEFT.clone())) {
            zotInventory.setItem(new MachineUpdateReason(), 0, 
                zotInventory.getItem(0).asQuantity(zotInventory.getItemAmount(0) - 1));
            zotInventory.setItem(new MachineUpdateReason(), 1, 
                zotInventory.getItem(1).asQuantity(zotInventory.getItemAmount(1) - 1));
        } else return;
        startProcess(secondsconsume);
    }
    @Override
    public void onProcessFinished() {
        progressItem.notifyWindows();
        outputInventory.addItem(new MachineUpdateReason(), RebarTranscEndenceItems.ZOT_COOL_DOWN.clone());
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
    private ItemStack reverseZot(ItemStack itemStack) {
        ItemStack resulItemStack = itemStack;
        if (itemStack.isSimilar(RebarTranscEndenceItems.ZOT_DOWN.clone())) resulItemStack = RebarTranscEndenceItems.ZOT_UP.clone();
        if (itemStack.isSimilar(RebarTranscEndenceItems.ZOT_UP.clone())) resulItemStack = RebarTranscEndenceItems.ZOT_DOWN.clone();
        if (itemStack.isSimilar(RebarTranscEndenceItems.ZOT_RIGHT.clone())) resulItemStack = RebarTranscEndenceItems.ZOT_LEFT.clone();
        if (itemStack.isSimilar(RebarTranscEndenceItems.ZOT_LEFT.clone())) resulItemStack = RebarTranscEndenceItems.ZOT_RIGHT.clone();
        return resulItemStack;
    }
    @Override
    public @Nullable WailaDisplay getWaila(@NotNull Player player) {
        return new WailaDisplay(isProcessing()
            ? Component.translatable("rebartranscendence.item.zot_condenser.waila.running")
                .arguments(
                    RebarArgument.of("time", 
                        Math.round(progressItem.getTotalTime().toSeconds() * (1.0 - progressItem.getProgress()))),
                    RebarArgument.of("input-bar", PylonUtils.createFluidAmountBar(
                        fluidAmount(PylonFluids.OBSCYRA),
                        fluidCapacity(PylonFluids.OBSCYRA),
                        20,
                        TextColor.fromHexString("#000000")
                )))
            : Component.translatable("rebartranscendence.item.zot_condenser.waila.not_running")
                .arguments(RebarArgument.of("input-bar", PylonUtils.createFluidAmountBar(
                        fluidAmount(PylonFluids.OBSCYRA),
                        fluidCapacity(PylonFluids.OBSCYRA),
                        20,
                        TextColor.fromHexString("#000000")
                )))
        );
    }
}
