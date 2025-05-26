package net.masternation.wallet.offer;

import lombok.RequiredArgsConstructor;
import net.masternation.wallet.config.Config;
import net.masternation.wallet.util.FormatUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OfferService {

    private final Config config;

    public double getCost(@NotNull Offer offer, int amount) {
        if (this.config.discountPercentage <= 0.0) {
            return offer.getCost();
        }

        double discount = (offer.getCost() * this.config.discountPercentage) / 100;
        return (offer.getCost() - discount) * amount;
    }

    public String getCostString(@NotNull Offer offer, int amount) {
        if (this.config.discountPercentage <= 0.0) {
            return FormatUtil.formatNumber(getCost(offer, amount)) + " zł";
        }

        return this.config.discountCostFormat.replace("{cost}", FormatUtil.formatNumber(getCost(offer, amount)))
                .replace("{percent}", FormatUtil.formatNumber(this.config.discountPercentage));
    }

}
