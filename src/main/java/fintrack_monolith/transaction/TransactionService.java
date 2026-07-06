package fintrack_monolith.transaction;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fintrack_monolith.account.AccountService;



@Service
public class TransactionService {
	
	@Autowired
	TransactionRepository transactionRepository;
	
	@Autowired
	AccountService accountService;
	
	@Transactional
	public Transaction processTransaction(int fromAccount, int toAccount, BigDecimal amount, char transactionType) {
		// if transactionType is W --> toAccount should be cust_ac and fromAccount should be GL
		// if transactionType is D --> toAccount should be GL and fromAccount should be cust_ac
		// if transactionType is T --> toAccount & fromAccounts are provided in parameters
		// if transactionType is R --> do a transfer but it is a reversal entry.
		return null;
	}

	@Transactional
	public Transaction deposit(Transaction transaction) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Transactional
	public Transaction withdrawl(Transaction transaction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public Transaction transfer(Transaction transaction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public Transaction reversal(int transactionID) {
		
		Transaction originalTransaction = new Transaction();
		
		if (originalTransaction.getTxnType().equals("R")){
			return null; //"Cannot reverse a reversed transaction" -  throw this error
		}
		
		return processTransaction(originalTransaction.getCreditAccount(),
						   originalTransaction.getDebitAccount(),
						   originalTransaction.getAmount(),
						   originalTransaction.getTxnType()
						   );
		
	}

}
