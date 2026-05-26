package io.github.tagdl.RebarTranscEndence;

import io.github.pylonmc.rebar.block.RebarBlock;
import io.github.tagdl.RebarTranscEndence.blocks.NanobotCrafter;
import io.github.tagdl.RebarTranscEndence.blocks.NanobotLauncher;
import io.github.tagdl.RebarTranscEndence.blocks.NanobotOutpuHatch;
import io.github.tagdl.RebarTranscEndence.blocks.Stabilizer;
import io.github.tagdl.RebarTranscEndence.blocks.QuirpOscillator;
import io.github.tagdl.RebarTranscEndence.blocks.QuirpAnnihilator;
import io.github.tagdl.RebarTranscEndence.blocks.ZotOverloader;
import io.github.tagdl.RebarTranscEndence.blocks.QuirpCycler;

import org.bukkit.Material;

public final class RebarTranscEndenceBlocks {

    public static void initialize() {
        RebarBlock.register(RebarTranscEndenceKeys.QUIRP_OSCILLATOR, Material.PURPUR_PILLAR, QuirpOscillator.class);
        RebarBlock.register(RebarTranscEndenceKeys.QUIRP_ANNIHILATOR, Material.YELLOW_CONCRETE, QuirpAnnihilator.class);
        RebarBlock.register(RebarTranscEndenceKeys.QUIRP_CYCLER, Material.BLUE_CONCRETE, QuirpCycler.class);
        RebarBlock.register(RebarTranscEndenceKeys.STABILIZER, Material.BLACK_CONCRETE, Stabilizer.class);
        RebarBlock.register(RebarTranscEndenceKeys.ZOT_OVERLOADER, Material.WHITE_CONCRETE, ZotOverloader.class);
        RebarBlock.register(RebarTranscEndenceKeys.NANOBOT_CRAFTER, Material.DISPENSER, NanobotCrafter.class);
        RebarBlock.register(RebarTranscEndenceKeys.NANOBOT_LAUNCHER, Material.CRAFTING_TABLE, NanobotLauncher.class);
        RebarBlock.register(RebarTranscEndenceKeys.NANOBOT_OUTPUT_HATCH, Material.PURPUR_BLOCK, NanobotOutpuHatch.class);
    }
}
