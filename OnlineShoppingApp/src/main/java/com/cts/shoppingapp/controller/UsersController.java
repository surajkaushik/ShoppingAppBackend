package com.cts.shoppingapp.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cts.shoppingapp.configuration.jwt.JwtUtils;
import com.cts.shoppingapp.configuration.services.UserDetailsImpl;
import com.cts.shoppingapp.model.ERole;
import com.cts.shoppingapp.model.LoginRequest;
import com.cts.shoppingapp.model.MessageResponse;
import com.cts.shoppingapp.model.ResetRequest;
import com.cts.shoppingapp.model.Role;
import com.cts.shoppingapp.model.SignupRequest;
import com.cts.shoppingapp.model.User;
import com.cts.shoppingapp.model.UserInfoResponse;
import com.cts.shoppingapp.repository.RoleRepository;
import com.cts.shoppingapp.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1.0/shopping")
public class UsersController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
				.body(new UserInfoResponse(userDetails.getFirstName(), userDetails.getLastName(),
						userDetails.getLoginId(), userDetails.getContactNumber(), roles));
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByLoginId(signUpRequest.getLoginId())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Login Id is already taken!"));
		} else if (!signUpRequest.getLoginId().equals(signUpRequest.getEmailId())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email Id and Login Id must be same !"));
		} else if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Passwords does not match !"));
		} else if ((signUpRequest.getFirstName().isEmpty() || signUpRequest.getEmailId().isEmpty()
				|| signUpRequest.getLoginId().isEmpty() || signUpRequest.getPassword().isEmpty()
				|| signUpRequest.getContactNumber().isEmpty())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error:All Fields are mandatory !"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getLoginId(),
				signUpRequest.getEmailId(), encoder.encode(signUpRequest.getPassword()),
				encoder.encode(signUpRequest.getConfirmPassword()), signUpRequest.getContactNumber());

		Set<String> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PutMapping("/forgot")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<?> passwordReset(@Valid @RequestBody ResetRequest resetRequest) {

		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<User> user = userRepository.findByLoginId(loginId);
		User u = user.get();
		u.setPassword(encoder.encode(resetRequest.getPassword()));
		u.setConfirmPassword(encoder.encode(resetRequest.getPassword()));
		userRepository.save(u);
		return ResponseEntity.ok(new MessageResponse("Password updated successfully!"));
	}

	@GetMapping("/findAllUsers")
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> getAllUsers() {
		List<User> usersList = userRepository.findAll();
		return usersList;
	}

	@PostMapping("/logout")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<?> logoutUser() {
		ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
				.body(new MessageResponse("You've been signed out!"));
	}
}
