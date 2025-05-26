package net.masternation.wallet.config;

import eu.okaeri.configs.OkaeriConfig;
import me.masterkaiser.bukkit.command.ArgumentMeta;
import me.masterkaiser.bukkit.command.CommandMeta;
import me.masterkaiser.framework.config.YamlConfig;

@YamlConfig(fileName = "commands")
public class Commands extends OkaeriConfig {

    public String prefix = "vira-wallet";

    public CommandMeta wallet = new CommandMeta("wallet")
            .setUsage("/wallet")
            .setDescription("Check your wallet.")
            .setPermission("vira.wallet.wallet")
            .addArgument("admin", new ArgumentMeta(
                    "admin",
                    "/wallet <add/minus/set/get/reload> <!player> <!value>",
                    "Manage wallet.",
                    "vira.wallet.wallet.admin",
                    new String[0]
            ))
            .setAliases(new String[] {"wallet"});

    public CommandMeta itemShop = new CommandMeta("itemshop")
            .setUsage("/itemshop")
            .setDescription("Check ItemShop offers.")
            .setPermission("vira.wallet.itemshop")
            .setAliases(new String[] {"is"});

}
