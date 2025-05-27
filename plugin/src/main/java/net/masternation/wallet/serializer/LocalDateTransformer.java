package net.masternation.wallet.serializer;

import eu.okaeri.configs.schema.GenericsPair;
import eu.okaeri.configs.serdes.BidirectionalTransformer;
import eu.okaeri.configs.serdes.SerdesContext;
import lombok.NonNull;

import java.time.LocalDate;

public class LocalDateTransformer extends BidirectionalTransformer<String, LocalDate> {

    @Override
    public GenericsPair<String, LocalDate> getPair() {
        return this.genericsPair(String.class, LocalDate.class);
    }

    @Override
    public LocalDate leftToRight(@NonNull String data, @NonNull SerdesContext serdesContext) {
        try {
            return LocalDate.parse(data);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String rightToLeft(@NonNull LocalDate data, @NonNull SerdesContext serdesContext) {
        return data.toString();
    }

}
