package fintrack_monolith.account;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fintrack_monolith.customer.Customer;
import fintrack_monolith.customer.CustomerService;
import fintrack_monolith.exception.AccountInactiveException;
import fintrack_monolith.exception.CurrencyMismatchException;
import fintrack_monolith.exception.InsufficientFundsException;
import fintrack_monolith.exception.KycNotVerifiedException;
import fintrack_monolith.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountService {

	private final AccountRepository accountRepository;
	private final CustomerService customerService;

	public AccountService(AccountRepository accountRepository, CustomerService customerService) {
		this.accountRepository = accountRepository;
		this.customerService = customerService;
	}

	private Account findAccountEntity(Integer accountNum) {
		log.info("Inside findAccountEntity for Account {}", accountNum);
		return accountRepository.findById(accountNum).orElseThrow(
				() -> new ResourceNotFoundException("Account not found with Account Number: " + accountNum));
	}

	private void isAccountActive(Account account) {
		log.info("Inside isAccountActive");
		
		String errorMessage;
		switch(account.getAccountStatus()) {
		case 'A':
			errorMessage = null;
			break;
		case 'C':
			errorMessage = "closed";
			break;
		case 'D':
			errorMessage = "dormant";
			break;
		case 'H':
			errorMessage = "on hold";
			break;
		case 'I':
			errorMessage = "inactive";
			break;
		default:
			errorMessage = "in invalid state";
		}
		
		if (errorMessage != null) {
			throw new AccountInactiveException("Account " + account.getAccountNum() + " is " + errorMessage);
		}
		
		log.info("Account {} is active", account.getAccountNum());
		log.info("returning from isAccountActive");
	}
	
	private void validateCurrencyMatch(Integer accountNum, String accountCcy, String transactionCcy) {
		log.info("Inside validateCurrencyMatch");
		if(accountCcy != transactionCcy) {
			log.warn("CURRENCY MISMATCH");
			// log.warn("Account {}'s currency - {} and Transaction currency - {}", accountNum, accountCcy, transactionCcy);
			throw new CurrencyMismatchException("Account ccy is " + accountCcy + " and Transaction ccy is " + transactionCcy);
		}
		
		log.info("returning from validateCurrencyMatch");
	}


	@Transactional
	public Account createAccount(Account accountDetails) {

		log.info("Inside createAccount");
		Customer customer = customerService.getCustomerById(accountDetails.getCustomerId());
		if (!customer.getKycStatus()) {
			throw new KycNotVerifiedException("KYC verification pending for customer ID: " + customer.getCustId());
		}

		accountDetails.setBalance(BigDecimal.ZERO);
		accountDetails.setAccountStatus('A');
		log.info("creating account for customer: {}", accountDetails.getCustomerId());
		log.info("returning from createAccount");
		return accountRepository.save(accountDetails);
	}

	public void closeAccount(Integer accountNum) {
		
		log.info("Inside closeAccount");
		Account acc = findAccountEntity(accountNum);
		
		isAccountActive(acc);
		acc.setAccountStatus('C');
		accountRepository.save(acc);
		log.info("Closed account {}", accountNum);
		
		log.info("returning from closeAccount");
	}

	public BigDecimal fetchBalance(Integer accountNum) {
		log.info("Inside fetchBalance");
		BigDecimal accountBalance = findAccountEntity(accountNum).getBalance();
		log.info("Account: {} balance is {}", accountNum, accountBalance);
		log.info("returning from fetchBalance");
		return accountBalance;
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public BigDecimal debit(Integer accountNum, BigDecimal amount, String transactionCcy) {
		
		log.info("Inside debit");

		Account acc = findAccountEntity(accountNum);
		
		isAccountActive(acc);
		validateCurrencyMatch(accountNum, acc.getCcyCode(), transactionCcy);
		
		BigDecimal currBalance = acc.getBalance();
		log.info("Account " + accountNum + " - balance before debit " + currBalance);
		
		int comparison = currBalance.compareTo(amount);
		if (comparison < 0) {
			throw new InsufficientFundsException("Account " + accountNum + " is not having sufficient funds");
		}

		BigDecimal newBalance = currBalance.subtract(amount);
		acc.setBalance(newBalance);
		accountRepository.save(acc);
		log.info("Account " + accountNum + " - updated balance is " + newBalance);
		
		log.info("returning from debit");
		return newBalance;

	}

	@Transactional(propagation = Propagation.MANDATORY)
	public BigDecimal credit(Integer accountNum, BigDecimal amount, String transactionCcy) {
		
		log.info("Inside credit");

		Account acc = findAccountEntity(accountNum);
		isAccountActive(acc);
		validateCurrencyMatch(acc.getAccountNum(), acc.getCcyCode(), transactionCcy);

		BigDecimal currBalance = acc.getBalance();
		log.info("Account " + accountNum + " - balance before credit " + currBalance);
		BigDecimal newBalance = currBalance.add(amount);

		acc.setBalance(newBalance);
		accountRepository.save(acc);
		log.info("Account " + accountNum + " - updated balance is " + newBalance);
		
		log.info("returning from credit");
		return newBalance;

	}

	@Transactional(readOnly = true)
	public Account getAccountById(Integer accountNum) {
		log.info("Inside getAccountById");
		return findAccountEntity(accountNum);

	}

}
