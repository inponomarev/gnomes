package ee.lagao.testbackend.api.controller;

import ee.lagao.testbackend.api.dto.LoginDTO;
import ee.lagao.testbackend.api.dto.SecretPlanDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ControllerImpl implements ee.lagao.testbackend.api.controller.Controller {

    @Override
    public void login(LoginDTO request, HttpServletResponse response) {
        if ("admin".equals(request.getLogin())
                && "admin".equals(request.getPassword())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        System.out.printf("Login: %s, Password: %s, Status: %d%n",
                request.getLogin(),
                request.getPassword(),
                response.getStatus());
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