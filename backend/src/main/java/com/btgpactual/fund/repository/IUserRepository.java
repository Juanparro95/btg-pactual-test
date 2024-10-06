package com.btgpactual.fund.repository;

import com.btgpactual.fund.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Davidparro
 */
@Repository
public interface IUserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
}
