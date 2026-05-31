package io.github.tagdl.RebarTranscEndence.daxi;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import io.github.pylonmc.rebar.config.Config;
import io.github.pylonmc.rebar.config.Settings;
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter;
import io.github.pylonmc.rebar.datatypes.RebarSerializers;
import io.github.tagdl.RebarTranscEndence.RebarTranscEndence;
import io.github.tagdl.RebarTranscEndence.RebarTranscEndenceItems;

public class DaxiListener implements Listener {
    public static final NamespacedKey DAXI_STRENGTH_KEY = new NamespacedKey(RebarTranscEndence.getInstance(), "daxi_strength_key");
    public static final NamespacedKey DAXI_ABSORPTION_KEY = new NamespacedKey(RebarTranscEndence.getInstance(), "daxi_absorption_key");
    public static final NamespacedKey DAXI_FORTITUDE_KEY = new NamespacedKey(RebarTranscEndence.getInstance(), "daxi_fortitude_key");
    public static final NamespacedKey DAXI_SATURATION_KEY = new NamespacedKey(RebarTranscEndence.getInstance(), "daxi_saturation_key");
    public static final NamespacedKey DAXI_REGENERATION_KEY = new NamespacedKey(RebarTranscEndence.getInstance(), "daxi_regeneration_key");
    private static final Config settings = Settings.get(new NamespacedKey(RebarTranscEndence.getInstance(), "daxi"));
    public final int strength = settings.getOrThrow("strength-level", ConfigAdapter.INTEGER);
    public final int absorption = settings.getOrThrow("absorption-level", ConfigAdapter.INTEGER);
    public final int fortitude = settings.getOrThrow("resistance-level", ConfigAdapter.INTEGER);
    public final int saturation = settings.getOrThrow("saturation-level", ConfigAdapter.INTEGER);
    public final int regeneration = settings.getOrThrow("regeneration-level", ConfigAdapter.INTEGER);
    @EventHandler
    public void onUsedToClick(@NotNull PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND 
                || event.useItemInHand() == Event.Result.DENY
                || event.getAction().isLeftClick()) return;
        if (event.getItem() == null) return;
        if (event.getItem().isSimilar(RebarTranscEndenceItems.DAXI_STRENGTH)) {
            if (!getPlayerPdc(event.getPlayer(), DAXI_STRENGTH_KEY)) {
                event.getItem().subtract();
                setPlayerPdc(event.getPlayer(), DAXI_STRENGTH_KEY, true);
                DaxiAnimation.startAnimation(event.getPlayer(), DaxiAnimation.Type.STRENGTH);
            }
        } else if (event.getItem().isSimilar(RebarTranscEndenceItems.DAXI_ABSORPTION)) {
            if (!getPlayerPdc(event.getPlayer(), DAXI_ABSORPTION_KEY)) {
                event.getItem().subtract();
                setPlayerPdc(event.getPlayer(), DAXI_ABSORPTION_KEY, true);
                DaxiAnimation.startAnimation(event.getPlayer(), DaxiAnimation.Type.ABSORPTION);
            }
        } else if (event.getItem().isSimilar(RebarTranscEndenceItems.DAXI_FORTITUDE)) {
            if (!getPlayerPdc(event.getPlayer(), DAXI_FORTITUDE_KEY)) {
                event.getItem().subtract();
                setPlayerPdc(event.getPlayer(), DAXI_FORTITUDE_KEY, true);
                DaxiAnimation.startAnimation(event.getPlayer(), DaxiAnimation.Type.FORTITUDE);
            }
        } else if (event.getItem().isSimilar(RebarTranscEndenceItems.DAXI_SATURATION)) {
            if (!getPlayerPdc(event.getPlayer(), DAXI_SATURATION_KEY)) {
                event.getItem().subtract();
                setPlayerPdc(event.getPlayer(), DAXI_SATURATION_KEY, true);
                DaxiAnimation.startAnimation(event.getPlayer(), DaxiAnimation.Type.SATURATION);
            }
        } else if (event.getItem().isSimilar(RebarTranscEndenceItems.DAXI_REGENERATION)) {
            if (!getPlayerPdc(event.getPlayer(), DAXI_REGENERATION_KEY)) {
                event.getItem().subtract();
                setPlayerPdc(event.getPlayer(), DAXI_REGENERATION_KEY, true);
                DaxiAnimation.startAnimation(event.getPlayer(), DaxiAnimation.Type.REGENERATION);
            }
        }
    }
    @EventHandler
    public void onEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (event.getCause() == EntityPotionEffectEvent.Cause.EXPIRATION 
                || event.getCause() == EntityPotionEffectEvent.Cause.MILK) {
            if (getPlayerPdc(player, DAXI_STRENGTH_KEY)) {
                Bukkit.getScheduler().runTask(RebarTranscEndence.getInstance(), () -> {
                    player.addPotionEffect(
                        new PotionEffect(PotionEffectType.STRENGTH, PotionEffect.INFINITE_DURATION, strength - 1));
                });
            }
            if (getPlayerPdc(player, DAXI_ABSORPTION_KEY)) {
                Bukkit.getScheduler().runTask(RebarTranscEndence.getInstance(), () -> {
                    player.addPotionEffect(
                        new PotionEffect(PotionEffectType.ABSORPTION, PotionEffect.INFINITE_DURATION, absorption - 1));
                });
            }
            if (getPlayerPdc(player, DAXI_FORTITUDE_KEY)) {
                Bukkit.getScheduler().runTask(RebarTranscEndence.getInstance(), () -> {
                    player.addPotionEffect(
                        new PotionEffect(PotionEffectType.RESISTANCE, PotionEffect.INFINITE_DURATION, fortitude - 1));
                });
            }
            if (getPlayerPdc(player, DAXI_SATURATION_KEY)) {
                Bukkit.getScheduler().runTask(RebarTranscEndence.getInstance(), () -> {
                    player.addPotionEffect(
                        new PotionEffect(PotionEffectType.SATURATION, PotionEffect.INFINITE_DURATION, saturation - 1));
                });
            }
            if (getPlayerPdc(player, DAXI_REGENERATION_KEY)) {
                Bukkit.getScheduler().runTask(RebarTranscEndence.getInstance(), () -> {
                    player.addPotionEffect(
                        new PotionEffect(PotionEffectType.REGENERATION, PotionEffect.INFINITE_DURATION, regeneration - 1));
                });
            }
        }
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) { //give up thinking, it just works
        int drop_amount = 0;
        if (getPlayerPdc(event.getPlayer(), DAXI_STRENGTH_KEY)) {
            setPlayerPdc(event.getPlayer(), DAXI_STRENGTH_KEY, false);
            drop_amount += 8;
        }
        if (getPlayerPdc(event.getPlayer(), DAXI_ABSORPTION_KEY)) {
            setPlayerPdc(event.getPlayer(), DAXI_ABSORPTION_KEY, false);
            drop_amount += 8;
        }
        if (getPlayerPdc(event.getPlayer(), DAXI_FORTITUDE_KEY)) {
            setPlayerPdc(event.getPlayer(), DAXI_FORTITUDE_KEY, false);
            drop_amount += 8;
        }
        if (getPlayerPdc(event.getPlayer(), DAXI_SATURATION_KEY)) {
            setPlayerPdc(event.getPlayer(), DAXI_SATURATION_KEY, false);
            drop_amount += 8;
        }
        if (getPlayerPdc(event.getPlayer(), DAXI_REGENERATION_KEY)) {
            setPlayerPdc(event.getPlayer(), DAXI_REGENERATION_KEY, false);
            drop_amount += 8;
        }
        if (drop_amount != 0) {
            ItemStack dropItemStack = RebarTranscEndenceItems.STABLE_BLOCK.clone();
            dropItemStack.setAmount(drop_amount);
            event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), dropItemStack);   
        }
    }
    public boolean getPlayerPdc(Player player, NamespacedKey namespacedKey) {
        return player.getPersistentDataContainer().getOrDefault(namespacedKey, RebarSerializers.BOOLEAN, false);
    }
    public void setPlayerPdc(Player player, NamespacedKey namespacedKey, boolean bool) {
        player.getPersistentDataContainer().set(namespacedKey, RebarSerializers.BOOLEAN, bool);
    }
}
