package com.cts.shoppingapp.model;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
public class UserInfoResponse {
	@Getter
	@Setter
	private String firstName;
	@Getter
	@Setter
	private String lastName;
	@Getter
	@Setter
	private String loginId;
	@Getter
	@Setter
	private String contactNo;
	@Getter
	private List<String> role;
	
	public UserInfoResponse(String firstName, String lastName, String loginId, String contactNo, List<String> role) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.loginId = loginId;
		this.contactNo = contactNo;
		this.role = role;
	}

	

}
