package fintrack_monolith.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
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
	@Column(name="ID")
	@GeneratedValue(generator = "t_seq_generator", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "t_seq_generator", sequenceName = "SEQ_TRANSACTION", initialValue = 1, allocationSize = 1)
	private Integer id;
	
	@Column(name="TRANSACTION_ID", unique = true, nullable = false)
	private String txnID;
	
	@Column(name="DEBIT_ACCOUNT")
	private String debitAccount;
	
	@Column(name="CREDIT_ACCOUNT")
	private String creditAccount;
	
	@Column(name="TXN_AMT")
	private BigDecimal amount;
	
	@Column(name="CCY_CODE")
	private String ccyCode;
	
	@Column(name="TXN_TYPE")
	private Character txnType;
	
	@UpdateTimestamp
	@Column(name="TXN_TIME")
	private LocalDateTime txnTime;
	
	public void assignTxnId() {
//        this.id = txnSeqValue;
		if(this.id != null) {
			this.txnID = String.format("T%07d", this.id);
		}
        
    }

}
