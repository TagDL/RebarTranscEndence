package io.github.tagdl.RebarTranscEndence.items;

import io.github.pylonmc.pylon.util.PylonUtils;
import io.github.pylonmc.rebar.datatypes.RebarSerializers;
import io.github.pylonmc.rebar.i18n.RebarArgument;
import io.github.pylonmc.rebar.item.RebarItem;
import io.github.pylonmc.rebar.util.gui.unit.UnitFormat;

import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;


public class Zot_2 extends RebarItem {
    private static final NamespacedKey AMOUNT_KEY = PylonUtils.pylonKey("zot_2_amount");
    public Zot_2(@NotNull ItemStack stack) {
        super(stack);
    }
    public void setAmount(int amount) {
        getStack().editPersistentDataContainer(pdc -> pdc.set(AMOUNT_KEY, RebarSerializers.INTEGER, amount));
    }
    public int getAmount() {
        return getStack().getPersistentDataContainer().getOrDefault(AMOUNT_KEY, PersistentDataType.INTEGER, 0);
    }
    @Override
    public @NotNull List<RebarArgument> getPlaceholders() {
        return List.of(
                RebarArgument.of("amount", UnitFormat.MILLIBUCKETS.format(getAmount()))
        );
    }
}
