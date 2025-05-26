package net.masternation.wallet.gui;

import lombok.RequiredArgsConstructor;
import me.masterkaiser.bukkit.BukkitApp;
import me.masterkaiser.bukkit.api.item.ItemBuilder;
import me.masterkaiser.bukkit.api.item.ItemConverterService;
import me.masterkaiser.bukkit.gui.Gui;
import me.masterkaiser.util.MapBuilder;
import me.masterkaiser.util.RoundUtil;
import net.masternation.wallet.config.Config;
import net.masternation.wallet.config.Messages;
import net.masternation.wallet.offer.Offer;
import net.masternation.wallet.offer.OfferService;
import net.masternation.wallet.user.UserService;
import net.masternation.wallet.util.FormatUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MountableGui {

    private final BukkitApp bukkitApp;
    private final UserService userService;
    private final Config config;
    private final OfferConfirmGui offerConfirmGui;
    private final Messages messages;
    private final ProcessingGui processingGui;
    private final OfferService offerService;

    private final int MAX_AMOUNT = 64;
    private final int MIN_AMOUNT = 1;

    public CompletableFuture<Gui> build(@NotNull Player player, @NotNull Offer offer) {
        CompletableFuture<Gui> future = new CompletableFuture<>();
        AtomicInteger amount = new AtomicInteger(1);

        Gui gui = Gui.create(this.bukkitApp, this.config.quantitySelectionGui, true, false, null);

        this.userService.getWallet(player.getUniqueId()).thenAccept(pln -> {
            if (pln == null) return;

            updateAmount(gui, amount.get(), pln, offer);
            setupModificationButtons(gui, amount, pln, offer);
            setupStaticButtons(gui, offer);
            setupConfirmButton(gui, offer, amount, pln);

            future.complete(gui);
        });

        return future;
    }

    public Map<String, Integer> buttons = new MapBuilder<String, Integer>()
            .put("set-default", 1)
            .put("remove-1", -1)
            .put("remove-2", -2)
            .put("remove-3", -3)
            .put("remove-5", -5)
            .put("remove-10", -10)
            .put("add-1", 1)
            .put("add-2", 2)
            .put("add-3", 3)
            .put("add-5", 5)
            .put("add-10", 10)
            .build();

    private void setupModificationButtons(Gui gui, AtomicInteger amount, double pln, Offer offer) {
        this.buttons.forEach((key, value) -> {
            Optional.ofNullable(gui.getGuiBuilder().getStateItems().get(key)).ifPresent(itemSlot -> {
                gui.setItem(itemSlot.getSlot(), itemSlot.getItemBuilder().build(), event -> {
                    int newValue = key.startsWith("set") ? value : amount.get() + value;

                    if (newValue < MIN_AMOUNT) {
                        return;
                    }

                    if (newValue > MAX_AMOUNT) {
                        return;
                    }

                    amount.set(newValue);
                    updateAmount(gui, amount.get(), pln, offer);
                });
            });
        });
    }

    private void setupStaticButtons(Gui gui, Offer offer) {
        Optional.ofNullable(gui.getGuiBuilder().getActions().get("item")).ifPresent(slot ->
                gui.setItem(slot, offer.getIcon().build()));
    }

    private void setupConfirmButton(Gui gui, Offer offer, AtomicInteger amount, double pln) {
        Optional.ofNullable(gui.getGuiBuilder().getStateItems().get("confirm")).ifPresent(itemSlot -> {
            gui.getGuiEvents().updateClickEvent(itemSlot.getSlot(), event -> {
                Player clicker = (Player) event.getWhoClicked();
                if (!this.userService.canDoAction(clicker.getUniqueId())) return;

                double cost = this.offerService.getCost(offer, amount.get());
                if (pln < cost) {
                    this.messages.insufficientFunds.send(clicker);
                    return;
                }

                this.processingGui.open(clicker);
                this.offerConfirmGui.build(clicker, offer, amount.get()).thenAccept(gui1 -> {
                    if (clicker.isOnline()) {
                        gui1.open(clicker);
                    } else {
                        gui1.dispose(true);
                    }
                });
            });
        });
    }

    public void updateAmount(@NotNull Gui gui, int amount, double pln, Offer offer) {
        Optional.ofNullable(gui.getGuiBuilder().getStateItems().get("confirm")).ifPresent(itemSlot -> {
            ItemBuilder itemBuilder = ItemConverterService.itemConverter.fromItemStack(itemSlot.getItemBuilder().build());
            itemBuilder.setAmount(amount);

            gui.setItem(itemSlot.getSlot(), itemBuilder.build(meta -> meta
                    .replace("{pln}", FormatUtil.formatNumber(RoundUtil.roundToDecimalPlaces(pln, 2)))
                    .replace("{cost}", this.offerService.getCostString(offer, amount))
                    .replace("{amount}", String.valueOf(amount))));
        });
    }
}
