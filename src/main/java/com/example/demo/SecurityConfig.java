@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private GoogleOAuth2UserService googleOAuth2UserService;

    @Autowired
    private UserDetailsService userDetailsService; // Implement your user details service

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/login", "/error", "/oauth2/").permitAll() // Allow login, error, and OAuth2 paths
                .antMatchers("/admin/").hasRole("ADMIN") // Require "ADMIN" role for admin paths
                .anyRequest().authenticated() // Require authentication for all other requests
            .and()
                .formLogin() // Enable form login if needed
                    .loginPage("/login")
                    .defaultSuccessUrl("/home")
                    .failureUrl("/login?error")
            .and()
                .oauth2Login() // Enable OAuth2 login
                    .userInfoEndpoint()
                        .userProvider(googleOAuth2UserService) // Google provider
            .and()
                .exceptionHandling() // Handle unauthorized access
                    .accessDeniedPage("/access-denied");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService) // Use your user details service
            .and()
                .oauth2Login(); // Enable OAuth2 login
    }

