package io.rishabh.bookshelf_server.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.rishabh.bookshelf_server.model.User;
import io.rishabh.bookshelf_server.repository.UserRepository;
import io.rishabh.bookshelf_server.security.jwt.filters.JwtFilter;
import io.rishabh.bookshelf_server.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @MockBean
    private JwtFilter jwtFilter;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        User user1 = new User("user1", "User One", "user1@example.com", "password");
        User user2 = new User("user2", "User Two", "user2@example.com", "password");
        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        mockMvc.perform(get("/users")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }

    @Test
    public void testAddUserSuccess() throws Exception {
        User user = new User("user1", "User One", "user1@example.com", "password");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        when(userService.register(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/user/register")
        .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
    }

    @Test
    public void testAddUserFailureIncompleteData() throws Exception {
        User user = new User(null, null, null, null);

        mockMvc.perform(post("/user/register")
        .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("incomplete data"));
    }

    @Test
    public void testAddUserAlreadyExists() throws Exception {
        User existingUser = new User("user1", "User One", "user1@example.com", "password");

        when(userRepository.findByUsername(existingUser.getUsername())).thenReturn(existingUser);

        mockMvc.perform(post("/user/register")
        .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(existingUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
    }

    @Test
    public void testLoginUser() throws Exception {
        User user = new User("user1", "User One", "user1@example.com", "password");

        when(userService.verifyUser(any(User.class))).thenReturn("success");

        mockMvc.perform(post("/user/login")
        .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }
}

