package io.github.tagdl.RebarTranscEndence.blocks;

import static java.lang.Math.max;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import io.github.pylonmc.pylon.PylonFluids;
import io.github.pylonmc.rebar.block.RebarBlock;
import io.github.pylonmc.rebar.block.base.RebarDirectionalBlock;
import io.github.pylonmc.rebar.block.base.RebarFluidBufferBlock;
import io.github.pylonmc.rebar.block.base.RebarGuiBlock;
import io.github.pylonmc.rebar.block.base.RebarLogisticBlock;
import io.github.pylonmc.rebar.block.base.RebarTickingBlock;
import io.github.pylonmc.rebar.block.base.RebarVirtualInventoryBlock;
import io.github.pylonmc.rebar.block.context.BlockBreakContext;
import io.github.pylonmc.rebar.block.context.BlockCreateContext;
import io.github.pylonmc.rebar.config.Config;
import io.github.pylonmc.rebar.config.Settings;
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter;
import io.github.pylonmc.rebar.fluid.FluidPointType;
import io.github.pylonmc.rebar.item.RebarItem;
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder;
import io.github.pylonmc.rebar.logistics.LogisticGroupType;
import io.github.pylonmc.rebar.util.MachineUpdateReason;
import io.github.pylonmc.rebar.util.gui.GuiItems;
import io.github.tagdl.RebarTranscEndence.RebarTranscEndenceItems;
import io.github.tagdl.RebarTranscEndence.RebarTranscEndenceKeys;
import io.github.tagdl.RebarTranscEndence.items.Zot_2;
import net.kyori.adventure.text.Component;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.inventory.VirtualInventory;
import xyz.xenondevs.invui.inventory.event.ItemPreUpdateEvent;

