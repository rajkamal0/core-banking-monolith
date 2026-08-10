package fintrack_monolith.customer;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fintrack_monolith.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerService {

	private CustomerRepository customerRepository;
	
	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}
	
	private Customer getCustomerEntity(String customerId) {
		log.info("Inside getCustomerEntity for Customer {}", customerId);
		return customerRepository.findByCustId(customerId).orElseThrow(
				() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));
	}
	
	@Transactional(readOnly = true)
	public Customer getCustomerByCustId(String customerID) {
		log.info("Inside getCustomerByCustId");
		log.info("customer ID: {}", customerID);
		return getCustomerEntity(customerID);
	}

	@Transactional(readOnly = true)
	public List<Customer> getAllCustomers() {
		log.info("Inside getAllCustomers");
		return customerRepository.findAll();
	}

	@Transactional
	public Customer createCustomer(Customer customerDetails) {
		log.info("Inside addCustomer");
//		Integer nextCustSeq = customerRepository.findNextCustomerSequence();
//		customerDetails.assignCustId(nextCustSeq);
		log.info("creating customer: " + customerDetails.getFirstname() + " " + customerDetails.getLastname());
		
		return customerRepository.save(customerDetails);
	}

	@Transactional
	public Customer updateCustomer(String customerID, Customer updatedCustomerDetails) {
		
		log.info("Inside updateCustomer");
		log.info("Customer ID: {}", customerID);
		
		Customer existingDetails = getCustomerEntity(customerID);
		
		if(updatedCustomerDetails.getKycStatus() != null) {
			existingDetails.setKycStatus(updatedCustomerDetails.getKycStatus());
		}
		
		if(updatedCustomerDetails.getAddressLine1() != null) {
			existingDetails.setAddressLine1(updatedCustomerDetails.getAddressLine1());
		}
		
		if(updatedCustomerDetails.getCountry() != null) {
			existingDetails.setCountry(updatedCustomerDetails.getCountry());
		}
		
		if(updatedCustomerDetails.getEmail() != null) {
			existingDetails.setEmail(updatedCustomerDetails.getEmail());
		}
		
		if(updatedCustomerDetails.getFirstname() != null) {
			existingDetails.setFirstname(updatedCustomerDetails.getFirstname());
		}
		
		if(updatedCustomerDetails.getLastname() != null) {
			existingDetails.setLastname(updatedCustomerDetails.getLastname());
		}
		
		if(updatedCustomerDetails.getMobileNum() != null) {
			existingDetails.setMobileNum(updatedCustomerDetails.getMobileNum());
		}
		
		if(updatedCustomerDetails.getPincode() != null) {
			existingDetails.setPincode(updatedCustomerDetails.getPincode());
		}
		
		if(updatedCustomerDetails.getIsActive() != null) {
			existingDetails.setIsActive(updatedCustomerDetails.getIsActive());
		}
		
		log.info("updating customer " + customerID);
		return customerRepository.save(existingDetails);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void closeCustomer(String customerID) {
		
		log.info("Inside closeCustomer");
		log.info("Closing customer: {}", customerID);
		
		/*
		if (!customerRepository.existsByCustId(customerID)) {
			throw new ResourceNotFoundException("Customer " + customerID + " not found");
		}
		
		Customer customer = getCustomerByCustId(customerID);
		*/
		
		Customer customer = customerRepository.findByCustId(customerID).orElseThrow(() -> new ResourceNotFoundException("Customer " + customerID + " not found"));
		customer.setIsActive(false);
		customerRepository.save(customer);
		
		// customerRepository.deleteById(customerID);
		// log.info("closing a customer requires active account check for which an orchestrator is recommeneded. Returning a success for now");
		
		log.info("Customer {} closed", customerID);
	}

}
