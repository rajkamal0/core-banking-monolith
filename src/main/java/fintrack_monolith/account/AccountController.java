package fintrack_monolith.account;

import java.math.BigDecimal;

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
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	AccountService accountService;
	
	@PostMapping("/create")
	public Account createAccount(@RequestBody Account accountDetails) {
		return accountService.createAccount(accountDetails);
	}
	
	@PutMapping("/close/{accountNum}")
	public ResponseEntity<Void> closeAccount(@PathVariable String accountNum) {
		accountService.closeAccount(accountNum);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/checkbalance/{accountNum}")
	public BigDecimal fetchBalance(@PathVariable String accountNum) {
		return accountService.fetchBalance(accountNum);
	}
	
	@PutMapping("/debit/{accountNum}")
	public BigDecimal debit(@PathVariable String accountNum, BigDecimal amount, String ccyCode) {
		return accountService.debit(accountNum, amount, ccyCode);
	}
	
	@PutMapping("/credit/{accountNum}")
	public BigDecimal credit(@PathVariable String accountNum, BigDecimal amount, String ccyCode) {
		return accountService.credit(accountNum, amount, ccyCode);
	}
	
	@GetMapping("/get/{accountNum}")
	public Account getAccountByAccountNum(@PathVariable String accountNum) {
		return accountService.getAccountByAccountNum(accountNum);
	}
}
