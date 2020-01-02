package edu.indiana.dlib.amppd.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import edu.indiana.dlib.amppd.model.Batch;


@RepositoryRestResource(collectionResourceRel = "batch", path = "batch")
public interface BatchRepository extends CrudRepository<Batch, Long> {
	
}
