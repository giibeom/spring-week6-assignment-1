package com.codesoom.assignment.domain.session.controller;

import com.codesoom.assignment.common.util.JsonUtil;
import com.codesoom.assignment.session.application.AuthenticationService;
import com.codesoom.assignment.user.exception.UserInvalidPasswordException;
import com.codesoom.assignment.user.exception.UserNotFoundException;
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
import static com.codesoom.assignment.support.UserFixture.USER_2;
import static com.codesoom.assignment.support.UserFixture.USER_INVALID_BLANK_EMAIL;
import static com.codesoom.assignment.support.UserFixture.USER_INVALID_BLANK_PASSWORD;
import static com.codesoom.assignment.support.UserFixture.USER_NOT_REGISTER;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class Session_Controller_웹_테스트 {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 로그인_API는 {
        @Nested
        @DisplayName("유효한 회원 정보가 주어지면")
        class Context_with_valid_user {
            @BeforeEach
            void setUpGiven() {
                given(authenticationService.login(USER_1.로그인_요청_데이터_생성()))
                        .willReturn(ACCESS_TOKEN_1_VALID.토큰());
            }

            @Test
            @DisplayName("201 코드로 응답한다")
            void it_responses_201() throws Exception {
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

        @Nested
        @DisplayName("유효하지 않는 회원 정보가 주어지면")
        class Context_with_invalid_user {
            @Nested
            @DisplayName("이메일이 공백으로 주어질 경우")
            class Context_with_blank_email {
                @Test
                @DisplayName("400 코드로 응답한다")
                void it_responses_400() throws Exception {
                    mockMvc.perform(
                                    post("/session")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(JsonUtil.writeValue(USER_INVALID_BLANK_EMAIL.로그인_요청_데이터_생성()))
                            )
                            .andExpect(status().isBadRequest());
                }
            }

            @Nested
            @DisplayName("비밀번호가 공백으로 주어질 경우")
            class Context_with_blank_password {
                @Test
                @DisplayName("400 코드로 응답한다")
                void it_responses_400() throws Exception {
                    mockMvc.perform(
                                    post("/session")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(JsonUtil.writeValue(USER_INVALID_BLANK_PASSWORD.로그인_요청_데이터_생성()))
                            )
                            .andExpect(status().isBadRequest());
                }
            }

            @Nested
            @DisplayName("찾을 수 없는 계정일 경우")
            class Context_with_not_found_user {
                @BeforeEach
                void setUpGiven() {
                    given(authenticationService.login(USER_NOT_REGISTER.로그인_요청_데이터_생성()))
                            .willThrow(new UserNotFoundException(USER_NOT_REGISTER.EMAIL()));
                }

                @Test
                @DisplayName("404 코드로 응답한다")
                void it_responses_404() throws Exception {
                    mockMvc.perform(
                                    post("/session")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(JsonUtil.writeValue(USER_NOT_REGISTER.로그인_요청_데이터_생성()))
                            )
                            .andExpect(status().isNotFound());

                    verify(authenticationService).login(USER_NOT_REGISTER.로그인_요청_데이터_생성());
                }
            }

            @Nested
            @DisplayName("비밀번호가 틀렸을 경우")
            class Context_with_wrong_password {
                @BeforeEach
                void setUpGiven() {
                    given(authenticationService.login(USER_2.로그인_요청_데이터_생성()))
                            .willThrow(new UserInvalidPasswordException(USER_2.EMAIL()));
                }

                @Test
                @DisplayName("403 코드로 응답한다")
                void it_responses_403() throws Exception {
                    mockMvc.perform(
                                    post("/session")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(JsonUtil.writeValue(USER_2.로그인_요청_데이터_생성()))
                            )
                            .andExpect(status().isForbidden());

                    verify(authenticationService).login(USER_2.로그인_요청_데이터_생성());
                }
            }
        }
    }
}
