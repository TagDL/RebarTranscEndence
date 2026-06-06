package io.github.tagdl.RebarTranscEndence.items;

import io.github.pylonmc.rebar.config.adapter.ConfigAdapter;
import io.github.pylonmc.rebar.datatypes.RebarSerializers;
import io.github.pylonmc.rebar.i18n.RebarArgument;
import io.github.pylonmc.rebar.item.RebarItem;
import io.github.pylonmc.rebar.item.interfaces.InventoryTickerRebarItem;
import io.github.tagdl.RebarTranscEndence.RebarTranscEndence;
import lombok.Getter;
import net.kyori.adventure.text.Component;

import static io.github.pylonmc.pylon.util.PylonUtils.colorToTextColor;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;


public class UnstableIngot extends RebarItem implements
    InventoryTickerRebarItem
{
    @Getter
    private final long deleteDelay = getSettings().getOrThrow("max_hold_seconds", ConfigAdapter.INTEGER) * 20;
    private static final NamespacedKey DEGREE_KEY = new NamespacedKey(RebarTranscEndence.getInstance(), "unstable_ingot_degree");
    public UnstableIngot(@NotNull ItemStack stack) {
        super(stack);
    }
    public void setAmount(int amount) {
        getStack().editPersistentDataContainer(pdc -> pdc.set(DEGREE_KEY, RebarSerializers.INTEGER, amount));
    }
    public int getAmount() {
        return getStack().getPersistentDataContainer().getOrDefault(DEGREE_KEY, PersistentDataType.INTEGER, 100);
    }
    @Override
    public @NotNull List<RebarArgument> getPlaceholders() {
        return List.of(
                RebarArgument.of("degree", getAmount())
        );
    }
    @Override
    public void onTick(Player player){
        Bukkit.getScheduler().runTaskLater(RebarTranscEndence.getInstance(), () -> {
            if (!player.isOnline() || player.getGameMode() == GameMode.CREATIVE) return;
            if (!player.getInventory().containsAtLeast(getStack(), 1)) return;
            for (ItemStack itemStack : player.getInventory().getContents()) {
                if (itemStack == null) continue;
                if (itemStack.getType() == Material.AIR) continue;
                if (!itemStack.isSimilar(getStack())) continue;
                itemStack.setAmount(0);
                break;
            }
            Bukkit.getScheduler().runTask(RebarTranscEndence.getInstance(), () -> {
                player.kill();
                player.sendMessage(Component.text(player.getName()).color(colorToTextColor(Color.YELLOW))
                   .append(Component.translatable("rebartranscendence.message.unstable-death-message")
                            .color(colorToTextColor(Color.RED))));
            });
        }, deleteDelay);
    }
    @Override
    public long getBaseTickInterval(){
        return 1;
    }

}
