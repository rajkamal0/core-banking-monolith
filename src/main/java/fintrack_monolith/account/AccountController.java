package fintrack_monolith.account;

import java.math.BigDecimal;

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
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	AccountService accountService;
	
	@PostMapping("/add")
	public Account createAccount(@RequestBody Account accountDetails) {
		return accountService.createAccount(accountDetails);
	}
	
	@DeleteMapping("/delete/{accountNum}")
	public String deleteAccount(@PathVariable Integer accountNum) {
		return accountService.deleteAccount(accountNum);
	}
	
	@GetMapping("checkbalance/{accountNum}")
	public String fetchBalance(@PathVariable Integer accountNum) {
		return accountService.fetchBalance(accountNum);
	}
	
	@PutMapping("debit/{accountNum}")
	public String debitBalance(@PathVariable Integer accountNum, BigDecimal amount, String ccyCode) {
		return accountService.debitBalance(accountNum, amount, ccyCode);
	}
	
	@PutMapping("credit/{accountNum}")
	public String creditBalance(@PathVariable Integer accountNum, BigDecimal amount, String ccyCode) {
		return accountService.creditBalance(accountNum, amount, ccyCode);
	}
	
	@GetMapping("/findaccount/{accountNum}")
	public Account getAccountDetails(@PathVariable Integer accountNum) {
		return accountService.getAccountDetails(accountNum);
	}
}
