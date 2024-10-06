package com.btgpactual.fund.repository;

import com.btgpactual.fund.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Davidparro
 */
@Repository
public interface ITransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByFundId(String fundId);    
    List<Transaction> findByUserIdAndType(String userId, String type);
    Optional<Transaction> findByFundIdAndUserIdAndType(String fundId, String userId, String type);
    List<Transaction> findByUserId(String userId);
}
