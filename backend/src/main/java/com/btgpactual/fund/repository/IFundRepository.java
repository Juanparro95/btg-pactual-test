package com.btgpactual.fund.repository;

import com.btgpactual.fund.model.Fund;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Davidparro
 */
@Repository
public interface IFundRepository extends MongoRepository<Fund, String> {
}
