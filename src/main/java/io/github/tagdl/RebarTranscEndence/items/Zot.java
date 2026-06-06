package io.github.tagdl.RebarTranscEndence.items;

import io.github.pylonmc.rebar.config.ConfigSection;
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter;
import io.github.pylonmc.rebar.datatypes.RebarSerializers;
import io.github.pylonmc.rebar.i18n.RebarArgument;
import io.github.pylonmc.rebar.item.RebarItem;
import io.github.tagdl.RebarTranscEndence.RebarTranscEndence;

import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;


public class Zot extends RebarItem {
    private static final NamespacedKey AMOUNT_KEY = new NamespacedKey(RebarTranscEndence.getInstance(), "zot_amount");
    private static final ConfigSection settings = ConfigSection.fromSettings(new NamespacedKey(RebarTranscEndence.getInstance(), "zot"));
    public final int maxAmount = settings.getOrThrow("max-amount", ConfigAdapter.INTEGER);
    public Zot(@NotNull ItemStack stack) {
        super(stack);
    }
    public void setAmount(int amount) {
        getStack().editPersistentDataContainer(pdc -> pdc.set(AMOUNT_KEY, RebarSerializers.INTEGER, amount));
    }
    public int getAmount() {
        return getStack().getPersistentDataContainer().getOrDefault(AMOUNT_KEY, PersistentDataType.INTEGER, 0);
    }
    public boolean isSimilar(Zot zot) {
        zot.setAmount(0);
        setAmount(0);
        return zot.getStack().isSimilar(getStack());
    }
    @Override
    public @NotNull List<RebarArgument> getPlaceholders() {
        return List.of(
                RebarArgument.of("amount", getAmount()),
                RebarArgument.of("max", maxAmount)
        );
    }
}
