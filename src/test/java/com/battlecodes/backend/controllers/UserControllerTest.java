package com.battlecodes.backend.controllers;

import com.battlecodes.backend.models.ERole;
import com.battlecodes.backend.models.UserModel;
import com.battlecodes.backend.models.requests.LoginRequest;
import com.battlecodes.backend.models.requests.RegisterUserRequest;
import com.battlecodes.backend.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Test
    public void testRegisterUser() throws Exception {
        UserModel existingUser = userService.getUserByEmail("test@example.com");
        if (existingUser != null) {
            userService.deleteUserById(existingUser.getId());
        }

        RegisterUserRequest request = new RegisterUserRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setRoles(Set.of(ERole.ROLE_STUDENT));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void testAuthenticateUser() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"STUDENT"})
    public void testGetMyUser() throws Exception {
        UserModel user = new UserModel();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRoles(Set.of(ERole.ROLE_STUDENT));
        userService.createUser(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"STUDENT"})
    public void testGetMyUserWhenUserExists() throws Exception {
        UserModel user = new UserModel();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRoles(Set.of(ERole.ROLE_STUDENT));
        userService.createUser(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    public void testDeleteUserById() throws Exception {
        UserModel user = userService.getUserByEmail("test@example.com");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/" + user.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    public void testEditUser() throws Exception {
        UserModel user = userService.getUserByEmail("test@example.com");
        if (user == null) {
            user = new UserModel();
            user.setName("Test User");
            user.setEmail("test@example.com");
            user.setPassword("password");
            user.setRoles(Set.of(ERole.ROLE_STUDENT));
            userService.createUser(user);
        }

        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/" + user.getId())
                        .param("name", "Updated Name"))
                .andExpect(status().isOk());
    }
}