package fintrack_monolith.gl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralLedgerRepository extends JpaRepository<GeneralLedger, Integer>{

}
