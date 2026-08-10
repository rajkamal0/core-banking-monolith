package fintrack_monolith.gl;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralLedgerRepository extends JpaRepository<GeneralLedger, Integer>{
	
	@Query(value = "SELECT SEQ_GENERALLEDGER.NEXTVAL FROM DUAL", nativeQuery = true)
	Integer getNextGlSequence();
	
	Optional<GeneralLedger> findByGlNum(String glNum);
	
	boolean existsByGlNum(String glNum);

}
