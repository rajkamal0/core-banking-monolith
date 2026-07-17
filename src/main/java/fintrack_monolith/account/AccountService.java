package fintrack_monolith.account;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fintrack_monolith.customer.Customer;
import fintrack_monolith.customer.CustomerRepository;
import fintrack_monolith.exception.InsufficientFundsException;
import fintrack_monolith.exception.ResourceNotFoundException;
import fintrack_monolith.transaction.Transaction;


@Service
public class AccountService {
	
	private final AccountRepository accountRepository;
	private final CustomerRepository customerRepository;
	
	public AccountService (AccountRepository accountRepository, CustomerRepository customerRepository) {
		this.accountRepository = accountRepository;
		this.customerRepository = customerRepository;
	}
	
	private boolean validateAccount(Integer accountNum, String ccy) {
		Account acc = accountRepository.findById(accountNum).orElseThrow(
				() -> new ResourceNotFoundException("Account not found with Account Number: " + accountNum));
		
		if (acc.getAccountStatus() != 'A') {
			return false;
		}
		if(!acc.getCcyCode().equals(ccy)) {
			return false;
		}
		return true;
	}
	
	@Transactional
	public Account createAccount(Account accountDetails) {
		
		Customer customer = customerRepository.findById(accountDetails.getCustomerID()).orElseThrow(
								() -> new ResourceNotFoundException("Account not found with Account Number: " + accountDetails.getAccountNum()));
		if (!customer.getKycStatus()) {
			return null;
		}
		
		accountDetails.setBalance(BigDecimal.ZERO);
		accountDetails.setAccountStatus('A');
		return accountRepository.save(accountDetails);
	}

	
	public String deleteAccount(Integer accountNum) {
		Account acc = accountRepository.findById(accountNum).orElseThrow(
				() -> new ResourceNotFoundException("Account not found with Account Number: " + accountNum));
		
		if (acc.getAccountStatus()=='A') {
			acc.setAccountStatus('C');
			return "Account " + accountNum + " deletion success";
		}
		
		return "Account " + accountNum + " deletion failed";
	}

	public String fetchBalance(Integer accountNum) {
		Account acc = accountRepository.findById(accountNum).orElseThrow(
				() -> new ResourceNotFoundException("Account not found with Account Number: " + accountNum));
		
		if (acc != null && acc.getAccountNum() != null)
			return "Balance in Account " + accountNum + " is " + acc.getBalance();
		 return "Balance check failed for Account " + accountNum;
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public String debitBalance(Integer accountNum, BigDecimal amount, String ccyCode) {
		
		if (!validateAccount(accountNum, ccyCode)) {
			return "Account " + accountNum + " failed in validation";
		}
		
		Account acc = accountRepository.findById(accountNum).orElseThrow(
				() -> new ResourceNotFoundException("Account not found with Account Number: " + accountNum));
		
		BigDecimal currBalance = acc.getBalance();
		int comparison = currBalance.compareTo(amount);
		
		if (comparison<0) {
			throw new InsufficientFundsException("Account " + accountNum + " is not having sufficient funds");
			// return "Account " + accountNum + " is not having sufficient funds";
		}
		
		BigDecimal newBalance = currBalance.subtract(amount);
		
		acc.setBalance(newBalance);
		accountRepository.save(acc);
		
		return "Debited Rs." + amount + " from the account: " + accountNum + "\nUpdated Balance is " + newBalance;
		
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
	public String creditBalance(Integer accountNum, BigDecimal amount, String ccyCode) {
		
		if (!validateAccount(accountNum, ccyCode)) {
			return "Account " + accountNum + " failed in validation";
		}
		
		Account acc = accountRepository.findById(accountNum).orElseThrow(
				() -> new ResourceNotFoundException("Account not found with Account Number: " + accountNum));
		
		BigDecimal currBalance = acc.getBalance();
		BigDecimal newBalance = currBalance.add(amount);
		
		acc.setBalance(newBalance);
		accountRepository.save(acc);
		
		return "Credited Rs." + amount + " to the account: " + accountNum + "\nUpdated Balance is " + newBalance;
		
	}
	
	public Account getAccountDetails (Integer accountNum) {		
		return accountRepository.findById(accountNum).orElseThrow(
				() -> new ResourceNotFoundException("Account " + accountNum + " not found"));

	}

}
