package net.masternation.wallet;

import eu.okaeri.tasker.bukkit.BukkitTasker;
import eu.okaeri.tasker.core.Tasker;
import lombok.RequiredArgsConstructor;
import me.masterkaiser.bukkit.BukkitApp;
import me.masterkaiser.bukkit.command.bukkit.CommandManager;
import me.masterkaiser.framework.persistence.RepositoryFactory;
import me.masterkaiser.framework.status.Initializable;
import net.masternation.wallet.config.Commands;
import net.masternation.wallet.config.Messages;
import net.masternation.wallet.user.UserRepository;
import net.masternation.wallet.user.named.NamedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WalletConfig implements Initializable {

    private final BukkitApp bukkitApp;
    private final Messages messages;
    private final Commands commands;
    private final CommandManager commandManager;
    private final RepositoryFactory repositoryFactory;

    @Override
    public void initialize() {
        this.commandManager.setPrefix(this.commands.prefix);
        this.commandManager.setNoPermissionMessage(this.messages.noPermission.findOneNotNullMessage());
        this.commandManager.setPlayerOnlyMessage(this.messages.onlyPlayerCommand.findOneNotNullMessage());
    }

    @Bean
    public Tasker tasker() {
        return BukkitTasker.newPool(this.bukkitApp);
    }

    @Bean
    UserRepository userRepository() {
        return (UserRepository) this.repositoryFactory.createRepository(UserRepository.class);
    }

    @Bean
    NamedUserRepository namedUserRepository() {
        return (NamedUserRepository) this.repositoryFactory.createRepository(NamedUserRepository.class);
    }

}
