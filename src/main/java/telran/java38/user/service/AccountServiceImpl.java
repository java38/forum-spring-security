package telran.java38.user.service;

import java.time.LocalDate;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import telran.java38.user.dao.AccountRepository;
import telran.java38.user.dto.UserProfileDto;
import telran.java38.user.dto.UserRegDto;
import telran.java38.user.dto.UserUpdateDto;
import telran.java38.user.dto.exceptions.UserConflictException;
import telran.java38.user.dto.exceptions.UserNotFoundException;
import telran.java38.user.model.UserProfile;

@Service
public class AccountServiceImpl implements AccountService {

	AccountRepository accountRepository;
	ModelMapper modelMapper;
	PasswordEncoder passwordEncoder;
	
	@Value("${exp.value}")
	long expPeriod;

	@Autowired
	public AccountServiceImpl(AccountRepository accountRepository, ModelMapper modelMapper, 
			PasswordEncoder passwordEncoder) {
		this.accountRepository = accountRepository;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserProfileDto addUser(UserRegDto userRegDto) {
		if (accountRepository.existsById(userRegDto.getLogin())) {
			throw new UserConflictException(userRegDto.getLogin());
		}
		String hashPassword = passwordEncoder.encode(userRegDto.getPassword());
		UserProfile userProfile = new UserProfile(userRegDto.getLogin(),
				hashPassword, userRegDto.getFirstName(),
				userRegDto.getLastName(), LocalDate.now().plusDays(expPeriod));
		accountRepository.save(userProfile);
		return modelMapper.map(userProfile, UserProfileDto.class);
	}

	@Override
	public UserProfileDto findUserById(String login) {
		UserProfile userProfile = accountRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		return modelMapper.map(userProfile, UserProfileDto.class);
	}

	@Override
	public UserProfileDto updateUser(String login, UserUpdateDto userUpdateDto) {
		UserProfile userProfile =accountRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		if (userUpdateDto.getFirstName() != null) {
			userProfile.setFirstName(userUpdateDto.getFirstName());
		}
		if (userUpdateDto.getLastName() != null) {
			userProfile.setLastName(userUpdateDto.getLastName());
		}
		accountRepository.save(userProfile);
		return modelMapper.map(userProfile, UserProfileDto.class);
	}

	@Override
	public UserProfileDto removeUser(String login) {
		UserProfile userProfile = accountRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		accountRepository.delete(userProfile);
		return modelMapper.map(userProfile, UserProfileDto.class);
	}

	@Override
	public void changePassword(String login, String password) {
		UserProfile userProfile = accountRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		if(password != null) {
			String hashPassword = passwordEncoder.encode(password);
			userProfile.setPassword(hashPassword);
			userProfile.setExpDate(LocalDate.now().plusDays(expPeriod));
			accountRepository.save(userProfile);
		}

	}

	@Override
	public Set<String> updateRolesList(String login, String role, boolean isSet) {
		UserProfile userProfile = accountRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		if (isSet) {
			userProfile.addRole(role);
		}else {
			userProfile.removeRole(role);
		}
		accountRepository.save(userProfile);
		return userProfile.getRoles();
	}

}
