package net.masternation.wallet.user;

import eu.okaeri.persistence.PersistenceEntity;
import eu.okaeri.persistence.repository.DocumentRepository;
import eu.okaeri.persistence.repository.annotation.DocumentCollection;
import eu.okaeri.persistence.repository.annotation.DocumentIndex;
import eu.okaeri.persistence.repository.annotation.DocumentPath;

import java.util.Optional;
import java.util.UUID;

@DocumentCollection(path = "user", keyLength = 48, indexes = {
        @DocumentIndex(path = "lastName", maxLength = 16)
})
public interface UserRepository extends DocumentRepository<UUID, User> {

    @DocumentPath("lastName")
    Optional<PersistenceEntity<User>> findEntityByLastName(String lastName);

}
