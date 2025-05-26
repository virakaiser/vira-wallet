package net.masternation.wallet.user.named;

import eu.okaeri.persistence.repository.DocumentRepository;
import eu.okaeri.persistence.repository.annotation.DocumentCollection;

@DocumentCollection(path = "named_user", keyLength = 16)
public interface NamedUserRepository extends DocumentRepository<String, NamedUser> {
}
