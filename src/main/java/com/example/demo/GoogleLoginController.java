@Controller
public class GoogleLoginController {

    @Autowired
    private GoogleOAuth2UserService googleOAuth2UserService;

    @Autowired
    private UserService userService; // Assuming a UserService for user management

    @GetMapping("/login")
    public String login() {
        return "redirect:/oauth2/authorization/google";
    }

    @GetMapping("/oauth2/callback/google")
    public Mono<String> googleCallback(AuthenticationPrincipal principal) {
        GoogleUser user = GoogleUser.class.cast(principal);
        String email = user.getEmail();

        // Create or authenticate user, store details (if using database)
        User dbUser = userService.createUser(user);

        // Redirect to authorized home page
        return Mono.just("redirect:/home");
    }

   
}
