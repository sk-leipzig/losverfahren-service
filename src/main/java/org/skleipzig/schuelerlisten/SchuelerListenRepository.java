package org.skleipzig.schuelerlisten;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@RepositoryRestResource(collectionResourceRel = "schuelerlisten", path = "/schuelerlisten")
public interface SchuelerListenRepository extends MongoRepository<Schuelerliste, String> {

}
