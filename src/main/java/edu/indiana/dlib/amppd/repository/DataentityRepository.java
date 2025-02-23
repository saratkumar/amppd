package edu.indiana.dlib.amppd.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import edu.indiana.dlib.amppd.model.Dataentity;

@NoRepositoryBean
//@RepositoryRestResource(excerptProjection = DataentityBrief.class)
public interface DataentityRepository<S extends Dataentity> extends PagingAndSortingRepository<S, Long> {
	
	List<S> findByName(@Param("name") String name);

	List<S> findByDescription(@Param("description") String description); // TODO: use customized impl to do match with SQL LIKE instead of =

	List<S> findByCreatedDate(@Param("createdDate") Date createdDate);

	List<S> findByCreatedBy(@Param("createdBy") String createdBy);

	List<S> findByModifiedDate(@Param("modifiedDate") String createdDate);

	List<S> findByModifiedBy(@Param("modifiedBy") String modifiedBy);

}
