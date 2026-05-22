package io.github.tagdl.RebarTranscEndence.blocks;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import io.github.pylonmc.pylon.PylonFluids;
import io.github.pylonmc.rebar.block.RebarBlock;
import io.github.pylonmc.rebar.block.base.RebarDirectionalBlock;
import io.github.pylonmc.rebar.block.base.RebarFluidBufferBlock;
import io.github.pylonmc.rebar.block.base.RebarTickingBlock;
import io.github.pylonmc.rebar.block.context.BlockCreateContext;
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter;
import io.github.pylonmc.rebar.fluid.FluidPointType;

public class ZotReverser extends RebarBlock implements
        RebarDirectionalBlock,
        RebarTickingBlock,
        RebarFluidBufferBlock
{
    public final double buffer = getSettings().getOrThrow("buffer", ConfigAdapter.INTEGER);
    public final double fluidPerCraft = getSettings().getOrThrow("fluid-per-craft", ConfigAdapter.INTEGER);
    public final int secondsconsume = Math.round(getSettings().getOrThrow("seconds-consume", ConfigAdapter.FLOAT) * 20);
    
    public ZotReverser(@NotNull Block block, @NotNull BlockCreateContext context) {
        super(block, context);
        setTickInterval(1);
        this.setFacing(context.getFacing());
        createFluidPoint(FluidPointType.INPUT, BlockFace.NORTH, context, false);
        createFluidBuffer(PylonFluids.OBSCYRA, buffer, true, false);
    }
    public ZotReverser(@NotNull Block block, @NotNull PersistentDataContainer pdc) {
        super(block, pdc);
    }
    @Override
    public void tick(){

    }
}
