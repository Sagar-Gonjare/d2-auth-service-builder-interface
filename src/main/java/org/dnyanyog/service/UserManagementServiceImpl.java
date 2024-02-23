package org.dnyanyog.service;

import static java.util.Objects.nonNull;

import java.util.List;
import java.util.Optional;

import org.dnyanyog.dto.AddUserRequest;
import org.dnyanyog.dto.AddUserResponse;
import org.dnyanyog.encryption.EncryptionService;
import org.dnyanyog.entity.Users;
import org.dnyanyog.repo.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserManagementServiceImpl implements UserManagementService {

	Logger logger = LoggerFactory.getLogger(UserManagementService.class);

	@Autowired
	UsersRepository userRepo;

	@Autowired
	AddUserResponse userResponse;

	@Autowired
	private List<Long> userIds;
	@Autowired
	EncryptionService encryptionService;

	public Optional<AddUserResponse> addUpdateUser(AddUserRequest request) throws Exception {

		Users usersTable = Users.getInstance().setUsername(request.getUsername()).setAge(request.getAge())
				.setEmail(request.getEmail()).setPassword(encryptionService.encrypt(request.getPassword()))
				.setUserId(generateRandomUserId());

		usersTable = userRepo.save(usersTable);

		userResponse.setMessage("User added successfully");
		userResponse.setStatus("Success");
		userResponse.setUserId(usersTable.getUserId());
		userResponse.getUserData().setEmail(usersTable.getEmail());
		userResponse.getUserData().setUsername(usersTable.getUsername());
		userResponse.getUserData().setPassword(usersTable.getPassword());
		userResponse.getUserData().setAge(usersTable.getAge());

		return Optional.of(userResponse);
	}

	public AddUserResponse getSingleUser(Long userId) throws Exception {
		Optional<Users> receivedData = userRepo.findByUserId(userId);

		if (receivedData.isPresent()) {
			Users user = receivedData.get();

			String encyptedPassword = user.getPassword();

			userResponse.setStatus("Success");
			userResponse.setMessage("User found");
			userResponse.setUserId(user.getUserId());
			userResponse.getUserData().setEmail(user.getEmail());
			userResponse.getUserData().setUsername(user.getUsername());
			userResponse.getUserData().setPassword(encryptionService.decrypt(encyptedPassword));
			userResponse.getUserData().setAge(user.getAge());

		} else {
			userResponse.setStatus("Fail");
			userResponse.setMessage("User not found");
		}
		return userResponse;
	}

	public List<Users> getAllUser() {
		return userRepo.findAll();
	}

	public List<Long> getAllUserIds() {
		List<Users> users = userRepo.findAll();

		for (Users user : users) {
			if (nonNull(user)) {
				userIds.add(user.getUserId());
			}
		}
		return userIds;
	}

	private long generateRandomUserId() {

		int randomId = (int) (Math.random() * 900) + 100;
		return randomId;
	}

	public AddUserResponse updateUser(Long userID, Users request) throws Exception {

		AddUserResponse userResponse = new AddUserResponse();
		Optional<Users> receivedData = userRepo.findByUserId(userID);
		if (receivedData.isPresent()) {

			Users user = receivedData.get();

			user.setUsername(request.getUsername());
			user.setPassword(encryptionService.encrypt(request.getPassword()));
			user.setAge(request.getAge());
			user.setEmail(request.getEmail());

			user = userRepo.save(user);

			userResponse.setStatus("Success");
			userResponse.setMessage("User Updated");
		} else {
			userResponse.setStatus("Fail");
			userResponse.setMessage("User Not Found");

		}
		return userResponse;
	}

}
