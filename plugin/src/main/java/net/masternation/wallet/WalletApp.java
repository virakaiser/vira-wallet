package net.masternation.wallet;

import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import me.masterkaiser.bukkit.BukkitApp;
import me.masterkaiser.bukkit.BukkitSerializers;
import me.masterkaiser.bukkit.serializer.RawSerializer;
import me.masterkaiser.framework.AppStatus;
import me.masterkaiser.framework.persistence.EnablePersistence;
import net.masternation.wallet.offer.Offer;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"net.masternation.wallet"})
@EnablePersistence
public class WalletApp extends BukkitApp {

    @Override
    public void appChangeStatus(@NotNull AppStatus appStatus) {
        if (appStatus == AppStatus.ENABLE) {
            context().refresh();
        }
    }

    @Override
    public OkaeriSerdesPack serializers() {
        return registry -> {
            registry.register(new BukkitSerializers());
            registry.register(RawSerializer.getSerializer(Offer.class));
        };
    }

}
