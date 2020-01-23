package edu.indiana.dlib.amppd.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import edu.indiana.dlib.amppd.model.AmpUser;
import edu.indiana.dlib.amppd.model.PasswordResetToken;

@CrossOrigin(origins = "*")
@RepositoryRestResource(collectionResourceRel = "token", path = "token")
public interface PasswordTokenRepository extends CrudRepository<PasswordResetToken, Long>{
		
		Optional<PasswordResetToken> findByToken(String token);
		
		Optional<PasswordResetToken> findByUser(AmpUser user);
		
		@Transactional
		@Modifying
		@Query(value = "update PasswordResetToken set token = :token, expiry_date= :expiration_date  where user_id = :user_id")
		void updateToken( @Param("token") String token, @Param("user_id") Long user_id, @Param("expiration_date") Date expiry_date);
		
		@Query(value = "select count(*) from PasswordResetToken where user_id = :user_id")
		int ifExists(@Param("user_id") Long user_id);
}
