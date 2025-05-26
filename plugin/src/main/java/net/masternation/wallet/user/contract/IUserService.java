package net.masternation.wallet.user.contract;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface IUserService<U, K> {

    @NotNull
    U findOrCreateUser(@NotNull K key);

    @NotNull
    CompletableFuture<U> retrieveUser(@NotNull K key);

    void modifyWallet(@NotNull K key, double value);
    void minusWallet(@NotNull K key, double value);
    void addWallet(@NotNull K key, double value);

    @NotNull
    CompletableFuture<Boolean> hasWallet(@NotNull K key, double value);

    @NotNull
    CompletableFuture<Double> getWallet(@NotNull K key);

    void save(@NotNull U u, @NotNull Consumer<Void> consumer);
}
