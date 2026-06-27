package fintrack_monolith.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	CustomerService customerService;

	@GetMapping("/fetch/{custID}")
	public Customer getCustomerByID(@PathVariable Integer custID) {
		return customerService.getCustomerByID(custID);
	}
	
	@GetMapping("/fetch")
	public List<Customer> getAllCustomers(){
		return customerService.getAllCustomers();
	}
	
	@PostMapping("/add")
	public Customer addCustomer(@RequestBody Customer customerDetails) {
		return customerService.addCustomer(customerDetails);
	}
	
	@PutMapping("/update")
	public Customer updateCustomer(@RequestBody Customer customerDetails) {
		return customerService.updateCustomer(customerDetails);
	}
	
	@DeleteMapping("/delete/{custID}")
	public String deleteCustomer(@PathVariable Integer custID) {
		return customerService.deleteCustomer(custID);
	}
	
}
