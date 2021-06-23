package ee.lagao.testbackend.api.controller;

import ee.lagao.testbackend.api.dto.LoginDTO;
import ee.lagao.testbackend.api.dto.LoginResultDTO;
import ee.lagao.testbackend.api.dto.SecretPlanDTO;
import ee.lagao.testbackend.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class ControllerImpl implements Controller {

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final JwtUtils jwtUtils;

    @Override
    public LoginResultDTO login(LoginDTO request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(request.getLogin());

        var result = new LoginResultDTO();
        result.setToken(jwt);
        result.setType("Bearer");
        result.setUsername(request.getLogin());
        return result;
    }


    @Override
    public SecretPlanDTO secretplan(HttpServletResponse response) {
        SecretPlanDTO secretPlan = new SecretPlanDTO();
        secretPlan.setPoints(List.of(
                "Phase 1: collect underpants",
                "Phase 2: ?",
                "Phase 3: PROFIT"));
        return secretPlan;
    }
}