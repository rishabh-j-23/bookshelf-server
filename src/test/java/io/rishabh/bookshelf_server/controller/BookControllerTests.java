package io.rishabh.bookshelf_server.controller;

import io.rishabh.bookshelf_server.model.Book;
import io.rishabh.bookshelf_server.repository.BookRepository;
import io.rishabh.bookshelf_server.services.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@WebMvcTest(BookController.class)
@SpringJUnitConfig
@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@AutoConfigureRestDocs(outputDir = "./target/snippets")
public class BookControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private JwtService jwtService;

    @InjectMocks
    private BookController bookController;

    private Book sampleBook;

    @BeforeEach
    public void setUp() {
        sampleBook = new Book(
            UUID.randomUUID(),
            "Title",
            "Author",
            "Description",
            4.5,
            2022,
            "1234567890123",
            "Publisher",
            300,
            "English",
            "coverImageUrl",
            Collections.singletonList("Platform"),
            "Series",
            "Edition",
            "Volume",
            Collections.singletonList("Genre"),
            Collections.singletonList("Tag"),
            5,
            UUID.randomUUID()
        );

        // Set up mock user
        UserDetails userDetails = User.withUsername("user")
            .password("password")
            .build();
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();;
        securityContext.setAuthentication(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetBooks() throws Exception {
        Mockito.when(bookRepository.findAll()).thenReturn(Collections.singletonList(sampleBook));

        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(sampleBook.getTitle()));
    }

    @Test
    public void testNewBook() throws Exception {
       // Mock repository behavior
       Mockito.when(bookRepository.findByTitle(sampleBook.getTitle())).thenReturn(null);
       Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(sampleBook);
       Mockito.when(jwtService.extractUsername(Mockito.anyString())).thenReturn("user");

       // Perform the request and verify the response
       mockMvc.perform(MockMvcRequestBuilders.post("/books/add")
               .with(SecurityMockMvcRequestPostProcessors.csrf())  // Include CSRF token
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"title\":\"Title\",\"author\":\"Author\",\"pages\":300}"))
               .andExpect(MockMvcResultMatchers.status().isCreated())
               .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Title"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("Author"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.pages").value(300));
    }

    @Test
    public void testGetBookById() throws Exception {
        Mockito.when(bookRepository.findById(sampleBook.getId())).thenReturn(Optional.of(sampleBook));

        mockMvc.perform(MockMvcRequestBuilders.get("/books/" + sampleBook.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(sampleBook.getTitle()));
    }

    @Test
    public void testUpdateBook() throws Exception {
        Book updatedBook = new Book(
            sampleBook.getId(),
            "Updated Title",
            "Updated Author",
            "Updated Description",
            5.0,
            2023,
            "Updated ISBN",
            "Updated Publisher",
            350,
            "Updated Language",
            "Updated coverImageUrl",
            Collections.singletonList("Updated Platform"),
            "Updated Series",
            "Updated Edition",
            "Updated Volume",
            Collections.singletonList("Updated Genre"),
            Collections.singletonList("Updated Tag"),
            10,
            UUID.randomUUID()
        );

        Mockito.when(bookRepository.findById(sampleBook.getId())).thenReturn(Optional.of(sampleBook));
        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(updatedBook);

        mockMvc.perform(MockMvcRequestBuilders.put("/books/update/" + sampleBook.getId())
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Updated Title\",\"author\":\"Updated Author\",\"pages\":350}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Title"));
    }

    @Test
    public void testDeleteBook() throws Exception {
        Mockito.when(bookRepository.findById(sampleBook.getId())).thenReturn(Optional.of(sampleBook));

        // Simulate authenticated user with correct role/permissions
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/delete/" + sampleBook.getId())
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("addedbyUserId", UUID.randomUUID().toString()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());  // Adjust as needed
    }
}
