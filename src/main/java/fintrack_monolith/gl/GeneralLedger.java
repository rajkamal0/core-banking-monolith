package fintrack_monolith.gl;

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
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="GL_DETAILS")
public class GeneralLedger {
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator = "g_seq_generator", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "g_seq_generator", sequenceName = "SEQ_GENERALLEDGER", initialValue = 1, allocationSize = 1)
	private Integer id;
	
	@Column(name="GL_NUMBER", unique = true, nullable = false)
	private String glNum;
	
	@Column(name="GL_TYPE")
	private String glType;
	
	@Column(name="LAST_ACTIVITY")
	@UpdateTimestamp
	private LocalDateTime lastActivityAt;
	
	@Column(name="GL_STATUS")
	private Character glStatus;
	
	@Column(name="GL_BALANCE")
	private BigDecimal balance;
	
	@Column(name="CCY_CODE")
	private String ccyCode;
	
	@Version
	@Column(name="VERSION")
	private Integer version;
	
	public void assignGlNum(Integer glSeqValue) {
        this.id = glSeqValue;
        this.glNum = String.format("G%07d", glSeqValue);
    }

}
