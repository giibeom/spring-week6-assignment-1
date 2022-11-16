package com.codesoom.assignment.session;

import com.codesoom.assignment.common.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.codesoom.assignment.support.TokenFixture.ACCESS_TOKEN_1_VALID;
import static com.codesoom.assignment.support.UserFixture.USER_1;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
   세션 등록 -> POST /session
*/
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("SessionController 웹 테스트")
class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    /*
        로그인 API는
        - 유효한 회원 정보가 주어지면
            - 201 코드로 응답한다 (세션 토큰)
        - 유효하지 않는 회원 정보가 주어지면
            - 빈 값이 주어질 경우
                - 400 코드를 응답한다 (@Valid)
            - 찾을 수 없는 Email일 경우
                - 404 코드를 응답한다 (UserNotFoundException)
            - 비밀번호가 틀렸을 경우
                - 400 코드를 응답한다 (InvalidUserPasswordException)
     */
    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 로그인_API는 {
        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 유효한_회원_정보가_주어지면 {
            @BeforeEach
            void setUpGiven() {
                given(authenticationService.login(USER_1.로그인_요청_데이터_생성()))
                        .willReturn(ACCESS_TOKEN_1_VALID.토큰());
            }

            @Test
            @DisplayName("201 코드로 응답한다")
            void it_returns_session() throws Exception {
                mockMvc.perform(
                                post("/session")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(JsonUtil.writeValue(USER_1.로그인_요청_데이터_생성()))
                        )
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString(".")));

                verify(authenticationService).login(USER_1.로그인_요청_데이터_생성());
            }
        }
    }
}
