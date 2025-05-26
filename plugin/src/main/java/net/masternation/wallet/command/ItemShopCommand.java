package net.masternation.wallet.command;

import lombok.RequiredArgsConstructor;
import me.masterkaiser.bukkit.command.CommandMeta;
import me.masterkaiser.bukkit.command.bukkit.MasterCommand;
import net.masternation.wallet.config.Commands;
import net.masternation.wallet.gui.MainGui;
import net.masternation.wallet.gui.ProcessingGui;
import net.masternation.wallet.user.UserService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemShopCommand implements MasterCommand {

    private final Commands commands;
    private final ProcessingGui processingGui;
    private final MainGui mainGui;
    private final UserService userService;

    @Override
    public @NotNull CommandMeta commandMeta() {
        return this.commands.itemShop;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if (!this.userService.canDoAction(player.getUniqueId())) {
            return false;
        }

        this.processingGui.open(player);
        this.mainGui.build(player).thenAccept(gui -> {
            if (player.isOnline()) {
                gui.open(player);
            } else {
                gui.dispose(true);
            }
        });

        return false;
    }

}
