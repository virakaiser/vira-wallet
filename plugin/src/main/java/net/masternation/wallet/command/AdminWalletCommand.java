package net.masternation.wallet.command;

import lombok.RequiredArgsConstructor;
import me.masterkaiser.bukkit.command.ArgumentMeta;
import me.masterkaiser.bukkit.command.CommandMeta;
import me.masterkaiser.bukkit.command.bukkit.MasterCommand;
import me.masterkaiser.bukkit.message.RawMessage;
import me.masterkaiser.util.ParseUtil;
import me.masterkaiser.util.RoundUtil;
import net.masternation.wallet.config.Commands;
import net.masternation.wallet.config.Config;
import net.masternation.wallet.config.Messages;
import net.masternation.wallet.user.UserManager;
import net.masternation.wallet.util.FormatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AdminWalletCommand implements MasterCommand {

    private final Commands commands;
    private final UserManager userManager;
    private final Messages messages;
    private final Config config;

    private final String ADMIN_ARGUMENT_NAME = "admin";

    @Override
    public @NotNull CommandMeta commandMeta() {
        return this.commands.wallet;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull String[] args) {
        ArgumentMeta adminArgument = commandMeta().getArgument(this.ADMIN_ARGUMENT_NAME);

        if (args.length == 0 || !commandSender.hasPermission(adminArgument.getPermission())) {
            handlePlayerWalletCheck(commandSender);
            return true;
        }

        return switch (args[0].toLowerCase()) {
            case "add" ->
                    handleModifyWallet(commandSender, args, adminArgument, this.userManager::addWallet, this.messages.addWallet);
            case "minus" ->
                    handleModifyWallet(commandSender, args, adminArgument, this.userManager::minusWallet, this.messages.minusWallet);
            case "set" ->
                    handleModifyWallet(commandSender, args, adminArgument, this.userManager::modifyWallet, this.messages.setWallet);
            case "get" -> handleGetWallet(commandSender, args, adminArgument);
            case "reload" -> handleReloadCommand(commandSender);
            default -> {
                this.messages.sendUsage(commandSender, adminArgument.getUsage());
                yield true;
            }
        };
    }

    private void handlePlayerWalletCheck(@NotNull CommandSender commandSender) {
        if (commandSender instanceof Player player) {
            this.userManager.getWallet(player.getName()).thenAccept(walletAmount -> {
                if (walletAmount != null) {
                    this.messages.checkWallet.send(commandSender, m ->
                            m.replace("{wallet}", FormatUtil.formatNumber(RoundUtil.roundToDecimalPlaces(walletAmount, 2))));
                }
            });
        } else {
            this.messages.sendUsage(commandSender, commandMeta().getUsage());
        }
    }

    private boolean handleModifyWallet(
            @NotNull CommandSender commandSender,
            @NotNull String[] args,
            @NotNull ArgumentMeta adminArgument,
            WalletModificationFunction walletModifier,
            @NotNull RawMessage rawMessage
    ) {
        if (args.length < 3) {
            return this.messages.sendUsage(commandSender, adminArgument.getUsage());
        }

        Optional<Double> amountOptional = ParseUtil.parseDouble(args[2]);
        if (amountOptional.isEmpty()) {
            return this.messages.invalidNumber.send(commandSender);
        }

        double amount = amountOptional.get();
        walletModifier.apply(args[1], amount);
        rawMessage.send(commandSender, m -> m.replace("{admin}", commandSender.getName())
                .replace("{player}", args[1])
                .replace("{value}", String.valueOf(amount)));
        return true;
    }

    @FunctionalInterface
    private interface WalletModificationFunction {
        void apply(String playerName, double amount);
    }

    private boolean handleGetWallet(@NotNull CommandSender commandSender, @NotNull String[] args, @NotNull ArgumentMeta adminArgument) {
        if (args.length < 2) {
            return this.messages.sendUsage(commandSender, adminArgument.getUsage());
        }

        this.userManager.getWallet(args[1]).thenAccept(walletAmount -> {
            if (walletAmount != null) {
                this.messages.getWallet.send(commandSender, m -> m.replace("{admin}", commandSender.getName())
                        .replace("{player}", args[1])
                        .replace("{value}", FormatUtil.formatNumber(RoundUtil.roundToDecimalPlaces(walletAmount, 2))));
            }
        });
        return true;
    }

    private boolean handleReloadCommand(@NotNull CommandSender commandSender) {
        this.config.load();
        this.messages.load();
        this.commands.load();
        return this.messages.configReloaded.send(commandSender);
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        ArgumentMeta adminArgument = commandMeta().getArgument(this.ADMIN_ARGUMENT_NAME);
        if (sender.hasPermission(adminArgument.getPermission())) {
            if (args.length == 1) {
                return List.of("add", "minus", "set", "get", "reload");
            }

            if (args.length == 3 && (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("minus") || args[0].equalsIgnoreCase("set"))) {
                return List.of("1", "3", "5", "10", "20", "30", "50", "100", "200");
            }
        }

        return MasterCommand.super.onTabComplete(sender, args);
    }
}
