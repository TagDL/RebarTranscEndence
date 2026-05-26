package io.github.tagdl.RebarTranscEndence;

import io.github.pylonmc.rebar.addon.RebarAddon;
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder;
import io.github.tagdl.RebarTranscEndence.daxi.DaxiListener;
import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;

import com.destroystokyo.paper.profile.PlayerProfile;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

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
