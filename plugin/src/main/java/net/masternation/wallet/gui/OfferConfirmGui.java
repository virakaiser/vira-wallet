package net.masternation.wallet.gui;

import lombok.RequiredArgsConstructor;
import me.masterkaiser.bukkit.BukkitApp;
import me.masterkaiser.bukkit.gui.Gui;
import me.masterkaiser.util.RoundUtil;
import net.masternation.wallet.config.Config;
import net.masternation.wallet.config.Messages;
import net.masternation.wallet.offer.Offer;
import net.masternation.wallet.offer.OfferService;
import net.masternation.wallet.user.UserService;
import net.masternation.wallet.util.FormatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OfferConfirmGui {
    
    private final BukkitApp bukkitApp;
    private final Config config;
    private final UserService userService;
    private final Messages messages;
    private final OfferService offerService;

    public CompletableFuture<Gui> build(@NotNull Player object, @NotNull Offer link, @NotNull Integer value) {
        CompletableFuture<Gui> completableFuture = new CompletableFuture<>();
        Gui gui = Gui.create(this.bukkitApp, this.config.purchaseConfirmationGui, true, false, null);

        Optional.ofNullable(this.config.purchaseConfirmationGui.getStateItems().get("refuse")).ifPresent(itemSlot -> {
            gui.setItem(itemSlot.getSlot(), itemSlot.getItemBuilder().build(), event -> {
                event.getWhoClicked().closeInventory();
            });
        });

        this.userService.getWallet(object.getUniqueId()).thenAccept(pln -> {
            if (pln == null) {
                return;
            }

            Optional.ofNullable(this.config.purchaseConfirmationGui.getStateItems().get("confirm")).ifPresent(itemSlot -> {
                gui.setItem(itemSlot.getSlot(), itemSlot.getItemBuilder().build(m -> m
                                .replace("{pln}", FormatUtil.formatNumber(RoundUtil.roundToDecimalPlaces(pln, 2)))
                                .replace("{amount}", String.valueOf(value))
                                .replace("{cost}", offerService.getCostString(link, value))
                ), event -> {
                    Player player = (Player) event.getWhoClicked();
                    if (!this.userService.canDoAction(player.getUniqueId())) {
                        return;
                    }

                    this.messages.transactionInProgress.send(player);
                    player.closeInventory();

                    double cost = offerService.getCost(link, value);
                    this.userService.hasWallet(player.getUniqueId(), cost).thenAccept(a -> {
                        if (!a) {
                            this.messages.insufficientFunds.send(player);
                            return;
                        }

                        this.messages.transactionSuccessMessage.send(player);
                        this.userService.minusWallet(player.getUniqueId(), cost);
                        link.giveToUser(player, value);

                        if (link.isMountable()) {
                            this.messages.mountablePurchaseMessage.send(Bukkit.getOnlinePlayers(), m -> {
                                return m.replace("{player}", player.getName())
                                        .replace("{amount}", String.valueOf(value))
                                        .replace("{item}", link.getName());
                            });
                        } else {
                            this.messages.purchaseMessage.send(Bukkit.getOnlinePlayers(), m -> {
                                return m.replace("{player}", player.getName())
                                        .replace("{amount}", String.valueOf(value))
                                        .replace("{item}", link.getName());
                            });
                        }
                    });
                });
            });

            completableFuture.complete(gui);
        });

        return completableFuture;
    }
}
