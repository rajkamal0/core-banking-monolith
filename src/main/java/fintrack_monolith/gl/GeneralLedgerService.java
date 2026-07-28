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
	
	private GeneralLedger getGlEntity(String glNum) {
		log.info("Inside getGlEntity for GL {}", glNum);
		return generalLedgerRepository.findByGlNum(glNum).orElseThrow(
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
	
	private void validateCurrencyMatch(String glNum, String glCcy, String transactionCcy) {
		log.info("Inside validateCurrencyMatch");
		if(glCcy != transactionCcy) {
			log.warn("CURRENCY MISMATCH");
			throw new CurrencyMismatchException("General Ledger " + glNum + " ccy is " + glCcy + " and Transaction ccy is " + transactionCcy);
		}
		
		log.info("returning from validateCurrencyMatch");
	}
	 
	
	@Transactional(propagation = Propagation.MANDATORY)
	public BigDecimal debitAssetGL(String glNum, BigDecimal amount, String transactionCcy) {
		
		log.info("Inside debitAssetGL");
		
		GeneralLedger gl = getGlEntity(glNum);
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
	public BigDecimal creditAssetGL(String glNum, BigDecimal amount, String transactionCcy) {
		
		log.info("Inside creditAssetGL");
		
		GeneralLedger gl = getGlEntity(glNum);
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
	public BigDecimal creditLiabilityGL(String glNum, BigDecimal amount, String transactionCcy) {
		
log.info("Inside creditLiabilityGL");
		
		GeneralLedger gl = getGlEntity(glNum);
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
	public BigDecimal debitLiabilityGL(String glNum, BigDecimal amount, String transactionCcy) {
		
		log.info("Inside debitLiabilityGL");
		
		GeneralLedger gl = getGlEntity(glNum);
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
	
	public BigDecimal fetchBalance(String glNum) {
		log.info("Inside fetchBalance");
		BigDecimal glBalance = getGlEntity(glNum).getBalance();
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

	
	public void closeGl(String glNum) {
		log.info("Inside closeGl");
		GeneralLedger gl = getGlEntity(glNum);
		
		isGlActive(gl);
		gl.setGlStatus('C');
		generalLedgerRepository.save(gl);
		log.info("Closed GL {}", glNum);
		
		log.info("returning from closeGl");
	}
	
	@Transactional(readOnly = true)
	public GeneralLedger getGlByGlNum (String glNum) {	
		log.info("Inside getGlByGlNum");
		return getGlEntity(glNum);

	}

}
