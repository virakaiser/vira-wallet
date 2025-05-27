package net.masternation.wallet.user;

import eu.okaeri.persistence.document.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class User extends Document {

    public UUID getUUID() {
        return getPath().toUUID();
    }

    private String lastName;
    private double wallet;
    private LocalDate lastRewardClaimDate;

    public boolean canClaimDailyReward() {
        return this.lastRewardClaimDate == null || !this.lastRewardClaimDate.equals(LocalDate.now());
    }

    public void claimDailyReward() {
        this.lastRewardClaimDate = LocalDate.now();
    }

}
