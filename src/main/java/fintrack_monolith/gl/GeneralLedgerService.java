package fintrack_monolith.gl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fintrack_monolith.exception.CurrencyMismatchException;
import fintrack_monolith.exception.GlInactiveException;
import fintrack_monolith.exception.InsufficientFundsException;
import fintrack_monolith.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GeneralLedgerService {
	
	private GeneralLedgerRepository generalLedgerRepository;
	
	public GeneralLedgerService(GeneralLedgerRepository generalLedgerRepository) {
		this.generalLedgerRepository = generalLedgerRepository;
	}
	
	private GeneralLedger findGlEntity(Integer glNum) {
		log.info("Inside findGlEntity for GL {}", glNum);
		return generalLedgerRepository.findById(glNum).orElseThrow(
				() -> new ResourceNotFoundException("GL not found with GL Number: " + glNum));
	}
	
	private void isGlActive(GeneralLedger generalLedger) {
		log.info("Inside isGlActive");
		
		if(generalLedger.getGlStatus()!='A') {
			throw new GlInactiveException("GL " + generalLedger.getGlNum() + " is invalid");
		}
		
		log.info("General Ledger {} is active", generalLedger.getGlNum());
		log.info("returning from isGlActive");
	}
	
	private void validateCurrencyMatch(Integer glNum, String glCcy, String transactionCcy) {
		log.info("Inside validateCurrencyMatch");
		if(glCcy != transactionCcy) {
			log.warn("CURRENCY MISMATCH");
			throw new CurrencyMismatchException("General Ledger ccy is " + glCcy + " and Transaction ccy is " + transactionCcy);
		}
		
		log.info("returning from validateCurrencyMatch");
	}
	 
	
	@Transactional(propagation = Propagation.MANDATORY)
	public BigDecimal debitAssetGL(Integer glNum, BigDecimal amount, String transactionCcy) {
		
		log.info("Inside debitAssetGL");
		
		GeneralLedger gl = findGlEntity(glNum);
		isGlActive(gl);
		validateCurrencyMatch(glNum, gl.getCcyCode(), transactionCcy);
		
		BigDecimal currBalance = gl.getBalance();
		log.info("GL " + glNum + " - balance before debit " + currBalance);
		
		BigDecimal newBalance = currBalance.add(amount);
		
		gl.setBalance(newBalance);
		generalLedgerRepository.save(gl);
		
		log.info("returning from debit");
		return newBalance;
		
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
	public BigDecimal creditAssetGL(Integer glNum, BigDecimal amount, String transactionCcy) {
		
		log.info("Inside creditAssetGL");
		
		GeneralLedger gl = findGlEntity(glNum);
		isGlActive(gl);
		validateCurrencyMatch(glNum, gl.getCcyCode(), transactionCcy);
		
		BigDecimal currBalance = gl.getBalance();
		log.info("GL " + glNum + " - balance before credit " + currBalance);
		
		int comparison = currBalance.compareTo(amount);
		
		if (comparison<0) {
			throw new InsufficientFundsException("Asset GL " + glNum + " is not having sufficient funds");
		}
		
		BigDecimal newBalance = currBalance.subtract(amount);
		
		gl.setBalance(newBalance);
		generalLedgerRepository.save(gl);
		
		log.info("returning from credit");
		return newBalance;
		
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
	public BigDecimal creditLiabilityGL(Integer glNum, BigDecimal amount, String transactionCcy) {
		
log.info("Inside creditLiabilityGL");
		
		GeneralLedger gl = findGlEntity(glNum);
		isGlActive(gl);
		validateCurrencyMatch(glNum, gl.getCcyCode(), transactionCcy);
		
		BigDecimal currBalance = gl.getBalance();
		log.info("GL " + glNum + " - balance before credit " + currBalance);
		
		BigDecimal newBalance = currBalance.add(amount);
		
		gl.setBalance(newBalance);
		generalLedgerRepository.save(gl);
		
		log.info("returning from credit");
		return newBalance;
		
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
	public BigDecimal debitLiabilityGL(Integer glNum, BigDecimal amount, String transactionCcy) {
		
		log.info("Inside debitLiabilityGL");
		
		GeneralLedger gl = findGlEntity(glNum);
		isGlActive(gl);
		validateCurrencyMatch(glNum, gl.getCcyCode(), transactionCcy);
		
		BigDecimal currBalance = gl.getBalance();
		log.info("GL " + glNum + " - balance before debit " + currBalance);
		
		int comparison = currBalance.compareTo(amount);
		
		if (comparison<0) {
			throw new InsufficientFundsException("Liability GL " + glNum + " is not having sufficient funds");
		}
		
		BigDecimal newBalance = currBalance.subtract(amount);
		
		gl.setBalance(newBalance);
		generalLedgerRepository.save(gl);
		
		log.info("returning from debit");
		return newBalance;
		
	}
	
	public BigDecimal fetchBalance(Integer glNum) {
		log.info("Inside fetchBalance");
		BigDecimal glBalance = findGlEntity(glNum).getBalance();
		log.info("GL: {} balance is {}", glNum, glBalance);
		log.info("returning from fetchBalance");
		return glBalance;
	}
	
	@Transactional
	public GeneralLedger createGl(GeneralLedger GLDetails) {
		
		log.info("Inside createGL");
		GLDetails.setBalance(BigDecimal.ZERO);
		GLDetails.setGlStatus('A');
		log.info("returning from createGL");
		return generalLedgerRepository.save(GLDetails);
	}

	
	public void closeGl(Integer glNum) {
		log.info("Inside closeGl");
		GeneralLedger gl = findGlEntity(glNum);
		
		isGlActive(gl);
		gl.setGlStatus('C');
		generalLedgerRepository.save(gl);
		log.info("Closed GL {}", glNum);
		
		log.info("returning from closeGl");
	}
	
	public GeneralLedger getGlById (Integer glNum) {	
		log.info("Inside getGlById");
		return findGlEntity(glNum);

	}

}
