package fintrack_monolith.gl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fintrack_monolith.account.Account;
import fintrack_monolith.exception.InsufficientFundsException;
import fintrack_monolith.exception.ResourceNotFoundException;

//Credit & Debit GL methods are written considering only Cash GL (Asset GL) is maintained

@Service
public class GeneralLedgerService {
	
	private GeneralLedgerRepository generalLedgerRepository;
	
	public GeneralLedgerService(GeneralLedgerRepository generalLedgerRepository) {
		this.generalLedgerRepository = generalLedgerRepository;
	}
	
	private boolean validateGL(Integer glNum, String ccy) {
		GeneralLedger gl = generalLedgerRepository.findById(glNum).orElseThrow(
				() -> new ResourceNotFoundException("GL " + glNum + " not found"));
		
		if (gl.getGlStatus() != 'A') {
			return false;
		}
		if(!gl.getCcyCode().equals(ccy)) {
			return false;
		}
		return true;
	}
	 
	
	@Transactional(propagation = Propagation.MANDATORY)
	public String debitAssetGL(Integer glNum, BigDecimal amount, String ccyCode) {
		
		if (!validateGL(glNum, ccyCode)) {
			return null;
		}
		
		GeneralLedger gl = generalLedgerRepository.findById(glNum).orElseThrow(
				() -> new ResourceNotFoundException("GL " + glNum + " not found"));
		
		BigDecimal currBalance = gl.getBalance();
		BigDecimal newBalance = currBalance.add(amount);
		
		gl.setBalance(newBalance);
		generalLedgerRepository.save(gl);
		
		return "Debited Rs." + amount + " to the Asset GL: " + glNum + "\nUpdated Balance is " + newBalance;
		
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
	public String creditAssetGL(Integer glNum, BigDecimal amount, String ccyCode) {
		
		if (!validateGL(glNum, ccyCode)) {
			return null;
		}
		
		GeneralLedger gl = generalLedgerRepository.findById(glNum).orElseThrow(
				() -> new ResourceNotFoundException("GL " + glNum + " not found"));
		
		BigDecimal currBalance = gl.getBalance();
		int comparison = currBalance.compareTo(amount);
		
		if (comparison<0) {
			throw new InsufficientFundsException("Asset GL " + glNum + " is not having sufficient funds");
			// return "Asset GL " + glNum + " is not having sufficient funds";
		}
		
		BigDecimal newBalance = currBalance.subtract(amount);
		
		gl.setBalance(newBalance);
		generalLedgerRepository.save(gl);
		
		return "Credited Rs." + amount + " from the Asset GL: " + glNum + "\nUpdated Balance is " + newBalance;
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
	public String creditLiabilityGL(Integer glNum, BigDecimal amount, String ccyCode) {
		
		if (!validateGL(glNum, ccyCode)) {
			return null;
		}
		
		GeneralLedger gl = generalLedgerRepository.findById(glNum).orElseThrow(
				() -> new ResourceNotFoundException("GL " + glNum + " not found"));
		
		BigDecimal currBalance = gl.getBalance();
		BigDecimal newBalance = currBalance.add(amount);
		
		gl.setBalance(newBalance);
		generalLedgerRepository.save(gl);
		
		return "Debited Rs." + amount + " to the Liability GL: " + glNum + "\nUpdated Balance is " + newBalance;
		
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
	public String debitLiabilityGL(Integer glNum, BigDecimal amount, String ccyCode) {
		
		if (!validateGL(glNum, ccyCode)) {
			return null;
		}
		
		GeneralLedger gl = generalLedgerRepository.findById(glNum).orElseThrow(
				() -> new ResourceNotFoundException("GL " + glNum + " not found"));
		
		BigDecimal currBalance = gl.getBalance();
		int comparison = currBalance.compareTo(amount);
		
		if (comparison<0) {
			throw new InsufficientFundsException("Liability GL " + glNum + " is not having sufficient funds");
			// return "Liability GL " + glNum + " is not having sufficient funds";
		}
		
		BigDecimal newBalance = currBalance.subtract(amount);
		
		gl.setBalance(newBalance);
		generalLedgerRepository.save(gl);
		
		return "Credited Rs." + amount + " from the Liability GL: " + glNum + "\nUpdated Balance is " + newBalance;
	}
	
	public String fetchBalance(Integer glNum) {
		GeneralLedger gl = generalLedgerRepository.findById(glNum).orElseThrow(
				() -> new ResourceNotFoundException("GL " + glNum + " not found"));
		
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

	
	public String deleteGL(Integer glNum) {
		GeneralLedger gl = generalLedgerRepository.findById(glNum).orElseThrow(
				() -> new ResourceNotFoundException("GL " + glNum + " not found"));
		
		if (gl.getGlStatus()=='A') {
			gl.setGlStatus('C');
			return "GL " + glNum + " deletion success";
		}
		
		return "GL " + glNum + " deletion failed";
	}
	
	public GeneralLedger getGLDetails (Integer glNum) {		
		return generalLedgerRepository.findById(glNum).orElseThrow(
				() -> new ResourceNotFoundException("GL " + glNum + " not found"));

	}

}
