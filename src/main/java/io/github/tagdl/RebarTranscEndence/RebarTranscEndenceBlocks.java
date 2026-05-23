package io.github.tagdl.RebarTranscEndence;

import io.github.pylonmc.rebar.block.RebarBlock;
import io.github.tagdl.RebarTranscEndence.blocks.StableMachine;
import io.github.tagdl.RebarTranscEndence.blocks.ZotCollector;
import io.github.tagdl.RebarTranscEndence.blocks.ZotCondenser;
import io.github.tagdl.RebarTranscEndence.blocks.ZotReverser;

import org.bukkit.Material;


public final class RebarTranscEndenceBlocks {

    public static void initialize() {
        RebarBlock.register(RebarTranscEndenceKeys.ZOT_COLLECTOR, Material.PURPUR_PILLAR, ZotCollector.class);
        RebarBlock.register(RebarTranscEndenceKeys.ZOT_CONDENSER, Material.YELLOW_CONCRETE, ZotCondenser.class);
        RebarBlock.register(RebarTranscEndenceKeys.ZOT_REVERSER, Material.BLUE_CONCRETE, ZotReverser.class);
        RebarBlock.register(RebarTranscEndenceKeys.STABLE_MACHINE, Material.BLACK_CONCRETE, StableMachine.class);
    }
}
