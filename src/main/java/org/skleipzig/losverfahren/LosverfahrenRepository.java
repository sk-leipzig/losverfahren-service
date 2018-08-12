package org.skleipzig.losverfahren;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@RepositoryRestResource(collectionResourceRel = "losverfahren", path = "losverfahren")
public interface LosverfahrenRepository extends MongoRepository<Losverfahren, Integer> {
}
