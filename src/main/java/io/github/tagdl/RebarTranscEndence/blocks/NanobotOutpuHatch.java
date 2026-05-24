package io.github.tagdl.RebarTranscEndence.blocks;

import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import io.github.pylonmc.pylon.content.components.ItemOutputHatch;
import io.github.pylonmc.rebar.block.context.BlockCreateContext;

public class NanobotOutpuHatch extends ItemOutputHatch {
    public NanobotOutpuHatch(@NotNull Block block, @NotNull BlockCreateContext context) {
        super(block, context);
    }
    public NanobotOutpuHatch(@NotNull Block block, @NotNull PersistentDataContainer pdc) {
        super(block, pdc);
    }
}
