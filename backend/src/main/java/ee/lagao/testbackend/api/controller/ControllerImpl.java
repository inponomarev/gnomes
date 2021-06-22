package ee.lagao.testbackend.api.controller;

import ee.lagao.testbackend.api.dto.LoginDTO;
import ee.lagao.testbackend.api.dto.LoginResultDTO;
import ee.lagao.testbackend.api.dto.SecretPlanDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ControllerImpl implements Controller {

    @Override
    public LoginResultDTO login(LoginDTO request, HttpServletResponse response) {
        LoginResultDTO result = new LoginResultDTO();
        if ("admin".equals(request.getLogin())
                && "admin".equals(request.getPassword())) {
            result.setSessionId("dead-beef");
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        System.out.printf("Login: %s, Password: %s, Status: %d, SessionId: %s%n",
                request.getLogin(),
                request.getPassword(),
                response.getStatus(),
                result.getSessionId());

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