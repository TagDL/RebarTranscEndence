package io.github.tagdl.RebarTranscEndence;

import io.github.pylonmc.rebar.content.guide.RebarGuide;
import io.github.pylonmc.rebar.guide.pages.base.SimpleStaticGuidePage;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
public class RebarTranscEndencePages {
    public static final SimpleStaticGuidePage TRANSC_ENDENCE = new SimpleStaticGuidePage(new NamespacedKey(RebarTranscEndence.getInstance(), "transcendence_guide"));
    public static final SimpleStaticGuidePage ITEMS = new SimpleStaticGuidePage(new NamespacedKey(RebarTranscEndence.getInstance(), "transcendence_items"));
    public static final SimpleStaticGuidePage MACHINES = new SimpleStaticGuidePage(new NamespacedKey(RebarTranscEndence.getInstance(), "transcendence_machines"));
    public static void initialise(){
        RebarGuide.getRootPage().addPage(ItemStack.of(Material.PURPUR_BLOCK), TRANSC_ENDENCE);
        TRANSC_ENDENCE.addPage(RebarTranscEndenceItems.ZOT_UP, ITEMS);
        TRANSC_ENDENCE.addPage(ItemStack.of(Material.PURPUR_BLOCK), MACHINES);
    }
}
