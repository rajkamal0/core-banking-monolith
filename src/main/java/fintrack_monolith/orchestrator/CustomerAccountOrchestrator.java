package fintrack_monolith.orchestrator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fintrack_monolith.account.AccountService;
import fintrack_monolith.customer.CustomerService;
import fintrack_monolith.exception.CannotCloseCustomerException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerAccountOrchestrator {
	
	private final CustomerService customerService;
    private final AccountService accountService;
    
    public CustomerAccountOrchestrator(CustomerService customerService, AccountService accountService) {
    	this.customerService = customerService;
    	this.accountService = accountService;
    }

    @Transactional
	public void closeCustomer(String customerID) {
		log.info("Inside closeCustomer");
		log.info("Closing customer: {}", customerID);
		
		// accountService.getAccountsByCustomerId(customerID);
		if (accountService.hasActiveAccountsByCustomerId(customerID)) {
			// throw customer has active accounts exception
			throw new CannotCloseCustomerException("Customer " + customerID + " has active accounts");
		}
		
		customerService.closeCustomer(customerID);
		log.info("Customer {} closed from orchestrator", customerID);
		
	}
    
    

}
