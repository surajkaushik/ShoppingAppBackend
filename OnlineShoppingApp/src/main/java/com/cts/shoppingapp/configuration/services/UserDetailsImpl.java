package com.cts.shoppingapp.configuration.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cts.shoppingapp.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;
	@Id
	private String id;

	private String firstName;

	@NotBlank
	@Size(max = 120)
	private String lastName;

	private String loginId;

	private String emailId;

	@JsonIgnore
	private String password;

	@JsonIgnore
	private String confirmPassword;

	private String contactNumber;

	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(String id, String firstName, @NotBlank @Size(max = 120) String lastName, String loginId,
			String emailId, String password, String confirmPassword, String contactNumber,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.loginId = loginId;
		this.emailId = emailId;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.contactNumber = contactNumber;
		this.authorities = authorities;
	}

	public static UserDetailsImpl build(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());

		return new UserDetailsImpl(user.getId(), user.getFirstName(), user.getLastName(), user.getLoginId(),
				user.getEmailId(), user.getPassword(), user.getConfirmPassword(), user.getContactNumber(), authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getId() {
		return id;
	}

	public String getEmail() {
		return emailId;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return loginId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getLoginId() {
		return loginId;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}
}
