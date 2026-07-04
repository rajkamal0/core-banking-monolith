package fintrack_monolith.gl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

@Entity
@Data
@Table(name="GL_DETAILS")
public class GeneralLedger {
	
	@Id
	@Column(name="GL_NUMBER")
	private Integer glNum;
	
	@Column(name="GL_TYPE")
	private String glType;
	
	@Column(name="LAST_ACTIVITY")
	@UpdateTimestamp
	private LocalDateTime lastActivityAt;
	
	@Column(name="GL_STATUS")
	private Character glStatus;
	
	@Column(name="GL_BALANCE")
	private BigDecimal balance;
	
	@Version
	@Column(name="VERSION")
	private Integer version;

}
