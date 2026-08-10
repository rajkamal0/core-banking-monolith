package fintrack_monolith.transaction;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer>{
	
	@Query(value = "SELECT SEQ_TRANSACTION.NEXTVAL FROM DUAL", nativeQuery = true)
	Integer getNextAccountSequence();
	
	Optional<Transaction> findByTxnID(String txnID);
	
	
	@Query(value = "SELECT t FROM Transaction t WHERE t.creditAccount = :accountNumber OR t.debitAccount = :accountNumber")
	List<Transaction> findByAccountNumber(@Param ("accountNumber") String accountNumber);
	
	boolean existsByTxnID(String txnID);

}
