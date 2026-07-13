package fintrack_monolith.transaction;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fintrack_monolith.account.AccountService;
import fintrack_monolith.gl.GeneralLedgerService;



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
	

	private Transaction saveTransaction(Integer debitAcc, Integer creditAcc, BigDecimal amount,
										Character txnType, String ccyCode) {
		
		// if transactionType is W --> toAccount should be cust_ac and fromAccount should be GL
		// if transactionType is D --> toAccount should be GL and fromAccount should be cust_ac
		// if transactionType is T --> toAccount & fromAccounts are provided in parameters
		// if transactionType is R --> do a transfer but it is a reversal entry.
		
		Transaction tx = new Transaction();
		
		
		tx.setDebitAccount(debitAcc);
		tx.setCreditAccount(creditAcc);
		tx.setAmount(amount);
		tx.setTxnType(txnType);
		tx.setCcyCode(ccyCode);
		// tx.setTxnID();
		return transactionRepository.save(tx);
	}
	
	@Transactional
	public Transaction deposit(Transaction transaction) {
		
		// transaction starts
		generalLedgerService.debitAssetGL(transaction.getDebitAccount(), transaction.getAmount());
		//debit done
		accountService.creditBalance(transaction.getCreditAccount(), transaction.getAmount());
		//credit done
		
		return saveTransaction(transaction.getDebitAccount(), transaction.getCreditAccount(),
							   transaction.getAmount(), 'D', transaction.getCcyCode());		
	}

	@Transactional
	public Transaction withdrawl(Transaction transaction) {
		
		accountService.debitBalance(transaction.getDebitAccount(), transaction.getAmount());
		generalLedgerService.creditAssetGL(transaction.getCreditAccount(), transaction.getAmount());
		
		return saveTransaction(transaction.getDebitAccount(), transaction.getCreditAccount(),
							   transaction.getAmount(), 'W', transaction.getCcyCode());				
	}

	@Transactional
	public Transaction transfer(Transaction transaction) {
		accountService.debitBalance(transaction.getDebitAccount(), transaction.getAmount());
		accountService.creditBalance(transaction.getCreditAccount(), transaction.getAmount());
		
		return saveTransaction(transaction.getDebitAccount(), transaction.getCreditAccount(),
				   transaction.getAmount(), 'T', transaction.getCcyCode());	
	}

	@Transactional
	public Transaction reversal(int transactionID) {
		
		Transaction originalTransaction = transactionRepository.findById(transactionID).get();
		
		if (originalTransaction.getTxnType()=='R'){
			return null; //"Cannot reverse a reversed transaction" -  throw this error
		}
		return saveTransaction(originalTransaction.getCreditAccount(), originalTransaction.getDebitAccount(),
							   originalTransaction.getAmount(), 'R', originalTransaction.getCcyCode());
		
	}

}
