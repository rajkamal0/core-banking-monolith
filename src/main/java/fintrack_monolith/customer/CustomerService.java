package fintrack_monolith.customer;

import java.util.List;

import org.springframework.stereotype.Service;

import fintrack_monolith.exception.ResourceNotFoundException;

@Service
public class CustomerService {

	private CustomerRepository customerRepository;
	
	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}
	
	public Customer getCustomerByID(Integer custID) {
		return customerRepository.findById(custID).orElseThrow(
				() -> new ResourceNotFoundException("Customer " + custID + " not found"));
	}

	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	public Customer addCustomer(Customer customerDetails) {
		return customerRepository.save(customerDetails);
	}

	public Customer updateCustomer(Customer updatedCustomerDetails) {
		
		Customer existingDetails = customerRepository.findById(updatedCustomerDetails.getCustID()).orElseThrow(
				() -> new ResourceNotFoundException("Customer " + updatedCustomerDetails.getCustID() + " not found"));
		
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
		
		return customerRepository.save(existingDetails);
	}

	public String deleteCustomer(Integer custID) {
		Customer customerDetails = customerRepository.findById(custID).orElseThrow(
				() -> new ResourceNotFoundException("Customer " + custID + " not found"));
		String message = "Customer Details: \n Customer ID - " + custID + 
						 "\n Customer Name - " + customerDetails.getFirstname() + " " + customerDetails.getLastname();
		customerRepository.deleteById(custID);
		return message + "\nCustomer deletion succesful";
	}

}
