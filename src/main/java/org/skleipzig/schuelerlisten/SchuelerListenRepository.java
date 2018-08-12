package org.skleipzig.schuelerlisten;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Collection;

@CrossOrigin(origins = "*")
@RepositoryRestResource(collectionResourceRel = "schuelerlisten", path = "/schuelerlisten")
public interface SchuelerListenRepository extends MongoRepository<Schuelerliste, String> {
    Schuelerliste findBySchuelerListeKennung(@Param("kennung") String kennung);
    Collection<Schuelerliste> findAllByLosverfahrenId(@Param("losverfahrenId") Integer losverfahrenId);
}
