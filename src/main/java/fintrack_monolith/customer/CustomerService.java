package fintrack_monolith.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

	@Autowired
	CustomerRepository customerRepository;
	
	public Customer getCustomerByID(Integer custID) {
		return customerRepository.findById(custID).get();
	}

	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	public Customer addCustomer(Customer customerDetails) {
		return customerRepository.save(customerDetails);
	}

	public Customer updateCustomer(Customer customerDetails) {
		return customerRepository.save(customerDetails);
	}

	public String deleteCustomer(Integer custID) {
		Customer customerDetails = customerRepository.findById(custID).get();
		String message = "Customer Details: \n Customer ID - " + custID + 
						 "\n Customer Name - " + customerDetails.getFirstname() + " " + customerDetails.getLastname();
		customerRepository.deleteById(custID);
		return message + "\nCustomer deletion succesful";
	}

}
