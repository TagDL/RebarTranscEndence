package io.github.tagdl.RebarTranscEndence;

import io.github.pylonmc.rebar.addon.RebarAddon;
import io.github.tagdl.RebarTranscEndence.daxi.DaxiListener;
import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Set;

@SuppressWarnings("unused")
public class RebarTranscEndence extends JavaPlugin implements RebarAddon {

    // Stores the instance of the addon (there's only ever one)
    @Getter private static RebarTranscEndence instance;

    // Called when the addon is enabled
    @Override
    public void onEnable() {
        instance = this;

        // Every Rebar addon must call this BEFORE doing anything Rebar-related
        registerWithRebar();
        PluginManager pm = Bukkit.getPluginManager();
        RebarTranscEndenceItems.initialize();
        RebarTranscEndenceBlocks.initialize();
        RebarTranscEndencePages.initialise();
        RebarTranscEndenceRecipe.initialize();
        pm.registerEvents(new DaxiListener(), this);
    }

    @Override
    public @NotNull JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public @NotNull Set<@NotNull Locale> getLanguages() {
        return Set.of(Locale.ENGLISH);
    }

    @Override
    public @NotNull Material getMaterial() {
        return Material.DEAD_BUSH;
    }
}
