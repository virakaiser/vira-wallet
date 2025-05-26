package net.masternation.wallet.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import me.masterkaiser.bukkit.api.item.ItemBuilder;
import me.masterkaiser.bukkit.gui.builder.GuiBuilder;
import me.masterkaiser.bukkit.gui.item.ItemSlot;
import me.masterkaiser.framework.config.YamlConfig;
import me.masterkaiser.util.ListBuilder;
import net.masternation.wallet.offer.Offer;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

@YamlConfig(fileName = "config")
public class Config extends OkaeriConfig {
    
    public double discountPercentage = 50.0;
    public String discountCostFormat = "{cost} wPLN &8(&ddiscount -{percent}%&8)";

    @Comment
    @Comment
    public GuiBuilder mainGui = new GuiBuilder("&8ItemShop", 4)
            .addItem(35, new ItemBuilder(Material.PAPER).setDisplayName("&4Top up your account at: &fhttps://shop.com"))
            .addStateItem("account", new ItemSlot(31, new ItemBuilder(Material.PLAYER_HEAD)
                    .setDisplayName("&9Profile")
                    .addLoreLine("&f» &7You have &f{pln} wPLN")));

    @Comment
    @Comment
    public GuiBuilder quantitySelectionGui = new GuiBuilder("&8Select Quantity", 5)
            .addAction("item", 11)
            .addStateItem("confirm", new ItemSlot(15, new ItemBuilder(Material.PAPER)
                    .setDisplayName("&7Selected quantity &f{amount}")
                    .addLoreLine("&f» &7Account balance: &f{pln} wPLN")
                    .addLoreLine("&f")
                    .addLoreLine("&f» &7Total &f{cost}")
                    .addLoreLine("&f")
                    .addLoreLine("&6Click to proceed.")))
            .addStateItem("remove-1", new ItemSlot(29, new ItemBuilder(Material.RED_DYE).setDisplayName("&c-1")))
            .addStateItem("remove-2", new ItemSlot(30, new ItemBuilder(Material.RED_DYE).setDisplayName("&c-2")))
            .addStateItem("remove-3", new ItemSlot(31, new ItemBuilder(Material.RED_DYE).setDisplayName("&c-3")))
            .addStateItem("remove-5", new ItemSlot(32, new ItemBuilder(Material.RED_DYE).setDisplayName("&c-5")))
            .addStateItem("remove-10", new ItemSlot(33, new ItemBuilder(Material.RED_DYE).setDisplayName("&c-10")))
            .addStateItem("add-1", new ItemSlot(38, new ItemBuilder(Material.LIME_DYE).setDisplayName("&a+1")))
            .addStateItem("add-2", new ItemSlot(39, new ItemBuilder(Material.LIME_DYE).setDisplayName("&a+2")))
            .addStateItem("add-3", new ItemSlot(40, new ItemBuilder(Material.LIME_DYE).setDisplayName("&a+3")))
            .addStateItem("add-5", new ItemSlot(41, new ItemBuilder(Material.LIME_DYE).setDisplayName("&a+5")))
            .addStateItem("add-10", new ItemSlot(42, new ItemBuilder(Material.LIME_DYE).setDisplayName("&a+10")))
            .addStateItem("set-default", new ItemSlot(13, new ItemBuilder(Material.MAP).setDisplayName("&eSet default quantity")))
            .setBackground(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&f"));

    @Comment
    @Comment
    public GuiBuilder purchaseConfirmationGui = new GuiBuilder("&8Confirm Purchase", 3)
            .setBackground(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&f"))
            .addStateItem("refuse", new ItemSlot(11, new ItemBuilder(Material.RED_CONCRETE).setDisplayName("&cCancel.")))
            .addStateItem("confirm", new ItemSlot(15, new ItemBuilder(Material.LIME_CONCRETE)
                    .setDisplayName("&9Confirm Purchase")
                    .addLoreLine("&f» &7Account balance: &f{pln} wPLN")
                    .addLoreLine("&f")
                    .addLoreLine("&f» &7Quantity &f{amount}")
                    .addLoreLine("&f» &7Total &f{cost}")
                    .addLoreLine("&f")
                    .addLoreLine("&6Click to buy.")));

    @Comment
    @Comment
    public GuiBuilder processingGui = new GuiBuilder("&8Processing", 1)
            .setBackground(new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE).setDisplayName("&f"));

    @Comment
    @Comment
    public List<String> loreOfferSufficientFunds = ListBuilder.ofValue(
            "",
            "&f» &7Price: &f{cost}",
            "",
            "&aYou can purchase.",
            "&aClick to buy."
    );

    public List<String> loreOfferInsufficientFunds = ListBuilder.ofValue(
            "",
            "&f» &7Price: &f{cost}",
            "",
            "&cYou don't have enough money."
    );

    @Comment
    @Comment
    public List<Offer> offers = new ListBuilder<Offer>()
            .add(new Offer(
                    "legendary-keys",
                    "&cLegendary Keys",
                    4.99,
                    true,
                    0,
                    new ItemBuilder(Material.TRIPWIRE_HOOK).setDisplayName("&cLegendary Key"),
                    new ArrayList<>(),
                    new ListBuilder<ItemBuilder>()
                            .add(new ItemBuilder(Material.TRIPWIRE_HOOK).setDisplayName("&cLegendary Key"))
                            .build()
            ))
            .add(new Offer(
                    "vip-rank",
                    "&6VIP",
                    9.99,
                    false,
                    1,
                    new ItemBuilder(Material.IRON_HELMET).setDisplayName("&6VIP"),
                    new ListBuilder<String>()
                            .add("lp user {player} parent add vip")
                            .build(),
                    new ArrayList<>()
            ))
            .add(new Offer(
                    "svip-rank",
                    "&eSVIP",
                    14.99,
                    false,
                    2,
                    new ItemBuilder(Material.DIAMOND_HELMET).setDisplayName("&eSVIP"),
                    new ListBuilder<String>()
                            .add("lp user {player} parent add svip")
                            .build(),
                    new ArrayList<>()
            ))
            .build();
}
