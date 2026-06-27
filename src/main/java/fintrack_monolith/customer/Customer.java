package fintrack_monolith.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
	@Column(name="CUSTOMER_ID")
	private Integer custID;
	
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
	
}
