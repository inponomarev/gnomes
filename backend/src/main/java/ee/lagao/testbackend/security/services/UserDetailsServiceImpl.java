package ee.lagao.testbackend.security.services;

import ee.lagao.testbackend.api.dto.LoginDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private final PasswordEncoder passwordEncoder;
    private final Map<String, LoginDTO> users = new ConcurrentHashMap<>();

    @PostConstruct
    void init() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLogin("admin");
        loginDTO.setPassword("admin");
        users.put(loginDTO.getLogin(), loginDTO);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginDTO loginDTO = users.get(username);
        if (loginDTO == null) {
            throw new UsernameNotFoundException("User Not Found with username: " + username);
        }
        return User.builder().passwordEncoder(passwordEncoder::encode)
                .username(loginDTO.getLogin())
                .password(loginDTO.getPassword())
                .authorities(emptyList())
                .build();
    }

}
