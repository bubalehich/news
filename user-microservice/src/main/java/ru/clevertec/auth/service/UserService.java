package ru.clevertec.auth.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.clevertec.auth.entity.User;
import ru.clevertec.auth.repository.UserRepository;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    private final RoleService roleService;

    private final PasswordEncoder bCryptPasswordEncoder;


    public User register(String email, String username, String password, String roleName) {
        if (isExistsByEmail(email)) {
            throw new RuntimeException("User with email already exists.");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("User with username already exists.");
        }

        var role = roleService.getByName(roleName);
        var encodedPassword = bCryptPasswordEncoder.encode(password);

        var user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRegisterDate(Instant.now());
        user.setUsername(username);
        user.setRoles(List.of(role));

        return userRepository.save(user);
    }

    private boolean isExistsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(()
                -> new RuntimeException("Invalid username"));
    }

    public User authenticate(String email, String password) {
        return null;
    }
}
