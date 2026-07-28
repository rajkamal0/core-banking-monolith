package fintrack_monolith.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="CUSTOMER_DETAILS")
public class Customer {
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator = "c_seq_generator", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "c_seq_generator", sequenceName = "SEQ_CUSTOMER", initialValue = 1, allocationSize = 1)
	private Integer id;
	
	@Column(name="CUSTOMER_ID", unique = true, nullable = false)
	private String custId;
	
	@Column(name="FIRST_NAME")
	private String firstname;
	
	@Column(name="LAST_NAME")
	private String lastname;

	@Column(name="MOBILE")
	private Integer mobileNum;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="ADDRLINE1")
	private String addressLine1;
	
	@Column(name="COUNTRY")
	private String country;
	
	@Column(name="PINCODE")
	private Integer pincode;
	
	@Column(name="KYC_STATUS")
	private Boolean kycStatus;
	
	// for generating customer id
	@PrePersist
	public void assignCustId() {
//        this.id = custSeqValue;
		if(this.id != null) {
			this.custId = String.format("C%07d", this.id);
		}
        
    }
	
}
