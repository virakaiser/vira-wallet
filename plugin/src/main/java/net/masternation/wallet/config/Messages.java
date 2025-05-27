package net.masternation.wallet.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Header;
import me.masterkaiser.bukkit.message.RawMessage;
import me.masterkaiser.framework.config.YamlConfig;
import me.masterkaiser.util.ListBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@YamlConfig(fileName = "messages")
@Header(value = {
        "Please note that only IridiumColorAPI formatting is supported, not Adventure formatting",
        "Examples of supported formatting:",
        "<GRADIENT:2C08BA>Cool string with a gradient</GRADIENT:028A97>",
        "<SOLID:FF0080>Cool RGB SUPPORT",
        "For information on how to use IridiumColorAPI, please refer to their GitHub repository: https://github.com/Iridium-Development/IridiumColorAPI"
})
public class Messages extends OkaeriConfig {

    public RawMessage noPermission = new RawMessage("&8» &cYou don't have permission.");

    public RawMessage configReloaded = new RawMessage("&8» &aReloaded.");

    public RawMessage onlyPlayerCommand = new RawMessage("&8» &cThis command is for players only.");

    public RawMessage usage = new RawMessage("&8» &cCorrect usage: &b{usage}&c.");

    public boolean sendUsage(@NotNull CommandSender commandSender, @NotNull String usage) {
        return this.usage.send(commandSender, m -> m.replace("{usage}", usage));
    }

    public RawMessage invalidNumber = new RawMessage("&8» &cThe provided number is invalid.");

    public RawMessage insufficientFunds = new RawMessage("&8» &cYou don't have enough wPLN.");

    public RawMessage checkWallet = new RawMessage("&8» &9You have: &f{wallet} wPLN.");

    public RawMessage addWallet = new RawMessage("&f[{admin}] &a{value} wPLN have been added to &6{player}'s &awallet.");

    public RawMessage minusWallet = new RawMessage("&f[{admin}] &a{value} wPLN have been removed from &6{player}'s &awallet.");

    public RawMessage setWallet = new RawMessage("&f[{admin}] &a{value} wPLN have been set for &6{player}'s &awallet.");

    public RawMessage getWallet = new RawMessage("&f[{admin}] &aPlayer &6{player}'s &ahas {value} wPLN.");

    public RawMessage transactionInProgress = new RawMessage("&aTransaction in progress...");

    public RawMessage transactionSuccessMessage = new RawMessage("&aTransaction completed successfully.");

    public RawMessage receiveDailyReward = new RawMessage("&aYou received {reward} wPLN from the daily reward.");

    public RawMessage purchaseMessage = new RawMessage(
            new ListBuilder<String>()
                    .add("&f")
                    .add("&f")
                    .add("&f")
                    .add(" &8############################")
                    .add(" &8# &7Player &6{player} &7bought &6{item}")
                    .add(" &8# &7Top up your account at &6https://shop.com")
                    .add(" &8# &7You can purchase ranks at &6/itemshop.")
                    .add(" &8############################")
                    .build()
    );

    public RawMessage mountablePurchaseMessage = new RawMessage(
            new ListBuilder<String>()
                    .add("&f")
                    .add("&f")
                    .add("&f")
                    .add(" &8############################")
                    .add(" &8# &7Player &6{player} &7bought &8x{amount} &6{item}")
                    .add(" &8# &7Top up your account at &6https://shop.com")
                    .add(" &8# &7You can purchase ranks at &6/itemshop.")
                    .add(" &8############################")
                    .build()
    );

}
