package fintrack_monolith.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

	@GetMapping("/get/{customerID}")
	public Customer getCustomerByCustId(@PathVariable String customerID) {
		return customerService.getCustomerByCustId(customerID);
	}
	
	@GetMapping("/get/all")
	public List<Customer> getAllCustomers(){
		return customerService.getAllCustomers();
	}
	
	@PostMapping("/create")
	public Customer createCustomer(@RequestBody Customer customerDetails) {
		return customerService.createCustomer(customerDetails);
	}
	
	@PutMapping("/update/{customerID}")
	public Customer updateCustomer(@PathVariable String customerID, @RequestBody Customer customerDetails) {
		return customerService.updateCustomer(customerID, customerDetails);
	}
	
	@PutMapping("/close/{customerID}")
	public ResponseEntity<Void> closeCustomer(@PathVariable String customerID) {
		customerService.closeCustomer(customerID);
		return ResponseEntity.noContent().build();
	}
	
}
