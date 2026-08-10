package fintrack_monolith.account;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{
	
	@Query(value = "SELECT SEQ_ACCOUNT.NEXTVAL FROM DUAL", nativeQuery = true)
	Integer findNextAccountSequence();
	
	Optional<Account> findByAccountNum(String accountNum);
	
	List<Account> findByCustomerId(String customerId);
	
	boolean existsByAccountNum(String accountNum);
	
	boolean existsByCustomerIdAndAccountStatus(String customerId, Character accountStatus);

}
