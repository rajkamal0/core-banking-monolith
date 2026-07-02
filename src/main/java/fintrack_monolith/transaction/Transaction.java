package fintrack_monolith.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="TRANSACTION_DETAILS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
	
	@Id
	@Column(name="TRANSACTION_ID")
	private Integer txnID;
	
	@Column(name="DEBIT_ACCOUNT")
	private Integer debitAccount;
	
	@Column(name="CREDIT_ACCOUNT")
	private Integer creditAccount;
	
	@Column(name="TXN_AMT")
	private BigDecimal amount;
	
	@Column(name="CCY_CODE")
	private String ccyCode;
	
	@Column(name="TXN_TYPE")
	private Character txnType;
	
	@UpdateTimestamp
	@Column(name="TXN_TIME")
	private LocalDateTime txnTime;

}
