package ru.gromov.resvote.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gromov.resvote.model.User;
import ru.gromov.resvote.repository.UserRepository;

/*
 *   Created by Gromov Vitaly, 2019   e-mail: mr.gromov.vitaly@gmail.com
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	@Autowired
	private final UserRepository userRepository;

	@Cacheable("user")
	@Transactional(readOnly = true)
	public User getUserByEmail(String email) {
		log.info("load user from repo");
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(
						String.format("User with name %s, not found!", email)
				));
	}
}
