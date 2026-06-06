package io.github.tagdl.RebarTranscEndence.blocks;

import static io.github.pylonmc.pylon.util.PylonUtils.colorToTextColor;
import static java.lang.Math.max;

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

import io.github.pylonmc.pylon.PylonFluids;
import io.github.pylonmc.pylon.util.PylonUtils;
import io.github.pylonmc.rebar.block.RebarBlock;
import io.github.pylonmc.rebar.block.interfaces.DirectionalRebarBlock;
import io.github.pylonmc.rebar.block.interfaces.FluidBufferRebarBlock;
import io.github.pylonmc.rebar.block.interfaces.GuiRebarBlock;
import io.github.pylonmc.rebar.block.interfaces.LogisticRebarBlock;
import io.github.pylonmc.rebar.block.interfaces.TickingRebarBlock;
import io.github.pylonmc.rebar.block.interfaces.VirtualInventoryRebarBlock;
import io.github.pylonmc.rebar.block.context.BlockBreakContext;
import io.github.pylonmc.rebar.block.context.BlockCreateContext;
import io.github.pylonmc.rebar.config.ConfigSection;
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter;
import io.github.pylonmc.rebar.fluid.FluidPointType;
import io.github.pylonmc.rebar.i18n.RebarArgument;
import io.github.pylonmc.rebar.item.RebarItem;
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder;
import io.github.pylonmc.rebar.logistics.LogisticGroupType;
import io.github.pylonmc.rebar.util.MachineUpdateReason;
import io.github.pylonmc.rebar.util.gui.GuiItems;
import io.github.pylonmc.rebar.util.gui.unit.UnitFormat;
import io.github.pylonmc.rebar.waila.WailaDisplay;
import io.github.tagdl.RebarTranscEndence.RebarTranscEndenceItems;
import io.github.tagdl.RebarTranscEndence.RebarTranscEndenceKeys;
import io.github.tagdl.RebarTranscEndence.items.Zot;
import io.github.tagdl.RebarTranscEndence.items.ZotDown;
import io.github.tagdl.RebarTranscEndence.items.ZotLeft;
import io.github.tagdl.RebarTranscEndence.items.ZotRight;
import io.github.tagdl.RebarTranscEndence.items.ZotUp;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.inventory.VirtualInventory;
import xyz.xenondevs.invui.inventory.event.ItemPreUpdateEvent;

