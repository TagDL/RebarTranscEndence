package io.github.tagdl.RebarTranscEndence.items;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import io.github.pylonmc.rebar.i18n.RebarArgument;
import io.github.pylonmc.rebar.item.RebarItem;
import io.github.tagdl.RebarTranscEndence.daxi.DaxiAnimation;

public class Daxi {
    public static class DaxiS extends RebarItem{
        public DaxiS(@NotNull ItemStack stack) {
            super(stack);
        }
        @Override
        public @NotNull List<RebarArgument> getPlaceholders() {
            return List.of(
                    RebarArgument.of("level", DaxiAnimation.Type.STRENGTH.getLevel())
            );
        }
    }
    public static class DaxiA extends RebarItem{
        public DaxiA(@NotNull ItemStack stack) {
            super(stack);
        }
        @Override
        public @NotNull List<RebarArgument> getPlaceholders() {
            return List.of(
                    RebarArgument.of("level", DaxiAnimation.Type.ABSORPTION.getLevel())
            );
        }
    }
    public static class DaxiF extends RebarItem{
        public DaxiF(@NotNull ItemStack stack) {
            super(stack);
        }
        @Override
        public @NotNull List<RebarArgument> getPlaceholders() {
            return List.of(
                    RebarArgument.of("level", DaxiAnimation.Type.FORTITUDE.getLevel())
            );
        }
    }
    public static class DaxiH extends RebarItem{
        public DaxiH(@NotNull ItemStack stack) {
            super(stack);
        }
        @Override
        public @NotNull List<RebarArgument> getPlaceholders() {
            return List.of(
                    RebarArgument.of("level", DaxiAnimation.Type.SATURATION.getLevel())
            );
        }
    }
    public static class DaxiR extends RebarItem{
        public DaxiR(@NotNull ItemStack stack) {
            super(stack);
        }
        @Override
        public @NotNull List<RebarArgument> getPlaceholders() {
            return List.of(
                    RebarArgument.of("level", DaxiAnimation.Type.REGENERATION.getLevel())
            );
        }
    }
}
