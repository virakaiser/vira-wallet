package net.masternation.wallet.user.named;

import eu.okaeri.tasker.core.Tasker;
import lombok.RequiredArgsConstructor;
import net.masternation.wallet.user.User;
import net.masternation.wallet.user.contract.IUserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class NamedUserService implements IUserService<NamedUser, String> {

    private final NamedUserRepository namedUserRepository;
    private final Tasker tasker;

    @Override
    public @NotNull NamedUser findOrCreateUser(@NotNull String key) {
        return this.namedUserRepository.findOrCreateByPath(key.toLowerCase());
    }

    @Override
    public @NotNull CompletableFuture<NamedUser> retrieveUser(@NotNull String key) {
        CompletableFuture<NamedUser> completableFuture = new CompletableFuture<>();

        this.tasker.newSharedChain("retrieve-named-user;" + key.toLowerCase())
                .supplyAsync(() -> findOrCreateUser(key.toLowerCase()))
                .acceptSync(completableFuture::complete)
                .execute();

        return completableFuture;
    }

    @Override
    public void modifyWallet(@NotNull String key, double value) {
        retrieveUser(key).thenAccept(user -> {
            user.setWallet(value);
            save(user, v -> {});
        });
    }

    @Override
    public void minusWallet(@NotNull String key, double value) {
        retrieveUser(key).thenAccept(user -> {
            double balance = user.getWallet() - value;
            if (balance < 0.0) {
                balance = 0.0;
            }

            user.setWallet(balance);
            save(user, v -> {});
        });
    }

    @Override
    public void addWallet(@NotNull String key, double value) {
        retrieveUser(key).thenAccept(user -> {
            double balance = user.getWallet() + value;

            user.setWallet(balance);
            save(user, v -> {});
        });
    }

    @Override
    public @NotNull CompletableFuture<Boolean> hasWallet(@NotNull String key, double value) {
        return retrieveUser(key)
                .thenApply(user -> user.getWallet() >= value);
    }

    @Override
    public @NotNull CompletableFuture<Double> getWallet(@NotNull String key) {
        return retrieveUser(key)
                .thenApply(NamedUser::getWallet);
    }

    @Override
    public void save(@NotNull NamedUser namedUser, @NotNull Consumer<Void> consumer) {
        this.tasker.newSharedChain("save-named-user;" + namedUser.getName())
                .async(() -> {
                    namedUser.save();
                    consumer.accept(null);
                })
                .execute();
    }

    public void connectAccount(@NotNull NamedUser namedUser, @NotNull User user, @NotNull Consumer<Void> consumer) {
        this.tasker.newSharedChain("delete-named-user;" + namedUser.getName())
                .async(() -> {
                    this.namedUserRepository.deleteByPath(namedUser.getName());
                    user.setWallet((user.getWallet() + namedUser.getWallet()));
                })
                .sync(() -> consumer.accept(null))
                .execute();
    }

}
