package net.masternation.wallet.user;

import lombok.RequiredArgsConstructor;
import net.masternation.wallet.user.named.NamedUserService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserManager implements Listener {

    private final UserService userService;
    private final NamedUserService namedUserService;

    public void modifyWallet(@NotNull String name, double add) {
        this.userService.retrieveByName(name).thenAccept(user -> {
            if (user == null) {
                this.namedUserService.modifyWallet(name, add);
            } else {
                this.userService.modifyWallet(user, add);
            }
        });
    }

    public void minusWallet(@NotNull String name, double add) {
        this.userService.retrieveByName(name).thenAccept(user -> {
            if (user == null) {
                this.namedUserService.minusWallet(name, add);
            } else {
                this.userService.minusWallet(user, add);
            }
        });
    }

    public void addWallet(@NotNull String name, double add) {
        this.userService.retrieveByName(name).thenAccept(user -> {
            if (user == null) {
                this.namedUserService.addWallet(name, add);
            } else {
                this.userService.addWallet(user, add);
            }
        });
    }

    public @NotNull CompletableFuture<Double> getWallet(@NotNull String name) {
        return this.userService.retrieveByName(name).thenCompose(user -> {
            if (user == null) {
                return this.namedUserService.getWallet(name);
            } else {
                return CompletableFuture.completedFuture(user.getWallet());
            }
        });
    }

    @EventHandler
    void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        this.userService.retrieveUserByPlayer(player).thenAccept(user -> {
            this.namedUserService.retrieveUser(player.getName()).thenAccept(namedUser -> {
                if (namedUser.getWallet() > 0.0) {
                    this.namedUserService.connectAccount(namedUser, user, v -> this.userService.save(user, v2 -> {}));
                }
            });
        });
    }

}
