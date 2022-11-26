package edu.shmonin.bookshelftest.service.impl;

import edu.shmonin.bookshelftest.exception.EntityNotFoundException;
import edu.shmonin.bookshelftest.model.User;
import edu.shmonin.bookshelftest.repository.UserRepository;
import edu.shmonin.bookshelftest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.Locale.ENGLISH;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    @Override
    public User saveUser(User user) {
        log.info(String.format("Saving user %s into DB", user.getUsername()));
        var baseUser = userRepository.findUserByUsername(user.getUsername());
        if (baseUser.isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        return baseUser.get();
    }

    @Override
    public User getUser(String username) {
        log.info("Looking user {} in DB", username);
        return userRepository.findUserByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(messageSource.getMessage("exception.userNotFound", new Object[]{username}, ENGLISH))
        );
    }

    @Override
    public List<User> getUsers() {
        log.info("Getting all users from DB");
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = getUser(username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}