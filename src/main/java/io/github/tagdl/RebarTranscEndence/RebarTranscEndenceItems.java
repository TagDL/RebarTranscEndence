package io.github.tagdl.RebarTranscEndence;

import io.github.pylonmc.rebar.content.guide.RebarGuide;
import io.github.pylonmc.rebar.guide.button.MachineRecipesButton;
import io.github.pylonmc.rebar.item.RebarItem;
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder;
import io.github.tagdl.RebarTranscEndence.blocks.Stabilizer;
import io.github.tagdl.RebarTranscEndence.blocks.QuirpOscillator;
import io.github.tagdl.RebarTranscEndence.blocks.QuirpAnnihilator;
import io.github.tagdl.RebarTranscEndence.blocks.ZotOverloader;
import io.github.tagdl.RebarTranscEndence.blocks.QuirpCycler;
import io.github.tagdl.RebarTranscEndence.items.UnstableIngot;
import io.github.tagdl.RebarTranscEndence.items.ZotDown;
import io.github.tagdl.RebarTranscEndence.items.ZotLeft;
import io.github.tagdl.RebarTranscEndence.items.ZotRight;
import io.github.tagdl.RebarTranscEndence.items.ZotUp;
import io.github.tagdl.RebarTranscEndence.items.Daxi.DaxiA;
import io.github.tagdl.RebarTranscEndence.items.Daxi.DaxiF;
import io.github.tagdl.RebarTranscEndence.items.Daxi.DaxiH;
import io.github.tagdl.RebarTranscEndence.items.Daxi.DaxiR;
import io.github.tagdl.RebarTranscEndence.items.Daxi.DaxiS;
import io.github.tagdl.RebarTranscEndence.recipe.NanobotCrafterRecipe;
import io.github.tagdl.RebarTranscEndence.recipe.QuirpCyclerRecipe;
import io.papermc.paper.datacomponent.DataComponentTypes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class RebarTranscEndenceItems {
    public static final ItemStack QUIRP_UP = ItemStackBuilder.rebar(Material.RED_CONCRETE, RebarTranscEndenceKeys.QUIRP_UP)
            .build();
    public static final ItemStack QUIRP_DOWN = ItemStackBuilder.rebar(Material.YELLOW_CONCRETE, RebarTranscEndenceKeys.QUIRP_DOWN)
            .build();
    public static final ItemStack QUIRP_LEFT = ItemStackBuilder.rebar(Material.LIME_CONCRETE, RebarTranscEndenceKeys.QUIRP_LEFT)
            .build();
    public static final ItemStack QUIRP_RIGHT = ItemStackBuilder.rebar(Material.BLUE_CONCRETE, RebarTranscEndenceKeys.QUIRP_RIGHT)
            .build();
    public static final ItemStack ZOT_UP = ItemStackBuilder.rebar(Material.RED_CONCRETE, RebarTranscEndenceKeys.ZOT_UP)
                .set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack ZOT_DOWN = ItemStackBuilder.rebar(Material.YELLOW_CONCRETE, RebarTranscEndenceKeys.ZOT_DOWN)
                .set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack ZOT_LEFT = ItemStackBuilder.rebar(Material.LIME_CONCRETE, RebarTranscEndenceKeys.ZOT_LEFT)
                .set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack ZOT_RIGHT = ItemStackBuilder.rebar(Material.BLUE_CONCRETE, RebarTranscEndenceKeys.ZOT_RIGHT)
                .set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack ZOT_UP_2 = ItemStackBuilder.rebar(Material.RED_GLAZED_TERRACOTTA, RebarTranscEndenceKeys.ZOT_UP_2)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack ZOT_DOWN_2 = ItemStackBuilder.rebar(Material.YELLOW_GLAZED_TERRACOTTA, RebarTranscEndenceKeys.ZOT_DOWN_2)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack ZOT_LEFT_2 = ItemStackBuilder.rebar(Material.LIME_GLAZED_TERRACOTTA, RebarTranscEndenceKeys.ZOT_LEFT_2)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack ZOT_RIGHT_2 = ItemStackBuilder.rebar(Material.BLUE_GLAZED_TERRACOTTA, RebarTranscEndenceKeys.ZOT_RIGHT_2)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack UNSTABLE_INGOT = ItemStackBuilder.rebar(Material.NETHER_BRICK, RebarTranscEndenceKeys.UNSTABLE_INGOT)
            .build();
    public static final ItemStack STABLE_INGOT = ItemStackBuilder.rebar(Material.BRICK, RebarTranscEndenceKeys.STABLE_INGOT)
            .build();
    public static final ItemStack STABLE_BLOCK = ItemStackBuilder.rebar(Material.BRICKS, RebarTranscEndenceKeys.STABLE_BLOCK)
            .build();
    public static final ItemStack QUIRP_CONDENSATE = ItemStackBuilder.rebar(Material.GRAY_GLAZED_TERRACOTTA, RebarTranscEndenceKeys.QUIRP_CONDENSATE)
            .build();
    public static final ItemStack VERTICAL_POLARIZER = ItemStackBuilder.rebar(Material.END_ROD, RebarTranscEndenceKeys.VERTICAL_POLARIZER)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack HORIZONTAL_POLARIZER = ItemStackBuilder.rebar(Material.END_ROD, RebarTranscEndenceKeys.HORIZONTAL_POLARIZER)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack DAXI_STRENGTH = ItemStackBuilder.rebar(Material.WHITE_GLAZED_TERRACOTTA, RebarTranscEndenceKeys.DAXI_STRENGTH)
                .set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack DAXI_ABSORPTION = ItemStackBuilder.rebar(Material.WHITE_GLAZED_TERRACOTTA, RebarTranscEndenceKeys.DAXI_ABSORPTION)
                .set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack DAXI_FORTITUDE = ItemStackBuilder.rebar(Material.WHITE_GLAZED_TERRACOTTA, RebarTranscEndenceKeys.DAXI_FORTITUDE)
                .set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack DAXI_SATURATION = ItemStackBuilder.rebar(Material.WHITE_GLAZED_TERRACOTTA, RebarTranscEndenceKeys.DAXI_SATURATION)
                .set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack DAXI_REGENERATION = ItemStackBuilder.rebar(Material.WHITE_GLAZED_TERRACOTTA, RebarTranscEndenceKeys.DAXI_REGENERATION)
                .set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();

    public static final ItemStack QUIRP_OSCILLATOR = ItemStackBuilder.rebar(Material.PURPUR_PILLAR, RebarTranscEndenceKeys.QUIRP_OSCILLATOR)
            .build();
    public static final ItemStack QUIRP_ANNIHILATOR = ItemStackBuilder.rebar(Material.YELLOW_CONCRETE, RebarTranscEndenceKeys.QUIRP_ANNIHILATOR)
            .build();
    public static final ItemStack QUIRP_CYCLER = ItemStackBuilder.rebar(Material.BLUE_CONCRETE, RebarTranscEndenceKeys.QUIRP_CYCLER)
            .build();
    public static final ItemStack STABILIZER = ItemStackBuilder.rebar(Material.BLACK_CONCRETE, RebarTranscEndenceKeys.STABILIZER)
            .build();
    public static final ItemStack ZOT_OVERLOADER = ItemStackBuilder.rebar(Material.WHITE_CONCRETE, RebarTranscEndenceKeys.ZOT_OVERLOADER)
            .build();
    public static final ItemStack NANOBOT_CRAFTER = ItemStackBuilder.rebar(Material.DISPENSER, RebarTranscEndenceKeys.NANOBOT_CRAFTER)
            .build();
    public static final ItemStack NANOBOT_LAUNCHER = ItemStackBuilder.rebar(Material.CRAFTING_TABLE, RebarTranscEndenceKeys.NANOBOT_LAUNCHER)
            .build();
    public static final ItemStack NANOBOT_OUTPUT_HATCH = ItemStackBuilder.rebar(Material.PURPUR_BLOCK, RebarTranscEndenceKeys.NANOBOT_OUTPUT_HATCH)
            .build();
    public static void initialize() {

        RebarItem.register(RebarItem.class, QUIRP_UP);
        RebarTranscEndencePages.ITEMS.addItem(QUIRP_UP);
        RebarItem.register(RebarItem.class, QUIRP_DOWN);
        RebarTranscEndencePages.ITEMS.addItem(QUIRP_DOWN);
        RebarItem.register(RebarItem.class, QUIRP_LEFT);
        RebarTranscEndencePages.ITEMS.addItem(QUIRP_LEFT);
        RebarItem.register(RebarItem.class, QUIRP_RIGHT);
        RebarTranscEndencePages.ITEMS.addItem(QUIRP_RIGHT);

        RebarItem.register(ZotUp.class, ZOT_UP);
        RebarTranscEndencePages.ITEMS.addItem(ZOT_UP);
        RebarItem.register(ZotDown.class, ZOT_DOWN);
        RebarTranscEndencePages.ITEMS.addItem(ZOT_DOWN);
        RebarItem.register(ZotLeft.class, ZOT_LEFT);
        RebarTranscEndencePages.ITEMS.addItem(ZOT_LEFT);
        RebarItem.register(ZotRight.class, ZOT_RIGHT);
        RebarTranscEndencePages.ITEMS.addItem(ZOT_RIGHT);

        RebarItem.register(RebarItem.class, ZOT_UP_2);
        RebarTranscEndencePages.ITEMS.addItem(ZOT_UP_2);
        RebarItem.register(RebarItem.class, ZOT_DOWN_2);
        RebarTranscEndencePages.ITEMS.addItem(ZOT_DOWN_2);
        RebarItem.register(RebarItem.class, ZOT_LEFT_2);
        RebarTranscEndencePages.ITEMS.addItem(ZOT_LEFT_2);
        RebarItem.register(RebarItem.class, ZOT_RIGHT_2);
        RebarTranscEndencePages.ITEMS.addItem(ZOT_RIGHT_2);

        RebarItem.register(UnstableIngot.class, UNSTABLE_INGOT);
        RebarTranscEndencePages.ITEMS.addItem(UNSTABLE_INGOT);
        RebarItem.register(RebarItem.class, STABLE_INGOT);
        RebarTranscEndencePages.ITEMS.addItem(STABLE_INGOT);
        RebarItem.register(RebarItem.class, STABLE_BLOCK);
        RebarTranscEndencePages.ITEMS.addItem(STABLE_BLOCK);

        RebarItem.register(RebarItem.class, QUIRP_CONDENSATE);
        RebarTranscEndencePages.ITEMS.addItem(QUIRP_CONDENSATE);

        RebarItem.register(RebarItem.class, VERTICAL_POLARIZER);
        RebarTranscEndencePages.ITEMS.addItem(VERTICAL_POLARIZER);
        RebarItem.register(RebarItem.class, HORIZONTAL_POLARIZER);
        RebarTranscEndencePages.ITEMS.addItem(HORIZONTAL_POLARIZER);

        RebarItem.register(DaxiS.class, DAXI_STRENGTH);
        RebarTranscEndencePages.ITEMS.addItem(DAXI_STRENGTH);
        RebarItem.register(DaxiA.class, DAXI_ABSORPTION);
        RebarTranscEndencePages.ITEMS.addItem(DAXI_ABSORPTION);
        RebarItem.register(DaxiF.class, DAXI_FORTITUDE);
        RebarTranscEndencePages.ITEMS.addItem(DAXI_FORTITUDE);
        RebarItem.register(DaxiH.class, DAXI_SATURATION);
        RebarTranscEndencePages.ITEMS.addItem(DAXI_SATURATION);
        RebarItem.register(DaxiR.class, DAXI_REGENERATION);
        RebarTranscEndencePages.ITEMS.addItem(DAXI_REGENERATION);


        RebarItem.register(QuirpOscillator.Item.class, QUIRP_OSCILLATOR, RebarTranscEndenceKeys.QUIRP_OSCILLATOR);
        RebarTranscEndencePages.MACHINES.addItem(QUIRP_OSCILLATOR);
        RebarItem.register(QuirpAnnihilator.Item.class, QUIRP_ANNIHILATOR, RebarTranscEndenceKeys.QUIRP_ANNIHILATOR);
        RebarTranscEndencePages.MACHINES.addItem(QUIRP_ANNIHILATOR);
        RebarItem.register(QuirpCycler.Item.class, QUIRP_CYCLER, RebarTranscEndenceKeys.QUIRP_CYCLER);
        RebarTranscEndencePages.MACHINES.addItem(QUIRP_CYCLER);
        RebarGuide.getOrCreateInfoPage(RebarTranscEndenceKeys.QUIRP_CYCLER)
                .addButton(new MachineRecipesButton(QuirpCyclerRecipe.RECIPE_TYPE));
        RebarItem.register(Stabilizer.Item.class, STABILIZER, RebarTranscEndenceKeys.STABILIZER);
        RebarTranscEndencePages.MACHINES.addItem(STABILIZER);
        RebarItem.register(ZotOverloader.Item.class, ZOT_OVERLOADER, RebarTranscEndenceKeys.ZOT_OVERLOADER);
        RebarTranscEndencePages.MACHINES.addItem(ZOT_OVERLOADER);
        RebarItem.register(RebarItem.class, NANOBOT_CRAFTER, RebarTranscEndenceKeys.NANOBOT_CRAFTER);
        RebarTranscEndencePages.MACHINES.addItem(NANOBOT_CRAFTER);
        RebarGuide.getOrCreateInfoPage(RebarTranscEndenceKeys.NANOBOT_CRAFTER)
                .addButton(new MachineRecipesButton(NanobotCrafterRecipe.RECIPE_TYPE));
        RebarItem.register(RebarItem.class, NANOBOT_LAUNCHER, RebarTranscEndenceKeys.NANOBOT_LAUNCHER);
        RebarTranscEndencePages.MACHINES.addItem(NANOBOT_LAUNCHER);
        RebarItem.register(RebarItem.class, NANOBOT_OUTPUT_HATCH, RebarTranscEndenceKeys.NANOBOT_OUTPUT_HATCH);
        RebarTranscEndencePages.MACHINES.addItem(NANOBOT_OUTPUT_HATCH);
    }
}
