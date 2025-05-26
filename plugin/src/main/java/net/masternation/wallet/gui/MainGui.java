package net.masternation.wallet.gui;

import lombok.RequiredArgsConstructor;
import me.masterkaiser.bukkit.BukkitApp;
import me.masterkaiser.bukkit.api.item.ItemBuilder;
import me.masterkaiser.bukkit.api.item.ItemConverterService;
import me.masterkaiser.bukkit.gui.Gui;
import me.masterkaiser.util.RoundUtil;
import net.masternation.wallet.config.Config;
import net.masternation.wallet.config.Messages;
import net.masternation.wallet.offer.OfferService;
import net.masternation.wallet.user.UserService;
import net.masternation.wallet.util.FormatUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MainGui {

    private final BukkitApp bukkitApp;
    private final UserService userService;
    private final Config config;
    private final OfferConfirmGui offerConfirmGui;
    private final MountableGui mountableGui;
    private final Messages messages;
    private final ProcessingGui processingGui;
    private final OfferService offerService;

    public CompletableFuture<Gui> build(@NotNull Player object) {
        CompletableFuture<Gui> guiCompletableFuture = new CompletableFuture<>();
        Gui gui = Gui.create(this.bukkitApp, this.config.mainGui, true, false, null);
        this.userService.getWallet(object.getUniqueId()).thenAccept(pln -> {
            if (pln == null) {
                return;
            }

            Optional.ofNullable(this.config.mainGui.getStateItems().get("account")).ifPresent(itemSlot -> {
                ItemStack is = itemSlot.getItemBuilder().build(m -> m.replace("{pln}", FormatUtil.formatNumber(RoundUtil.roundToDecimalPlaces(pln, 2))));
                ItemMeta meta = is.getItemMeta();
                if (meta instanceof SkullMeta skullMeta) {
                    skullMeta.setOwningPlayer(object);
                    is.setItemMeta(skullMeta);
                }

                gui.setItem(itemSlot.getSlot(), is);
            });

            this.config.offers.forEach(offer -> {
                ItemBuilder itemBuilder = ItemConverterService.itemConverter.fromItemStack(offer.getIcon().build());
                if (pln >= offer.getCost()) {
                    itemBuilder.mergeLore(this.config.loreOfferSufficientFunds, m -> {
                        return m.replace("{cost}", offerService.getCostString(offer, 1));
                    });
                } else {
                    itemBuilder.mergeLore(this.config.loreOfferInsufficientFunds, m -> {
                        return m.replace("{cost}", offerService.getCostString(offer, 1));
                    });
                }

                gui.setItem(offer.getSlot(), itemBuilder.build(), event -> {
                    Player player = (Player) event.getWhoClicked();
                    if (!this.userService.canDoAction(player.getUniqueId())) {
                        return;
                    }

                    if (pln < this.offerService.getCost(offer, 1)) {
                        this.messages.insufficientFunds.send(player);
                        return;
                    }

                    if (!offer.isMountable()) {
                        this.processingGui.open(player);
                        this.offerConfirmGui.build(player, offer, 1).thenAccept(gui1 -> {
                            if (player.isOnline()) {
                                gui1.open(player);
                            } else {
                                gui1.dispose(true);
                            }
                        });
                    } else {
                        this.processingGui.open(player);
                        this.mountableGui.build(player, offer).thenAccept(gui1 -> {
                            if (player.isOnline()) {
                                gui1.open(player);
                            } else {
                                gui1.dispose(true);
                            }
                        });
                    }
                });
            });

            guiCompletableFuture.complete(gui);
        });


        return guiCompletableFuture;
    }
}
