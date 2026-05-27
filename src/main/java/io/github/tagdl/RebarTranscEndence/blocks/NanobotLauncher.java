package io.github.tagdl.RebarTranscEndence.blocks;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import io.github.pylonmc.rebar.block.BlockStorage;
import io.github.pylonmc.rebar.block.RebarBlock;
import io.github.pylonmc.rebar.block.base.RebarDirectionalBlock;
import io.github.pylonmc.rebar.block.base.RebarInteractBlock;
import io.github.pylonmc.rebar.block.base.RebarNoVanillaInventoryBlock;
import io.github.pylonmc.rebar.block.context.BlockCreateContext;
import io.github.tagdl.RebarTranscEndence.recipe.NanobotCrafterRecipe;

public class NanobotLauncher extends RebarBlock implements
        RebarDirectionalBlock,
        RebarInteractBlock,
        RebarNoVanillaInventoryBlock
{
    public NanobotLauncher(@NotNull Block block, @NotNull BlockCreateContext context) {
        super(block, context);
        this.setFacing(context.getFacing());
    }
    public NanobotLauncher(@NotNull Block block, @NotNull PersistentDataContainer pdc) {
        super(block, pdc);
    }
    @Override
    public void onInteract(@NotNull PlayerInteractEvent event, @NotNull EventPriority priority) {
        if (event.getHand() != EquipmentSlot.HAND
                || !event.getAction().isRightClick()
        ) return;
        event.setUseItemInHand(Event.Result.DENY);
        event.setUseInteractedBlock(Event.Result.DENY);
        NanobotCrafter nanobotCrafter = findBlock();
        if (nanobotCrafter == null) return;
        if (!nanobotCrafter.isFormedAndFullyLoaded()) return;
        NanobotCrafterRecipe recipe = nanobotCrafter.getRecipe();
        if (recipe == null) return;
        nanobotCrafter.tryStartRecipe(recipe, event.getPlayer(), getBlock());
        return;
    }
    private NanobotCrafter findBlock(){
        NanobotCrafter block = null;
        if (BlockStorage.get(getBlock().getRelative(1, 0, 0)) instanceof NanobotCrafter nanobotCrafter) block = nanobotCrafter;
        else if (BlockStorage.get(getBlock().getRelative(0, 0, 1)) instanceof NanobotCrafter nanobotCrafter) block = nanobotCrafter;
        else if (BlockStorage.get(getBlock().getRelative(-1, 0, 0)) instanceof NanobotCrafter nanobotCrafter) block = nanobotCrafter;
        else if (BlockStorage.get(getBlock().getRelative(0, 0, -1)) instanceof NanobotCrafter nanobotCrafter) block = nanobotCrafter;
        return block;
    }
}
