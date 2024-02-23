import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GoogleLoginControllerTest {

    @Mock
    private GoogleOAuth2UserService googleOAuth2UserService;

    @Mock
    private UserService userService;

    @InjectMocks
    private GoogleLoginController googleLoginController;

    @Test
    @WithMockUser
    public void testLogin() {
        WebTestClient.bindToController(googleLoginController).build()
                .get()
                .uri("/login")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/oauth2/authorization/google");
    }

    @Test
    public void testGoogleCallback() {
        // Mock principal
        OAuth2User principal = mock(OAuth2User.class);
        when(principal.getEmail()).thenReturn("test@example.com");

        // Mock user creation
        User mockedUser = new User();
        when(userService.createUser(any())).thenReturn(mockedUser);

        // Call the controller method
        Mono<String> result = googleLoginController.googleCallback(principal);

        // Verify redirects
        WebTestClient.bindToController(googleLoginController).build()
                .get()
                .uri("/home")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/home");

        // Verify userService.createUser is called with correct argument
        verify(userService, times(1)).createUser(any());

        // Assert result
        result.block(); // Block to get the result
    }
}
