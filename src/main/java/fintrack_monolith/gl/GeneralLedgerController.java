package fintrack_monolith.gl;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gl")
public class GeneralLedgerController {
	
	private final GeneralLedgerService generalLedgerService;

	GeneralLedgerController(GeneralLedgerService generalLedgerService) {
		this.generalLedgerService = generalLedgerService;
	}

	@GetMapping("/checkbalance/{glNum}")
	public BigDecimal fetchBalance(@PathVariable String glNum) {
		return generalLedgerService.fetchBalance(glNum);
	}

}
