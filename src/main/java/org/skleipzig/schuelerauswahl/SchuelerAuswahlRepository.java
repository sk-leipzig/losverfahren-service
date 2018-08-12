package org.skleipzig.schuelerauswahl;

import org.skleipzig.schuelerlisten.Schueler;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Collection;

@CrossOrigin(origins = "*")
@RepositoryRestResource(collectionResourceRel = "schuelerauswahl", path = "/schuelerauswahl")
public interface SchuelerAuswahlRepository  extends MongoRepository<SchuelerAuswahl, Schueler> {
    SchuelerAuswahl findBySchuelerKennung(@Param("kennung") String kennung);
    void deleteAllBySchueler(Collection<Schueler> schueler);
}
