package edu.indiana.dlib.amppd.model;

import javax.jdo.annotations.Index;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import edu.indiana.dlib.amppd.validator.UniqueName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * CollectionSupplement is a supplemental file associated with a collection and shared by all items within that collection.
 * @author yingfeng
 *
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = {@UniqueConstraint(name = "UniquePrimaryfileNamePerItem", columnNames = {"collection_id", "name"})})
@UniqueName(message="collectionSupplement name must be unique within its parent collection")
@Data
@EqualsAndHashCode(callSuper=true, onlyExplicitlyIncluded=true)
@ToString(callSuper=true, onlyExplicitlyIncluded=true)
public class CollectionSupplement extends Supplement {

	@NotNull
	@Index
	@ManyToOne
    private Collection collection;
    
}