public class ZotOverloader extends RebarBlock implements
        RebarDirectionalBlock,
        RebarTickingBlock,
        RebarFluidBufferBlock,
        RebarLogisticBlock,
        RebarVirtualInventoryBlock,
        RebarGuiBlock
{
    private static final Config settings = Settings.get(RebarTranscEndenceKeys.ZOT_OVERLOADER);
    public static final double buffer = settings.getOrThrow("buffer", ConfigAdapter.INTEGER);
    public static final double fluidPerCraft = settings.getOrThrow("fluid-per-craft", ConfigAdapter.INTEGER);
    public static final int timeconsume = Math.round(settings.getOrThrow("seconds-consume", ConfigAdapter.FLOAT) * 20);
    public static final int diffZot = settings.getOrThrow("different-direction-zot-require", ConfigAdapter.INTEGER);
    public final ItemStackBuilder upStack = ItemStackBuilder.gui(Material.RED_STAINED_GLASS_PANE, getKey() + ":up")
            .name(Component.translatable("rebartranscendence.gui.zot_condenser.up"));
    public final ItemStackBuilder downStack = ItemStackBuilder.gui(Material.YELLOW_STAINED_GLASS_PANE, getKey() + ":down")
            .name(Component.translatable("rebartranscendence.gui.zot_condenser.down"));
    public final ItemStackBuilder leftStack = ItemStackBuilder.gui(Material.GREEN_STAINED_GLASS_PANE, getKey() + ":left")
            .name(Component.translatable("rebartranscendence.gui.zot_condenser.left"));
    public final ItemStackBuilder rightStack = ItemStackBuilder.gui(Material.BLUE_STAINED_GLASS_PANE, getKey() + ":right")
            .name(Component.translatable("rebartranscendence.gui.zot_condenser.right"));
    public final ItemStackBuilder unchargeStack = ItemStackBuilder.gui(Material.BLUE_STAINED_GLASS_PANE, getKey() + ":uncharge")
            .name(Component.translatable("rebartranscendence.gui.zot_condenser.uncharge"));
    private final VirtualInventory zotInventory = new VirtualInventory(4);
    private final VirtualInventory outputInventory = new VirtualInventory(1);
    private final ItemStack[] itemStacks_2 = new ItemStack[]{
                RebarTranscEndenceItems.ZOT_UP_2.clone(), 
                RebarTranscEndenceItems.ZOT_DOWN_2.clone(), 
                RebarTranscEndenceItems.ZOT_LEFT_2.clone(), 
                RebarTranscEndenceItems.ZOT_RIGHT_2.clone()
            };
    public ZotOverloader(@NotNull Block block, @NotNull BlockCreateContext context) {
        super(block, context);
        setTickInterval(timeconsume);
        this.setFacing(context.getFacing());
        createFluidPoint(FluidPointType.INPUT, BlockFace.NORTH, context, false);
        createFluidBuffer(PylonFluids.OBSCYRA, buffer, true, false);
    }
    public ZotOverloader(@NotNull Block block, @NotNull PersistentDataContainer pdc) {
        super(block, pdc);
    }
    @Override
    public void postInitialise() {
        zotInventory.addPreUpdateHandler(event -> onZotUpdate(event));
        outputInventory.addPreUpdateHandler(event -> onOutputUpdate(event));
        createLogisticGroup("zot", LogisticGroupType.INPUT, zotInventory);
        createLogisticGroup("output", LogisticGroupType.OUTPUT, outputInventory);
    }
    public void onZotUpdate(ItemPreUpdateEvent event){
        if (event.isAdd() || event.isSwap()) {
            switch (event.getSlot()) {
                case 0:
                    event.setCancelled(!event.getNewItem().isSimilar(RebarTranscEndenceItems.ZOT_UP.clone()));
                    break;
                case 1:
                    event.setCancelled(!event.getNewItem().isSimilar(RebarTranscEndenceItems.ZOT_DOWN.clone()));
                    break;
                case 2:
                    event.setCancelled(!event.getNewItem().isSimilar(RebarTranscEndenceItems.ZOT_LEFT.clone()));
                    break;
                case 3:
                    event.setCancelled(!event.getNewItem().isSimilar(RebarTranscEndenceItems.ZOT_RIGHT.clone()));
                    break;
                default:
                    event.setCancelled(true);
                    break;
            }
        }
    }
    public void onOutputUpdate(ItemPreUpdateEvent event){
        if (event.isAdd()) event.setCancelled(!(RebarItem.fromStack(event.getNewItem()) instanceof Zot_2));
    }
    @Override
    public @NotNull Gui createGui() {
        return Gui.builder()
                .setStructure(
                        "A U D L R A B B B",
                        "A I I I I A B O B",
                        "A U D L R A B B B"
                )
                .addIngredient('#', GuiItems.background())
                .addIngredient('A', GuiItems.input())
                .addIngredient('B', unchargeStack)
                .addIngredient('I', zotInventory)
                .addIngredient('O', outputInventory)
                .addIngredient('U', upStack)
                .addIngredient('D', downStack)
                .addIngredient('L', leftStack)
                .addIngredient('R', rightStack)
                .build();
    }
    @Override
    public void tick() {
        if (getBlock() == null || !getBlock().getChunk().isLoaded()) return;
        if (outputInventory == null) return;
        if (outputInventory.getItem(0) == null || outputInventory.getItem(0).isEmpty()) return;
        if (fluidAmount(PylonFluids.OBSCYRA) < fluidPerCraft) return;
        if (!canRun()) return;
        Run();
    }
    public void Run() {
        ItemStack[] zot_3 = {
            RebarTranscEndenceItems.ZOT_UP_3.clone(),
            RebarTranscEndenceItems.ZOT_DOWN_3.clone(),
            RebarTranscEndenceItems.ZOT_LEFT_3.clone(),
            RebarTranscEndenceItems.ZOT_RIGHT_3.clone()
        };
        Zot_2 zot_2 = (Zot_2) RebarItem.fromStack(outputInventory.getItem(0).asOne());
        removeFluid(PylonFluids.OBSCYRA, fluidPerCraft);
        for (int i = 0; i < zotInventory.getSize(); i++) {
            if (RebarItem.fromStack(outputInventory.getItem(0)).getKey().equals(RebarItem.fromStack(itemStacks_2[i]).getKey())) { //find uncharge zot
                if (zotInventory.getItem(i) == null || zotInventory.getItem(i).isEmpty()) { //whether has same direcion zot
                    int remove_amount = diffZot;
                    for (int j = 0; j < zotInventory.getSize(); j++) {
                        if (remove_amount == 0) break;
                        if (zotInventory.getItem(j) == null || zotInventory.getItem(j).isEmpty()) continue;
                        int raw_amount = zotInventory.getItemAmount(j);
                        zotInventory.setItem(new MachineUpdateReason(), j, 
                            zotInventory.getItem(j).asQuantity(max(zotInventory.getItemAmount(j) - remove_amount, 0)));
                        remove_amount -= raw_amount - zotInventory.getItemAmount(j);
                    }
                } else {
                    zotInventory.setItem(new MachineUpdateReason(), i, 
                            zotInventory.getItem(i).asQuantity(zotInventory.getItemAmount(i) - 1));
                }
            }
        }
        if (zot_2.getAmount() == zot_2.maxAmount - 1) {
            for (int i = 0; i < itemStacks_2.length; i++) {
                if (RebarItem.fromStack(outputInventory.getItem(0)).getKey().equals(RebarItem.fromStack(itemStacks_2[i]).getKey())) {
                    outputInventory.setItem(new MachineUpdateReason(), 0, zot_3[i]);
                    break;
                }
            }
        } else {
            zot_2.setAmount(zot_2.getAmount() + 1);
            outputInventory.setItem(new MachineUpdateReason(), 0, zot_2.getStack());
        }
    }
    public boolean canRun() {
        boolean tempbool = false;
        for (int i = 0; i < itemStacks_2.length; i++) {
            if (RebarItem.fromStack(outputInventory.getItem(0)).getKey().equals(RebarItem.fromStack(itemStacks_2[i]).getKey())) { //find uncharge zot
                if (zotInventory.getItem(i) == null || zotInventory.getItem(i).isEmpty()) {
                    int amount_zot = 0;
                    for (ItemStack itemStack : zotInventory.getItems()) {
                        if (itemStack == null || itemStack.isEmpty()) continue;
                        amount_zot += itemStack.getAmount();
                    }
                    tempbool = amount_zot >= diffZot;
                } else {
                    tempbool = true;
                }
                break;
            }
        }
        return tempbool;
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
}
