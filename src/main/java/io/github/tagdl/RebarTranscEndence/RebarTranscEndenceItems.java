package io.github.tagdl.RebarTranscEndence;

import io.github.pylonmc.rebar.content.guide.RebarGuide;
import io.github.pylonmc.rebar.guide.button.MachineRecipesButton;
import io.github.pylonmc.rebar.item.RebarItem;
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder;
import io.github.tagdl.RebarTranscEndence.items.UnstableIngot;
import io.github.tagdl.RebarTranscEndence.items.Zot_2;
import io.github.tagdl.RebarTranscEndence.recipe.ZotReverserRecipe;
import io.papermc.paper.datacomponent.DataComponentTypes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public final class RebarTranscEndenceItems {

    public static final ItemStack ZOT_UP = ItemStackBuilder.rebar(Material.RED_DYE, RebarTranscEndenceKeys.ZOT_UP)
            .build();
    public static final ItemStack ZOT_DOWN = ItemStackBuilder.rebar(Material.YELLOW_DYE, RebarTranscEndenceKeys.ZOT_DOWN)
            .build();
    public static final ItemStack ZOT_LEFT = ItemStackBuilder.rebar(Material.GREEN_DYE, RebarTranscEndenceKeys.ZOT_LEFT)
            .build();
    public static final ItemStack ZOT_RIGHT = ItemStackBuilder.rebar(Material.BLUE_DYE, RebarTranscEndenceKeys.ZOT_RIGHT)
            .build();
    public static final ItemStack ZOT_UP_2 = ItemStackBuilder.rebar(Material.RED_DYE, RebarTranscEndenceKeys.ZOT_UP_2)
                .set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack ZOT_DOWN_2 = ItemStackBuilder.rebar(Material.YELLOW_DYE, RebarTranscEndenceKeys.ZOT_DOWN_2)
                .set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack ZOT_LEFT_2 = ItemStackBuilder.rebar(Material.GREEN_DYE, RebarTranscEndenceKeys.ZOT_LEFT_2)
                .set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack ZOT_RIGHT_2 = ItemStackBuilder.rebar(Material.BLUE_DYE, RebarTranscEndenceKeys.ZOT_RIGHT_2)
                .set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack UNSTABLE_INGOT = ItemStackBuilder.rebar(Material.NETHER_BRICK, RebarTranscEndenceKeys.UNSTABLE_INGOT)
            .build();
    public static final ItemStack STABLE_INGOT = ItemStackBuilder.rebar(Material.BRICK, RebarTranscEndenceKeys.STABLE_INGOT)
            .build();
    public static final ItemStack STABLE_INGOT_BLOCK = ItemStackBuilder.rebar(Material.BRICKS, RebarTranscEndenceKeys.STABLE_INGOT_BLOCK)
            .build();
    public static final ItemStack ZOT_COOL_DOWN = ItemStackBuilder.rebar(Material.GRAY_DYE, RebarTranscEndenceKeys.ZOT_COOL_DOWN)
            .build();
    public static final ItemStack VERTICAL_POLARIZER = ItemStackBuilder.rebar(Material.END_ROD, RebarTranscEndenceKeys.VERTICAL_POLARIZER)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack HORIZONTAL_POLARIZER = ItemStackBuilder.rebar(Material.END_ROD, RebarTranscEndenceKeys.HORIZONTAL_POLARIZER)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack DAXI_STRENGTH = ItemStackBuilder.rebar(Material.END_ROD, RebarTranscEndenceKeys.DAXI_STRENGTH)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack DAXI_ABSORPTION = ItemStackBuilder.rebar(Material.END_ROD, RebarTranscEndenceKeys.DAXI_ABSORPTION)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack DAXI_FORTITUDE = ItemStackBuilder.rebar(Material.END_ROD, RebarTranscEndenceKeys.DAXI_FORTITUDE)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack DAXI_SATURATION = ItemStackBuilder.rebar(Material.END_ROD, RebarTranscEndenceKeys.DAXI_SATURATION)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();
    public static final ItemStack DAXI_REGENERATION = ItemStackBuilder.rebar(Material.END_ROD, RebarTranscEndenceKeys.DAXI_REGENERATION)
                .set(DataComponentTypes.MAX_STACK_SIZE, 1)
            .build();

    public static final ItemStack ZOT_COLLECTOR = ItemStackBuilder.rebar(Material.PURPUR_BLOCK, RebarTranscEndenceKeys.ZOT_COLLECTOR)
            .build();
    public static final ItemStack ZOT_CONDENSER = ItemStackBuilder.rebar(Material.YELLOW_CONCRETE, RebarTranscEndenceKeys.ZOT_CONDENSER)
            .build();
    public static final ItemStack ZOT_REVERSER = ItemStackBuilder.rebar(Material.BLUE_CONCRETE, RebarTranscEndenceKeys.ZOT_REVERSER)
            .build();
    public static final ItemStack STABLE_MACHINE = ItemStackBuilder.rebar(Material.BLACK_CONCRETE, RebarTranscEndenceKeys.STABLE_MACHINE)
            .build();
    public static final ItemStack ZOT_OVERLOADER = ItemStackBuilder.rebar(Material.WHITE_CONCRETE, RebarTranscEndenceKeys.ZOT_OVERLOADER)
            .build();

    public static void initialize() {
        // Register an item using the ExampleItem class
        RebarItem.register(RebarItem.class, ZOT_UP);
        RebarTranscEndencePages.ITEMS.addItem(ZOT_UP);
        RebarItem.register(RebarItem.class, ZOT_DOWN);
        RebarTranscEndencePages.ITEMS.addItem(ZOT_DOWN);
        RebarItem.register(RebarItem.class, ZOT_LEFT);
        RebarTranscEndencePages.ITEMS.addItem(ZOT_LEFT);
        RebarItem.register(RebarItem.class, ZOT_RIGHT);
        RebarTranscEndencePages.ITEMS.addItem(ZOT_RIGHT);

        RebarItem.register(Zot_2.class, ZOT_UP_2);
        RebarTranscEndencePages.ITEMS.addItem(ZOT_UP_2);
        RebarItem.register(Zot_2.class, ZOT_DOWN_2);
        RebarTranscEndencePages.ITEMS.addItem(ZOT_DOWN_2);
        RebarItem.register(Zot_2.class, ZOT_LEFT_2);
        RebarTranscEndencePages.ITEMS.addItem(ZOT_LEFT_2);
        RebarItem.register(Zot_2.class, ZOT_RIGHT_2);

        RebarTranscEndencePages.ITEMS.addItem(UNSTABLE_INGOT);
        RebarItem.register(UnstableIngot.class, UNSTABLE_INGOT);
        RebarTranscEndencePages.ITEMS.addItem(STABLE_INGOT);
        RebarItem.register(RebarItem.class, STABLE_INGOT);
        RebarTranscEndencePages.ITEMS.addItem(STABLE_INGOT_BLOCK);
        RebarItem.register(RebarItem.class, STABLE_INGOT_BLOCK);

        RebarTranscEndencePages.ITEMS.addItem(ZOT_COOL_DOWN);
        RebarItem.register(RebarItem.class, ZOT_COOL_DOWN);

        RebarTranscEndencePages.ITEMS.addItem(VERTICAL_POLARIZER);
        RebarItem.register(RebarItem.class, VERTICAL_POLARIZER);
        RebarTranscEndencePages.ITEMS.addItem(HORIZONTAL_POLARIZER);
        RebarItem.register(RebarItem.class, HORIZONTAL_POLARIZER);

        RebarTranscEndencePages.ITEMS.addItem(DAXI_STRENGTH);
        RebarItem.register(RebarItem.class, DAXI_STRENGTH);
        RebarTranscEndencePages.ITEMS.addItem(DAXI_ABSORPTION);
        RebarItem.register(RebarItem.class, DAXI_ABSORPTION);
        RebarTranscEndencePages.ITEMS.addItem(DAXI_FORTITUDE);
        RebarItem.register(RebarItem.class, DAXI_FORTITUDE);
        RebarTranscEndencePages.ITEMS.addItem(DAXI_SATURATION);
        RebarItem.register(RebarItem.class, DAXI_SATURATION);
        RebarTranscEndencePages.ITEMS.addItem(DAXI_REGENERATION);
        RebarItem.register(RebarItem.class, DAXI_REGENERATION);

        // Register a 'normal' item which represents Example Block
        // Blocks and their corresponding item will almost always share the same key
        // Note the 3rd parameter - this is the key of the corresponding block registered in [RebarTranscEndenceBlocks]
        RebarItem.register(RebarItem.class, ZOT_COLLECTOR, RebarTranscEndenceKeys.ZOT_COLLECTOR);
        RebarTranscEndencePages.MACHINES.addItem(ZOT_COLLECTOR);
        RebarItem.register(RebarItem.class, ZOT_CONDENSER, RebarTranscEndenceKeys.ZOT_CONDENSER);
        RebarTranscEndencePages.MACHINES.addItem(ZOT_CONDENSER);
        RebarItem.register(RebarItem.class, ZOT_REVERSER, RebarTranscEndenceKeys.ZOT_REVERSER);
        RebarTranscEndencePages.MACHINES.addItem(ZOT_REVERSER);
        RebarGuide.getOrCreateInfoPage(RebarTranscEndenceKeys.ZOT_REVERSER)
                .addButton(new MachineRecipesButton(ZOT_REVERSER, ZotReverserRecipe.RECIPE_TYPE));
        RebarItem.register(RebarItem.class, STABLE_MACHINE, RebarTranscEndenceKeys.STABLE_MACHINE);
        RebarTranscEndencePages.MACHINES.addItem(STABLE_MACHINE);
        RebarItem.register(RebarItem.class, ZOT_OVERLOADER, RebarTranscEndenceKeys.ZOT_OVERLOADER);
        RebarTranscEndencePages.MACHINES.addItem(ZOT_OVERLOADER);
    }
}
