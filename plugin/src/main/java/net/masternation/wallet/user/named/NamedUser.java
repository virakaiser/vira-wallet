package net.masternation.wallet.user.named;

import eu.okaeri.persistence.document.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NamedUser extends Document {

    public String getName() {
        return getPath().getValue();
    }

    private double wallet;

}