public class ZotOverloader extends RebarBlock implements
        DirectionalRebarBlock,
        TickingRebarBlock,
        FluidBufferRebarBlock,
        LogisticRebarBlock,
        VirtualInventoryRebarBlock,
        GuiRebarBlock
{
    private static final ConfigSection settings = ConfigSection.fromSettings(RebarTranscEndenceKeys.ZOT_OVERLOADER);
    public static final double buffer = settings.getOrThrow("buffer", ConfigAdapter.INTEGER);
    public static final double fluidPerCraft = settings.getOrThrow("fluid-per-craft", ConfigAdapter.INTEGER);
    public static final int timeconsume = Math.round(settings.getOrThrow("seconds-consume", ConfigAdapter.FLOAT) * 20);
    public static final int diffZot = settings.getOrThrow("different-direction-zot-require", ConfigAdapter.INTEGER);
    public final ItemStackBuilder upStack = ItemStackBuilder.gui(Material.RED_STAINED_GLASS_PANE, getKey() + ":up")
            .name(Component.translatable("rebartranscendence.gui.zot_overloader.up"));
    public final ItemStackBuilder downStack = ItemStackBuilder.gui(Material.YELLOW_STAINED_GLASS_PANE, getKey() + ":down")
            .name(Component.translatable("rebartranscendence.gui.zot_overloader.down"));
    public final ItemStackBuilder leftStack = ItemStackBuilder.gui(Material.GREEN_STAINED_GLASS_PANE, getKey() + ":left")
            .name(Component.translatable("rebartranscendence.gui.zot_overloader.left"));
    public final ItemStackBuilder rightStack = ItemStackBuilder.gui(Material.BLUE_STAINED_GLASS_PANE, getKey() + ":right")
            .name(Component.translatable("rebartranscendence.gui.zot_overloader.right"));
    public final ItemStackBuilder unchargeStack = ItemStackBuilder.gui(Material.BLUE_STAINED_GLASS_PANE, getKey() + ":uncharge")
            .name(Component.translatable("rebartranscendence.gui.zot_overloader.uncharge"));
    private final VirtualInventory zotInventory = new VirtualInventory(4);
    private final VirtualInventory outputInventory = new VirtualInventory(1);
    private final ItemStack[] itemStacks_2 = new ItemStack[]{
                RebarTranscEndenceItems.ZOT_UP.clone(), 
                RebarTranscEndenceItems.ZOT_DOWN.clone(), 
                RebarTranscEndenceItems.ZOT_LEFT.clone(), 
                RebarTranscEndenceItems.ZOT_RIGHT.clone()
            };
    private Color color = Color.WHITE;
    private int inner_amount = 0;
    public static class Item extends RebarItem {

        public final double fluidPerCraft = getSettings().getOrThrow("fluid-per-craft", ConfigAdapter.INTEGER);
        public final double buffer = getSettings().getOrThrow("buffer", ConfigAdapter.INTEGER);
        public static final int diffZot = settings.getOrThrow("different-direction-zot-require", ConfigAdapter.INTEGER);
        public Item(@NotNull ItemStack stack) {
            super(stack);
        }
        @Override
        public @NotNull List<RebarArgument> getPlaceholders() {
            return List.of(
                    RebarArgument.of("diff", diffZot),
                    RebarArgument.of("fluid-per-craft", UnitFormat.MILLIBUCKETS.format(fluidPerCraft)),
                    RebarArgument.of("buffer", UnitFormat.MILLIBUCKETS.format(buffer))
            );
        }
    }
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
                    event.setCancelled(!event.getNewItem().isSimilar(RebarTranscEndenceItems.QUIRP_UP.clone()));
                    break;
                case 1:
                    event.setCancelled(!event.getNewItem().isSimilar(RebarTranscEndenceItems.QUIRP_DOWN.clone()));
                    break;
                case 2:
                    event.setCancelled(!event.getNewItem().isSimilar(RebarTranscEndenceItems.QUIRP_LEFT.clone()));
                    break;
                case 3:
                    event.setCancelled(!event.getNewItem().isSimilar(RebarTranscEndenceItems.QUIRP_RIGHT.clone()));
                    break;
                default:
                    event.setCancelled(true);
                    break;
            }
        }
    }
    public void onOutputUpdate(ItemPreUpdateEvent event){
        if (event.isAdd()) {
            if (!(RebarItem.fromStack(event.getNewItem()) instanceof Zot)) {
                event.setCancelled(true);
            } else {
                RebarItem newItem = RebarItem.fromStack(event.getNewItem());
                if (newItem instanceof ZotUp zotUp) this.inner_amount = zotUp.getAmount();
                else if (newItem instanceof ZotDown zotDown) this.inner_amount = zotDown.getAmount();
                else if (newItem instanceof ZotLeft zotLeft) this.inner_amount = zotLeft.getAmount();
                else if (newItem instanceof ZotRight zotRight) this.inner_amount = zotRight.getAmount();
            }
            
        }
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
        if (getBlock().getWorld().getEnvironment() != World.Environment.THE_END) return;
        if (outputInventory == null) return;
        if (outputInventory.getItem(0) == null || outputInventory.getItem(0).isEmpty()) return;
        if (fluidAmount(PylonFluids.OBSCYRA) < fluidPerCraft) return;
        if (!canRun()) return;
        Run();
    }
    public void Run() {
        ItemStack[] zot_2 = {
            RebarTranscEndenceItems.ZOT_UP_2.clone(),
            RebarTranscEndenceItems.ZOT_DOWN_2.clone(),
            RebarTranscEndenceItems.ZOT_LEFT_2.clone(),
            RebarTranscEndenceItems.ZOT_RIGHT_2.clone()
        };
        Zot zot = (Zot) RebarItem.fromStack(outputInventory.getItem(0).asOne());
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
        if (zot.getAmount() == zot.maxAmount - 1) {
            for (int i = 0; i < itemStacks_2.length; i++) {
                if (RebarItem.fromStack(outputInventory.getItem(0)).getKey().equals(RebarItem.fromStack(itemStacks_2[i]).getKey())) {
                    outputInventory.setItem(new MachineUpdateReason(), 0, zot_2[i]);
                    break;
                }
            }
        } else {
            zot.setAmount(zot.getAmount() + 1);
            outputInventory.setItem(new MachineUpdateReason(), 0, zot.getStack().clone());
            setInner(zot.getStack().asOne(), zot.getAmount());
        }
    }
    public void setInner(ItemStack itemStack, int amount) {
        if (RebarItem.fromStack(itemStack).getKey() == RebarTranscEndenceKeys.ZOT_UP) this.color = Color.RED;
        else if (RebarItem.fromStack(itemStack).getKey() == RebarTranscEndenceKeys.ZOT_DOWN) this.color = Color.YELLOW;
        else if (RebarItem.fromStack(itemStack).getKey() == RebarTranscEndenceKeys.ZOT_LEFT) this.color = Color.LIME;
        else if (RebarItem.fromStack(itemStack).getKey() == RebarTranscEndenceKeys.ZOT_RIGHT) this.color = Color.BLUE;
        this.inner_amount = amount;
    }
    public Component getInner() {
        return Component.text(this.inner_amount).color(colorToTextColor(this.color));
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
    public void onBlockBreak(@NotNull List<@NotNull ItemStack> drops, @NotNull BlockBreakContext context) {
        VirtualInventoryRebarBlock.super.onBlockBreak(drops, context);
        FluidBufferRebarBlock.super.onBlockBreak(drops, context);
    }
    @Override
    public @Nullable WailaDisplay getWaila(@NotNull Player player) {
        return new WailaDisplay(outputInventory != null && !outputInventory.isEmpty() 
            ? Component.translatable("rebartranscendence.item.zot_overloader.waila.running")
                .arguments(
                    RebarArgument.of("amount", getInner()),
                    RebarArgument.of("input-bar", PylonUtils.createFluidAmountBar(
                        fluidAmount(PylonFluids.OBSCYRA),
                        fluidCapacity(PylonFluids.OBSCYRA),
                        20,
                        TextColor.fromHexString("#000000")
                )))
            : Component.translatable("rebartranscendence.item.zot_overloader.waila.not_running")
                .arguments(RebarArgument.of("input-bar", PylonUtils.createFluidAmountBar(
                        fluidAmount(PylonFluids.OBSCYRA),
                        fluidCapacity(PylonFluids.OBSCYRA),
                        20,
                        TextColor.fromHexString("#000000")
                )))
        );
    }
}
