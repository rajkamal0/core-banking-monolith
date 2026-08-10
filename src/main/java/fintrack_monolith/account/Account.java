package fintrack_monolith.account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ACCOUNT_DETAILS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator = "a_seq_generator", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "a_seq_generator", sequenceName = "SEQ_ACCOUNT", initialValue = 1, allocationSize = 1)
	private Integer id;
	
	@Column(name="ACCOUNT_NUMBER", unique = true, nullable = false)
	private String accountNum;
	
	@Column(name="ACC_TYPE")
	private Character accountType;
	
	@Column(name="LAST_ACTIVITY")
	@UpdateTimestamp
	private LocalDateTime lastActivityAt;
	
	@Column(name="ACC_STATUS")
	private Character accountStatus;
	
	@Column(name="CUSTOMER_NO")
	private String customerId;
	
	@Column(name="CCY_CODE")
	private String ccyCode;
	
	@Column(name="ACC_BALANCE")
	private BigDecimal balance;
	
	@Version
	@Column(name="VERSION")
	private Integer version;
	
	// for generating account number
	@PrePersist
	public void assignAccountNum() {
//        this.id = accSeqValue;
		if(this.id != null) {
			this.accountNum = String.format("A%07d", this.id);
		}
        
    }
	
}
