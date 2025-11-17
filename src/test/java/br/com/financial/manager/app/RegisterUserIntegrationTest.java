package br.com.financial.manager.app;

import br.com.financial.manager.app.domain.entity.Role;
import br.com.financial.manager.app.domain.entity.Users;
import br.com.financial.manager.app.infrastructure.repository.postgres.RoleRepository;
import br.com.financial.manager.app.infrastructure.repository.postgres.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class RegisterUserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setup(){
        Role role = new Role("USER", new HashSet<>());
        roleRepository.save(role);
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        String json = """
                {
                    "username": "Ronald",
                    "email": "or040918@gmail.com",
                    "password": "123"
                }
                """;

        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isCreated());

        Users user = usersRepository.findByEmail("or040918@gmail.com").orElseThrow();

        assertEquals("Ronald", user.getUsername());
        assertEquals("or040918@gmail.com", user.getEmail());

        assertTrue(user.getRoles().stream()
                .anyMatch(r -> r.getName().equals("USER")));
    }
}
