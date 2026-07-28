package fintrack_monolith.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{
	
	@Query(value = "SELECT SEQ_ACCOUNT.NEXTVAL FROM DUAL", nativeQuery = true)
	Integer findNextAccountSequence();
	
	Optional<Account> findByAccountNum(String accountNum);
	
	List<Account> findByCustomerId(Integer customerId);
	
	boolean existsByAccountNum(String accountNum);

}
