package fintrack_monolith.customer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	
	@Query(value = "SELECT SEQ_CUSTOMER.NEXTVAL FROM DUAL", nativeQuery = true)
	Integer findNextCustomerSequence();
	
	Optional<Customer> findByCustId(String customerId);
	
	boolean existsByCustId(String customerId);

}
