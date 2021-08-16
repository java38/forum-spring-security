package telran.java38.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import telran.java38.user.dao.AccountRepository;
import telran.java38.user.model.UserProfile;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	AccountRepository accountRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserProfile userProfile = accountRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));
		String[] roles = userProfile.getRoles()
				.stream()
				.map(r -> "ROLE_" + r.toUpperCase())
				.toArray(String[]::new);
		return new User(username, userProfile.getPassword(), 
				AuthorityUtils.createAuthorityList(roles));
	}

}
