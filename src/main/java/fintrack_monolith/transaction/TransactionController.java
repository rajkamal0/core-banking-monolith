package fintrack_monolith.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
	
	@Autowired
	TransactionService transactionService;
	
	@PostMapping("/deposit")
	public Transaction deposit(@RequestBody Transaction transaction) {
		return transactionService.deposit(transaction);
	}
	
	@PostMapping("/withdrawl")
	public Transaction withdrawl(@RequestBody Transaction transaction) {
		return transactionService.withdrawl(transaction);
	}
	
	@PostMapping("/transfer")
	public Transaction transfer(@RequestBody Transaction transaction) {
		return transactionService.transfer(transaction);
	}
	
	@PostMapping("/reversal/{transactionID}")
	public Transaction reversal(@PathVariable int transactionID) {
		return transactionService.reversal(transactionID);
	}
	
}
