package fintrack_monolith.account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
	@Column(name="ACCOUNT_NUMBER")
	private Integer accountNum;
	
	@Column(name="ACC_TYPE")
	private Character accountType;
	
	@Column(name="LAST_ACTIVITY")
	@UpdateTimestamp
	private LocalDateTime lastActivityAt;
	
	@Column(name="ACC_STATUS")
	private Character accountStatus;
	
	@Column(name="CUSTOMER_NO")
	private Integer customerID;
	
	@Column(name="CCY_CODE")
	private String ccyCode;
	
	@Column(name="ACC_BALANCE")
	private BigDecimal balance;
	
	@Version
	@Column(name="VERSION")
	private Integer version;
	
}
