package com.codesoom.assignment.session;

import com.codesoom.assignment.session.dto.SessionRequestDto;
import com.codesoom.assignment.session.dto.SessionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {
    private final AuthenticationService authenticationService;

    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseDto login(@RequestBody SessionRequestDto sessionRequestDto) {
        return SessionResponseDto.builder()
                .accessToken(authenticationService.login(sessionRequestDto))
                .build();
    }
}
