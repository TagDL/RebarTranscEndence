package io.github.tagdl.RebarTranscEndence;

import io.github.pylonmc.rebar.block.RebarBlock;
import io.github.tagdl.RebarTranscEndence.blocks.ZotCollector;
import io.github.tagdl.RebarTranscEndence.blocks.ZotCondenser;

import org.bukkit.Material;


public final class RebarTranscEndenceBlocks {

    public static void initialize() {
        RebarBlock.register(RebarTranscEndenceKeys.ZOT_COLLECTOR, Material.PURPUR_BLOCK, ZotCollector.class);
        RebarBlock.register(RebarTranscEndenceKeys.ZOT_CONDENSER, Material.YELLOW_CONCRETE, ZotCondenser.class);
    }
}
