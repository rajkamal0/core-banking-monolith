package fintrack_monolith.transaction;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fintrack_monolith.account.AccountService;
import fintrack_monolith.exception.CannotReverseTransactionException;
import fintrack_monolith.exception.IncorrectTransactionTypeException;
import fintrack_monolith.exception.ResourceNotFoundException;
import fintrack_monolith.gl.GeneralLedgerService;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class TransactionService {
	
	// Constructor injection
	private final TransactionRepository transactionRepository;
	private final AccountService accountService;
	private final GeneralLedgerService generalLedgerService;
	
	public TransactionService(TransactionRepository transactionRepository, 
		   AccountService accountService,
		   GeneralLedgerService generalLedgerService) {
		
		this.transactionRepository = transactionRepository;
		this.accountService = accountService;
		this.generalLedgerService = generalLedgerService;
	};
	//
	

	private Transaction saveTransaction(String debitAcc, String creditAcc, BigDecimal amount,
										Character txnType, String txnCcy) {
		
		// if transactionType is W --> toAccount should be cust_ac and fromAccount should be GL
		// if transactionType is D --> toAccount should be GL and fromAccount should be cust_ac
		// if transactionType is T --> toAccount & fromAccounts are provided in parameters
		// if transactionType is R --> do a transfer but it is a reversal entry.
		log.info("Inside saveTransaction");
		Transaction tx = new Transaction();
		
//		Integer nextTxnSeq = transactionRepository.getNextAccountSequence();
//		tx.assignTxnId(nextTxnSeq);
		tx.setDebitAccount(debitAcc);
		tx.setCreditAccount(creditAcc);
		tx.setAmount(amount);
		tx.setTxnType(txnType);
		tx.setCcyCode(txnCcy);
		log.info("returning from saveTransaction");
		return transactionRepository.save(tx);
	}
	
	@Transactional
	public Transaction deposit(Transaction transaction) {
		
		// transaction starts
		log.info("Inside deposit");
		generalLedgerService.debitAssetGL(transaction.getDebitAccount(), transaction.getAmount(), transaction.getCcyCode());
		//debit done
		accountService.credit(transaction.getCreditAccount(), transaction.getAmount(), transaction.getCcyCode());
		//credit done
		
		log.info("returning from deposit");
		return saveTransaction(transaction.getDebitAccount(), transaction.getCreditAccount(),
							   transaction.getAmount(), 'D', transaction.getCcyCode());		
	}

	@Transactional
	public Transaction withdrawal(Transaction transaction) {
		log.info("Inside withdrawal");
		accountService.debit(transaction.getDebitAccount(), transaction.getAmount(), transaction.getCcyCode());
		generalLedgerService.creditAssetGL(transaction.getCreditAccount(), transaction.getAmount(), transaction.getCcyCode());
		
		log.info("returning from withdrawal");
		return saveTransaction(transaction.getDebitAccount(), transaction.getCreditAccount(),
							   transaction.getAmount(), 'W', transaction.getCcyCode());				
	}

	@Transactional
	public Transaction transfer(Transaction transaction) {
		
		log.info("Inside transfer");
		accountService.debit(transaction.getDebitAccount(), transaction.getAmount(), transaction.getCcyCode());
		accountService.credit(transaction.getCreditAccount(), transaction.getAmount(), transaction.getCcyCode());
		log.info("returning from transfer");
		return saveTransaction(transaction.getDebitAccount(), transaction.getCreditAccount(),
				   transaction.getAmount(), 'T', transaction.getCcyCode());	
	}

	@Transactional
	public Transaction reversal(Integer transactionID) {
		
		log.info("Inside reversal");
		Transaction originalTransaction = getTransactionByTxnID(transactionID);
		
		if (originalTransaction.getTxnType()=='R'){
			throw new CannotReverseTransactionException("Cannot reverse a reversed transaction " + transactionID);
		}
		
	    char originalType = originalTransaction.getTxnType();
	    
	    if (originalType == 'D') { 
	        generalLedgerService.creditAssetGL(originalTransaction.getDebitAccount(), originalTransaction.getAmount(), originalTransaction.getCcyCode());
	        accountService.debit(originalTransaction.getCreditAccount(), originalTransaction.getAmount(), originalTransaction.getCcyCode());
	        
	    } else if (originalType == 'W') { 
	        accountService.credit(originalTransaction.getDebitAccount(), originalTransaction.getAmount(), originalTransaction.getCcyCode());
	        generalLedgerService.debitAssetGL(originalTransaction.getCreditAccount(), originalTransaction.getAmount(), originalTransaction.getCcyCode());
	        
	    } else if (originalType == 'T') { 
	        accountService.credit(originalTransaction.getDebitAccount(), originalTransaction.getAmount(), originalTransaction.getCcyCode());
	        accountService.debit(originalTransaction.getCreditAccount(), originalTransaction.getAmount(), originalTransaction.getCcyCode());
	    }
	    else {
	    	throw new IncorrectTransactionTypeException("Incorrect transaction type: " + originalType);
	    }
	    
	    log.info("returning from reversal");
		return saveTransaction(originalTransaction.getCreditAccount(), originalTransaction.getDebitAccount(),
							   originalTransaction.getAmount(), 'R', originalTransaction.getCcyCode());
		
	}
	
	@Transactional(readOnly = true)
	public Transaction getTransactionByTxnID (Integer transactionID) {		
		log.info("Inside getTransactionByTxnID");
		return transactionRepository.findById(transactionID).orElseThrow(
				() -> new ResourceNotFoundException("Transaction " + transactionID + " not found"));

	}

}
