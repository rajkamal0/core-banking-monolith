package fintrack_monolith.customer;

import java.util.List;

import org.springframework.stereotype.Service;
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
	
	private Customer findCustomerEntity(Integer customerId) {
		log.info("Inside findCustomerEntity for Customer {}", customerId);
		return customerRepository.findById(customerId).orElseThrow(
				() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));
	}
	
	@Transactional(readOnly = true)
	public Customer getCustomerById(Integer customerID) {
		log.info("Inside getCustomerByID");
		log.info("customer ID: {}", customerID);
		return findCustomerEntity(customerID);
	}

	public List<Customer> getAllCustomers() {
		log.info("Inside getAllCustomers");
		return customerRepository.findAll();
	}

	public Customer createCustomer(Customer customerDetails) {
		log.info("Inside addCustomer");
		log.info("creating customer: " + customerDetails.getFirstname() + " " + customerDetails.getLastname());
		return customerRepository.save(customerDetails);
	}

	public Customer updateCustomer(Integer customerID, Customer updatedCustomerDetails) {
		
		log.info("Inside updateCustomer");
		log.info("Customer ID: {}", customerID);
		
		Customer existingDetails = findCustomerEntity(customerID);
		
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
		
		log.info("updating customer " + customerID);
		return customerRepository.save(existingDetails);
	}

	public void closeCustomer(Integer customerID) {
		
		log.info("Inside closeCustomer");
		log.info("Closing customer: {}", customerID);
		if (!customerRepository.existsById(customerID)) {
			throw new ResourceNotFoundException("Customer " + customerID + " not found");
		}
		// customerRepository.deleteById(customerID);
		log.info("closing a customer requires active account check for which an orchestrator is recommeneded. Returning a success for now");
		log.info("Customer {} closed", customerID);
	}

}
