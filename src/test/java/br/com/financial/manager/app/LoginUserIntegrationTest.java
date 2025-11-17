package br.com.financial.manager.app;

import br.com.financial.manager.app.domain.entity.Users;
import br.com.financial.manager.app.infrastructure.repository.postgres.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class LoginUserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UsersRepository usersRepository;

    @BeforeEach
    void setup(){
        usersRepository.deleteAll();

        Users user = Users.builder()
                .username("Ronald")
                .email("or040918@gmail.com")
                .password(encoder.encode("123456"))
                .accounts(new ArrayList<>())
                .roles(new HashSet<>())
                .build();

        usersRepository.save(user);
    }

    @Test
    void shouldDoUsersLoginAndReturnJwt() throws Exception {
        String json = """
                {
                    "email":"or040918@gmail.com",
                    "password":"123456"
                }
                """;

        mockMvc.perform(
                post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void shouldThrowBadCredentialException() throws Exception {
        String json = """
                {
                    "email":"or040918@gmail.com",
                    "password":"123"
                }
                """;

        mockMvc.perform(
                        post("/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Login error"))
                .andExpect(jsonPath("$.message").value("Email or Password invalid"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
