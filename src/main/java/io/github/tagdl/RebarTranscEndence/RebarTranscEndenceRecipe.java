package io.github.tagdl.RebarTranscEndence;

import io.github.tagdl.RebarTranscEndence.recipe.NanobotCrafterRecipe;
import io.github.tagdl.RebarTranscEndence.recipe.ZotReverserRecipe;

public class RebarTranscEndenceRecipe {

    private RebarTranscEndenceRecipe() {
        throw new AssertionError("Utility class");
    }
    public static void initialize() {
        ZotReverserRecipe.RECIPE_TYPE.register();
        NanobotCrafterRecipe.RECIPE_TYPE.register();
    }
}
