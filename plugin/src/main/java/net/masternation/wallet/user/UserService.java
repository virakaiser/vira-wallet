package net.masternation.wallet.user;

import eu.okaeri.tasker.core.Tasker;
import lombok.RequiredArgsConstructor;
import me.masterkaiser.util.RandomUtil;
import net.masternation.wallet.config.Config;
import net.masternation.wallet.user.contract.IUserService;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserService implements IUserService<User, UUID> {

    private final Tasker tasker;
    private final UserRepository userRepository;
    private final Config config;
    private final Set<UUID> blockedUsers = new HashSet<>();

    @Override
    public @NotNull User findOrCreateUser(@NotNull UUID key) {
        return this.userRepository.findOrCreateByPath(key);
    }

    @Override
    public @NotNull CompletableFuture<User> retrieveUser(@NotNull UUID uuid) {
        CompletableFuture<User> completableFuture = new CompletableFuture<>();

        this.tasker.newSharedChain("retrieve-user;" + uuid)
                .supplyAsync(() -> findOrCreateUser(uuid))
                .acceptSync(completableFuture::complete)
                .execute();

        return completableFuture;
    }

    public @NotNull CompletableFuture<User> retrieveUserByPlayer(@NotNull Player player) {
        CompletableFuture<User> completableFuture = new CompletableFuture<>();

        this.tasker.newSharedChain("retrieve-user;" + player.getUniqueId())
                .supplyAsync(() -> findOrCreateUser(player.getUniqueId()))
                .acceptAsync(user -> {
                    if (user.getLastName() == null || !user.getLastName().equalsIgnoreCase(player.getName())) {
                        user.setLastName(player.getName().toLowerCase());
                        this.userRepository.save(user);
                    }
                })
                .acceptSync(completableFuture::complete)
                .execute();

        return completableFuture;
    }

    public @NotNull CompletableFuture<User> retrieveByName(@NotNull String name) {
        CompletableFuture<User> completableFuture = new CompletableFuture<>();
        this.tasker.newSharedChain("retrieve-user;" + name)
                .supplyAsync(() -> this.userRepository.findEntityByLastName(name.toLowerCase()))
                .acceptSync(userPersistenceEntity ->
                        userPersistenceEntity.ifPresentOrElse(u -> completableFuture.complete(u.getValue()), () -> completableFuture.complete(null)))
                .execute();

        return completableFuture;
    }

    @Override
    public void modifyWallet(@NotNull UUID key, double value) {
        this.blockedUsers.add(key);
        retrieveUser(key).thenAccept(user -> {
            user.setWallet(value);
            save(user, v -> this.blockedUsers.remove(key));
        });
    }

    public void minusWallet(@NotNull UUID key, double value) {
        this.blockedUsers.add(key);
        retrieveUser(key).thenAccept(user -> {
            double balance = user.getWallet() - value;
            if (balance < 0.0) {
                balance = 0.0;
            }

            user.setWallet(balance);
            save(user, v -> this.blockedUsers.remove(key));
        });
    }

    @Override
    public void addWallet(@NotNull UUID key, double value) {
        this.blockedUsers.add(key);
        retrieveUser(key).thenAccept(user -> {
            double balance = user.getWallet() + value;

            user.setWallet(balance);
            save(user, v -> this.blockedUsers.remove(key));
        });
    }

    public void modifyWallet(@NotNull User user, double value) {
        this.blockedUsers.add(user.getUUID());
        user.setWallet(value);
        save(user, v -> this.blockedUsers.remove(user.getUUID()));
    }

    public void minusWallet(@NotNull User user, double value) {
        this.blockedUsers.add(user.getUUID());
        double balance = user.getWallet() - value;
        if (balance < 0.0) {
            balance = 0.0;
        }

        user.setWallet(balance);
        save(user, v -> this.blockedUsers.remove(user.getUUID()));
    }

    public void addWallet(@NotNull User user, double value) {
        this.blockedUsers.add(user.getUUID());
        double balance = user.getWallet() + value;

        user.setWallet(balance);
        save(user, v -> this.blockedUsers.remove(user.getUUID()));
    }

    @Override
    public @NotNull CompletableFuture<Boolean> hasWallet(@NotNull UUID key, double value) {
        return retrieveUser(key)
                .thenApply(user -> user.getWallet() >= value);
    }

    @Override
    public @NotNull CompletableFuture<Double> getWallet(@NotNull UUID key) {
        return retrieveUser(key)
                .thenApply(User::getWallet);
    }

    public @NotNull CompletableFuture<Boolean> canClaimDailyReward(@NotNull UUID key) {
        return retrieveUser(key)
                .thenApply(User::canClaimDailyReward);
    }

    public @NotNull CompletableFuture<Double> claimDailyReward(@NotNull UUID key) {
        this.blockedUsers.add(key);
        CompletableFuture<Double> completableFuture = new CompletableFuture<>();

        double rand = RandomUtil.randomDouble(this.config.dailyRewardMin, this.config.dailyRewardMax);
        retrieveUser(key).thenAccept(user -> {
            user.setWallet((user.getWallet() + rand));
            user.claimDailyReward();
            save(user, v -> {
                this.blockedUsers.remove(key);
                completableFuture.complete(rand);
            });
        });

        return completableFuture;
    }

    @Override
    public void save(@NotNull User user, @NotNull Consumer<Void> consumer) {
        this.tasker.newSharedChain("save-user;" + user.getUUID())
                .async(() -> {
                    user.save();
                    consumer.accept(null);
                })
                .execute();
    }

    public boolean canDoAction(@NotNull UUID uuid) {
        return !this.blockedUsers.contains(uuid);
    }

}
