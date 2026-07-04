package fintrack_monolith.account;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fintrack_monolith.customer.Customer;
import fintrack_monolith.customer.CustomerRepository;


@Service
public class AccountService {
	
	private final AccountRepository accountRepository;
	private final CustomerRepository customerRepository;
	
	public AccountService (AccountRepository accountRepository, CustomerRepository customerRepository) {
		this.accountRepository = accountRepository;
		this.customerRepository = customerRepository;
	}
	
	@Transactional
	public Account createAccount(Account accountDetails) {
		
		Customer customer = customerRepository.findById(accountDetails.getCustomerID()).get();
		if (!customer.getKycStatus()) {
			return null;
		}
		
		accountDetails.setBalance(BigDecimal.ZERO);
		accountDetails.setAccountStatus('A');
		return accountRepository.save(accountDetails);
	}

	
	public String deleteAccount(Integer accountNum) {
		Account acc = accountRepository.findById(accountNum).get();
		
		if (acc.getAccountStatus()=='A') {
			acc.setAccountStatus('C');
			return "Account " + accountNum + " deletion success";
		}
		
		return "Account " + accountNum + " deletion failed";
	}

	public String fetchBalance(Integer accountNum) {
		Account acc = accountRepository.findById(accountNum).get();
		
		if (acc != null && acc.getAccountNum() != null)
			return "Balance in Account " + accountNum + " is " + acc.getBalance();
		return "Balance check failed for Account " + accountNum;
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public String debitBalance(Integer accountNum, BigDecimal amount) {
		Account acc = accountRepository.findById(accountNum).get();
		
		if (acc.getAccountStatus() != 'A') {
			return "Account " + accountNum + " is not active";
		}
		
		BigDecimal currBalance = acc.getBalance();
		int comparison = currBalance.compareTo(amount);
		
		if (comparison<0) {
			return "Account " + accountNum + " is not having sufficient funds";
		}
		
		BigDecimal newBalance = currBalance.subtract(amount);
		return "Debited Rs." + amount + " from the account: " + accountNum + "\nUpdated Balance is " + newBalance;
		
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
	public String creditBalance(Integer accountNum, BigDecimal amount) {
		Account acc = accountRepository.findById(accountNum).get();
		
		if (acc.getAccountStatus() != 'A') {
			return "Account " + accountNum + " is not active";
		}
		
		BigDecimal currBalance = acc.getBalance();
		BigDecimal newBalance = currBalance.add(amount);
		return "Credited Rs." + amount + " to the account: " + accountNum + "\nUpdated Balance is " + newBalance;
		
	}

}
