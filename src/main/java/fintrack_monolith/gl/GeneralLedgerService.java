package fintrack_monolith.gl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fintrack_monolith.account.Account;
import fintrack_monolith.customer.Customer;

//Credit & Debit GL methods are written considering only Cash GL (Asset GL) is maintained

@Service
public class GeneralLedgerService {
	
	@Autowired
	GeneralLedgerRepository generalLedgerRepository;
	
	@Transactional(propagation = Propagation.MANDATORY)
	public String debitAssetGL(Integer glNum, BigDecimal amount) {
		
		GeneralLedger gl = generalLedgerRepository.findById(glNum).get();
		
		if(gl.getGlStatus() != 'A') {
			return "Asset GL " + glNum + " is not active";
		}
		
		BigDecimal currBalance = gl.getBalance();
		BigDecimal newBalance = currBalance.add(amount);
		return "Debited Rs." + amount + " to the Asset GL: " + glNum + "\nUpdated Balance is " + newBalance;
		
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
	public String creditAssetGL(Integer glNum, BigDecimal amount) {
		
		GeneralLedger gl = generalLedgerRepository.findById(glNum).get();
		
		if(gl.getGlStatus() != 'A') {
			return "Asset GL " + glNum + " is not active";
		}
		
		BigDecimal currBalance = gl.getBalance();
		int comparison = currBalance.compareTo(amount);
		
		if (comparison<0) {
			return "Asset GL " + glNum + " is not having sufficient funds";
		}
		
		BigDecimal newBalance = currBalance.subtract(amount);
		return "Credited Rs." + amount + " from the Asset GL: " + glNum + "\nUpdated Balance is " + newBalance;
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
	public String creditLiabilityGL(Integer glNum, BigDecimal amount) {
		
		GeneralLedger gl = generalLedgerRepository.findById(glNum).get();
		
		if(gl.getGlStatus() != 'A') {
			return "Liability GL " + glNum + " is not active";
		}
		
		BigDecimal currBalance = gl.getBalance();
		BigDecimal newBalance = currBalance.add(amount);
		return "Debited Rs." + amount + " to the Liability GL: " + glNum + "\nUpdated Balance is " + newBalance;
		
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
	public String debitLiabilityGL(Integer glNum, BigDecimal amount) {
		
		GeneralLedger gl = generalLedgerRepository.findById(glNum).get();
		
		if(gl.getGlStatus() != 'A') {
			return "Liability GL " + glNum + " is not active";
		}
		
		BigDecimal currBalance = gl.getBalance();
		int comparison = currBalance.compareTo(amount);
		
		if (comparison<0) {
			return "Liability GL " + glNum + " is not having sufficient funds";
		}
		
		BigDecimal newBalance = currBalance.subtract(amount);
		return "Credited Rs." + amount + " from the Liability GL: " + glNum + "\nUpdated Balance is " + newBalance;
	}
	
	public String fetchBalance(Integer glNum) {
		GeneralLedger gl = generalLedgerRepository.findById(glNum).get();
		
		if (gl != null && gl.getGlNum() != null)
			return "Balance in GL " + glNum + " is " + gl.getBalance();
		return "Balance check failed for GL " + glNum;
	}
	
	@Transactional
	public GeneralLedger createGL(GeneralLedger GLDetails) {
		
		GLDetails.setBalance(BigDecimal.ZERO);
		GLDetails.setGlStatus('A');
		return generalLedgerRepository.save(GLDetails);
	}

	
	public String deleteGL(Integer GLNum) {
		GeneralLedger gl = generalLedgerRepository.findById(GLNum).get();
		
		if (gl.getGlStatus()=='A') {
			gl.setGlStatus('C');
			return "GL " + GLNum + " deletion success";
		}
		
		return "GL " + GLNum + " deletion failed";
	}

}
