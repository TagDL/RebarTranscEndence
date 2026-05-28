package io.github.tagdl.RebarTranscEndence.daxi;

import static io.github.pylonmc.pylon.util.PylonUtils.colorToTextColor;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;

import io.github.pylonmc.rebar.config.Config;
import io.github.pylonmc.rebar.config.Settings;
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter;
import io.github.pylonmc.rebar.entity.display.ItemDisplayBuilder;
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder;
import io.github.tagdl.RebarTranscEndence.RebarTranscEndence;
import net.kyori.adventure.text.Component;

public class DaxiAnimation {
        private static final Config settings = Settings.get(new NamespacedKey(RebarTranscEndence.getInstance(), "daxi"));
        public static void startAnimation(Player player, Type type) {
            player.sendMessage(Component.translatable("rebartranscendence.message.daxi-message-intro")
                    .color(colorToTextColor(Color.PURPLE)));
            Location l = player.getLocation();
            int lasttick = 105;
            Location locas1 = l.clone().add(2, -0.5, 0);
            Location locas2 = l.clone().add(0, -0.5, 2);
            Location locas3 = l.clone().add(-2, -0.5, 0);
            Location locas4 = l.clone().add(0, -0.5, -2);
            Material[] concreteTypes = type.materials;
            ItemDisplay[] displays = new ItemDisplay[4];
            displays[0] = createDisplay(locas1, concreteTypes[0]);
            displays[1] = createDisplay(locas2, concreteTypes[1]);
            displays[2] = createDisplay(locas3, concreteTypes[2]);
            displays[3] = createDisplay(locas4, concreteTypes[3]);
            Vector vas1 = locas1.toVector();
            Vector vas2 = locas2.toVector();
            Vector vas3 = locas3.toVector();
            Vector vas4 = locas4.toVector();
            Vector[] displayLocations = {vas1, vas2, vas3, vas4};
            final Location centerLoc = l.clone();
            for (int i = 0; i < lasttick; i++) {
                Bukkit.getScheduler().runTaskLater(RebarTranscEndence.getInstance(), () -> moveDisplays(displays, displayLocations, centerLoc), i);
            }
            ThreadLocalRandom random = ThreadLocalRandom.current();
            Bukkit.getScheduler().runTaskLater(RebarTranscEndence.getInstance(), () -> {
                for (ItemDisplay display : displays) {
                    if (display != null && display.isValid()) {
                        display.remove();
                    }
                }
                for (Color color : type.colors) {
                    for (int i = 0; i < 25; i++) {
                        float xRand = (random.nextFloat() - 0.5F) * 3.2F;
                        float yRand = (random.nextFloat() - 0.5F) * 3.2F;
                        float zRand = (random.nextFloat() - 0.5F) * 3.2F;

                        player.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION,
                            l.getX() + (double) xRand,
                            l.getY() + 2.0D + (double) yRand,
                            l.getZ() + (double) zRand,
                            i,
                            new Particle.DustTransition(color, color,1));
                    }
                }
                player.getWorld().playSound(l, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1F, 1F);
                player.addPotionEffect(
                    new PotionEffect(type.effect, PotionEffect.INFINITE_DURATION, type.level - 1));
                player.sendMessage(type.component);
            }, lasttick);
        }
        private static ItemDisplay createDisplay(Location spawnLocation, Material material) {
            return new ItemDisplayBuilder()
                .transformation(new Matrix4f().scale(0.5f, 0.5f, 0.5f))
                .itemStack(ItemStackBuilder.of(material).build()) 
                .build(spawnLocation);
        }
        private static void moveDisplays(ItemDisplay[] displays, Vector[] asv, Location centerLoc) {
            double rotateAngle = 100; 
            double heightSpeed = 0.02;     
            double radiusExpand = 1.0;     

            for (int i = 0; i < 4; i++) {
                if (displays[i] == null || !displays[i].isValid()) continue;
                Vector relative = asv[i].clone().subtract(centerLoc.toVector());
                double currentY = relative.getY();
                relative.setY(0); 
                Vector nextRelativeXZ = relative.rotateAroundY(Math.toRadians(rotateAngle)).multiply(radiusExpand);
                double nextY = currentY + heightSpeed;
                Vector nextRelative = nextRelativeXZ.setY(nextY);
                Vector v = nextRelative.clone().subtract(asv[i].clone().subtract(centerLoc.toVector()));
                Location currentLoc = displays[i].getLocation();
                displays[i].teleport(currentLoc.add(v));
                asv[i] = centerLoc.toVector().add(nextRelative);
            }
        }
    
    public enum Type {
        STRENGTH(new Material[]{Material.RED_CONCRETE, Material.RED_CONCRETE, Material.RED_CONCRETE, Material.RED_CONCRETE}, 
            PotionEffectType.STRENGTH, settings.getOrThrow("strength-level", ConfigAdapter.INTEGER),
            Component.translatable("rebartranscendence.message.daxi-message-strength").color(colorToTextColor(Color.RED)),
            new Color[]{Color.RED, Color.RED, Color.RED, Color.RED}
        ),
        ABSORPTION(new Material[]{Material.YELLOW_CONCRETE, Material.YELLOW_CONCRETE, Material.YELLOW_CONCRETE, Material.YELLOW_CONCRETE},
            PotionEffectType.ABSORPTION, settings.getOrThrow("absorption-level", ConfigAdapter.INTEGER),
            Component.translatable("rebartranscendence.message.daxi-message-absorption").color(colorToTextColor(Color.YELLOW)),
            new Color[]{Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW}
        ),
        FORTITUDE(new Material[]{Material.LIME_CONCRETE, Material.LIME_CONCRETE, Material.LIME_CONCRETE, Material.LIME_CONCRETE},
            PotionEffectType.RESISTANCE, settings.getOrThrow("resistance-level", ConfigAdapter.INTEGER),
            Component.translatable("rebartranscendence.message.daxi-message-fortitude").color(colorToTextColor(Color.LIME)),
            new Color[]{Color.LIME, Color.LIME, Color.LIME, Color.LIME}
        ),
        SATURATION(new Material[]{Material.BLUE_CONCRETE, Material.BLUE_CONCRETE, Material.BLUE_CONCRETE, Material.BLUE_CONCRETE},
            PotionEffectType.SATURATION, settings.getOrThrow("saturation-level", ConfigAdapter.INTEGER),
            Component.translatable("rebartranscendence.message.daxi-message-saturation").color(colorToTextColor(Color.BLUE)),
            new Color[]{Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE}
        ),
        REGENERATION(new Material[]{Material.RED_CONCRETE, Material.YELLOW_CONCRETE, Material.LIME_CONCRETE, Material.BLUE_CONCRETE},
            PotionEffectType.REGENERATION, settings.getOrThrow("regeneration-level", ConfigAdapter.INTEGER),
            Component.translatable("rebartranscendence.message.daxi-message-regeneration").color(colorToTextColor(Color.ORANGE)),
            new Color[]{Color.RED, Color.YELLOW, Color.LIME, Color.BLUE}
        );
        private final Material[] materials;
        private final PotionEffectType effect;
        private final int level;
        private final Component component;
        private final Color[] colors;
        Type (Material[] materials, PotionEffectType effect, int level, Component component, Color[] colors){
            this.materials = materials;
            this.effect = effect;
            this.level = level;
            this.component = component;
            this.colors = colors;
        }
        public int getLevel() {
            return this.level;
        }
    }
}
