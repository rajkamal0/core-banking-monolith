package fintrack_monolith.gl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GeneralLedgerService {
	
	@Autowired
	GeneralLedgerRepository generalLedgerRepository;
	
	@Transactional(propagation = Propagation.MANDATORY)
	public String debitGL(Integer glNum, BigDecimal amount) {
		
		// check this again
		GeneralLedger gl = new GeneralLedger();
		
		if(gl.getGlStatus() != 'A') {
			return "GL " + glNum + " is not active";
		}
		
		BigDecimal currBalance = gl.getBalance();
		//
		
		
		return null;
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
	public String creditGL(Integer glNum, BigDecimal amount) {
		
		return null;
	}

}
