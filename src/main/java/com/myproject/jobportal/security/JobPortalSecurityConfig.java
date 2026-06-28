package com.myproject.jobportal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class JobPortalSecurityConfig {

    /**
     * Configures the default {@link SecurityFilterChain} for the application.
     * <p>
     * This configuration applies a strict baseline security policy. Here is a deep dive into
     * what each configuration does, what it looks like, and modern industry standards:
     * </p>
     *
     * <h3>1. Authorization Policy (anyRequest().authenticated())</h3>
     * <ul>
     * <li><b>What it does:</b> It blocks all URLs and endpoints in the application by default.</li>
     * <li><b>When to use:</b> Use this when you want to secure the entire app (like an Admin Panel). In real production apps, some public endpoints (like /login or /register) are bypassed using {@code .permitAll()}.</li>
     * </ul>
     *
     * <h3>2. Form Login (formLogin)</h3>
     * <ul>
     * <li><b>What the user sees:</b> A default, Spring-generated HTML login page will appear in the browser with username and password fields.</li>
     * <li><b>When it is used:</b> It is still used today in traditional Monolith applications (Server-Side Rendering like Thymeleaf or JSP) where the Java backend directly serves the HTML to the browser.</li>
     * <li><b>Modern Alternative:</b> For modern Single Page Applications (React, Angular, Vue), this is usually <b>disabled</b> because the frontend framework handles the login UI.</li>
     * </ul>
     *
     * <h3>3. HTTP Basic (httpBasic)</h3>
     * <ul>
     * <li><b>What the user sees:</b> A small native browser pop-up box asking for credentials. For APIs (like Postman or cURL), it accepts an {@code Authorization: Basic [Base64-encoded-credentials]} header.</li>
     * <li><b>When it is used:</b> It was historically used for quick testing or internal microservice communication. <b>Do not use this in production</b> because it sends the raw password in every single request (Base64 encoding is not secure).</li>
     * <li><b>Modern Alternative:</b> For modern REST APIs, industry standards use <b>JWT (JSON Web Tokens)</b> or <b>OAuth2 / OIDC (like Okta, Keycloak, or Auth0)</b> instead.</li>
     * </ul>
     *
     * <p><b>Original Implementation Reference:</b></p>
     * <pre>{@code
     * SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
     *     http.authorizeHttpRequests((requests) -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)requests.anyRequest()).authenticated());
     *     http.formLogin(Customizer.withDefaults());
     *     http.httpBasic(Customizer.withDefaults());
     *     return (SecurityFilterChain)http.build();
     * }
     * }</pre>
     *
     * @param http the {@link HttpSecurity} object used to build the security configurations
     * @return the fully configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs during the security configuration build process
     */


//    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((requests) -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)requests.anyRequest()).authenticated());
//        http.formLogin(Customizer.withDefaults());
//        http.httpBasic(Customizer.withDefaults());
//        return (SecurityFilterChain)http.build();
//    }

    // Instead, we use the below type custom overriding of this method :

    @Bean
    SecurityFilterChain customSecurityFilterChain(HttpSecurity http) {

        /**Internal Mechanism: Configures the AuthorizationFilter to use an AuthorizationManager that checks if the current Authentication object is fully authenticated (i.e., it is not anonymous and not remember-me, depending on specific configuration, but generally means the user has logged in successfully).
           Filter Chain Behaviour: The request goes through the preceding filter chain (SecurityContext, CORS, etc.). When it reaches the authorization step, the filter inspects the security context to verify the user's identity. If no valid authentication exists, it blocks the request.
           In Simple Terms: The request is allowed only if the user is logged in. If they are anonymous (not logged in), authorization fails and they are either redirected to a login page or issued a 401 Unauthorized / 403 Forbidden error.
         */
//        return http.authorizeHttpRequests((requests) -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl) requests.anyRequest()).authenticated())

        /**
         * Internal Mechanism: Configures the AuthorizationFilter to use an AuthorizationManager that always returns a "deny/rejected" decision.  Filter Chain Behaviour: The request still goes through the preceding filter chain (SecurityContext, CORS, etc.), but it is blocked immediately at the authorization step.In Simple Terms: Regardless of whether the user is authenticated, anonymous, or an admin, authorization always fails (false) and throws an access-denied exception.  */
//        return http.authorizeHttpRequests((requests) -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl) requests.anyRequest()).denyAll())

        /**permitAll() in Spring Security configures the AuthorizationFilter to use an AuthorizationManager that always returns an “allow/granted” decision.
         The request still goes through the full filter chain (SecurityContext, CORS, etc.), but the authorization step does not block it.
         In simple terms: authentication may or may not exist, but authorization always passes (true).*/

//        return http.authorizeHttpRequests((requests) -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl) requests.anyRequest()).permitAll()) //PermitAll permits all requests by bypassing the authentication. Means the request is still checked by the security framework, but the framework chooses to always open the gate (true) rather than blocking it.
                return http.csrf(csrfConfig -> csrfConfig.disable())

//                .formLogin(Customizer.withDefaults())
//                .httpBasic(Customizer.withDefaults()) //You might think commenting out them will make the form and
//                input pop-up to disappear from the browser and spring security have configured to not read the
//                "Authorization header" from the request to authenticate and the default login form as well, will be
//                disappeared from the client or browser.No it will not work in that way, rather it will redirect to configure
//                default configuration. You can do that but not by commenting but by disabling them. See below how.

//                The correct way to configure it:
                .formLogin(form -> form.disable())
                .httpBasic(withDefaults())
                .authorizeHttpRequests(requests ->
                                // METHOD 1:
/*
                        requests.requestMatchers("/api/companies").permitAll() //Allows all users (authenticated or not) to access the /api/companies endpoint without a login. Or Publicly viewable, means this particular api end-point is non-secured and anyone can view it publicly without the client required to be authenticated.
                                // Remember when we only had .permitAll() without the .requestMatchers("") i.e. "http.authorizeHttpRequests((requests) -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl) requests.anyRequest()).permitAll())"  (look on the github with commit id: 07ce52e, learnt in previous lessons), it would make all the api-endpoints public to access without the client to be log in or authenticated. That's the difference between specifying the endpoints to permitAll() and simply putting permitAll().
                                .requestMatchers("/api/contacts").authenticated()//It does opposite to the above configuration, it Restricts access to the /api/contacts endpoint, allowing only logged-in users to view or interact with it. you can imagine like this protects the api-endpoints from unauthenticated access while above one says I will make all these api endpoints to be publicly available to be accessed without the authentication of the client
                                .anyRequest().denyAll()
*/

                                // METHOD 2: (Focus on RegesReqMatcher in this 2nd method)

                               /* requests.requestMatchers(RegexRequestMatcher.regexMatcher(".*public$")).permitAll()//Though we don't have that situation right now, but imagine a situation where you need to configure N no. of APIs to public, then you compulsorily have to write equal no. of .requestMatchers() as the line right above commented out. Right!. so let's leverage two things, the "uniformly ending path segment" and the RegexRequestMatcher to fast up the things.
                                        .requestMatchers( //making Swagger-related APIs public to access without authentication
                                                "/api/swagger-ui.html",
                                                "/swagger-ui/**",
                                                "/api/v3/api-docs/**",
                                                "/swagger-resources/**",
                                                "/swagger-ui.html",
                                                "/webjars/**"
                                        ).permitAll()
                                        .requestMatchers("/api/contacts").authenticated()
                                        .anyRequestDenyAll()
                                        */

                        // METHOD 3: Defining public/secured API endpoints in a separate File/Bean (PathsConfig.java)

                                // CRITICAL NOTE: Making a separate file with Spring @Beans to return Lists is a bad practice because it bloats the application container.
                                // BUT our active code below does not use beans! We are using simple static String[] arrays (PathsConfig.PUBLIC_PATHS).
                                // Using a static array from a separate class is completely clean, fast, and a production-ready way to manage Method 1.

                       requests.requestMatchers( PathsConfig.PUBLIC_PATHS).permitAll()
                               .requestMatchers(PathsConfig.SECURED_PATHS).authenticated()
                               .anyRequest().denyAll()

                )
                .build();

    }

    //Kindly read the below note for at least one time
    /**
     * METHOD 1: Centralized Request Matching (Enterprise Best Practice)
     * <p>
     * Defines perimeter security explicitly at the Web Layer using native Ant path matching.
     * Implements a "secure-by-default" mindset by maintaining a transparent map of the
     * application's attack surface in a single, auditable location.
     * </p>
     *
     * @see <a href="https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-requests.html">Spring Security Authorization</a>
     */

    /**
     * METHOD 2: Regular Expression Request Matching
     * <p>
     * ADVICE: Use only when regular-expression matching is genuinely required; prefer Ant-style
     * path patterns (/api/**) for most applications because they are simpler and more maintainable.
     * Inspects URIs using regular expressions to match visibility markers (e.g., '.*public$').
     * This tightly couples resource paths to security classifications, violating
     * RESTful resource-oriented design principles and risking brittle API routing.
     * </p>
     */

    /**
     * METHOD 3: Separated Static Path Arrays
     * <p>
     * Isolates URI string arrays into a dedicated utility class (PathsConfig.java).
     * This keeps the main filter configuration extremely clean, short, and readable
     * by eliminating repeated matching lines while avoiding Spring container bloat.
     * When combined with explicit matching, this provides a highly maintainable setup.
     * </p>
     */

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration corsConfiguration = new CorsConfiguration();
// setAllowedOrigins: Here, we only allow specific URLs that belong to our trusted UI.
// SECURITY NOTE: We should avoid using the wildcard '*' here because it means "any domain in the world."
// If '*' is used, hackers could easily send requests to your server from their fake websites (Security Risk).
// Without this configuration, the browser would block requests from http://localhost:5173, throwing a 'CORS Policy Error'.
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));

// setAllowedMethods: The server informs the browser about which HTTP actions (GET, POST, etc.) the client is permitted to perform.
// If this is not explicitly mentioned, the browser will only allow 'Safe' requests (like a simple GET) by default.
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));

// corsConfiguration.setAllowedMethods(Collections.singletonList("*")); // This allows all methods.
// This is a static method from the java.util.Collections class that creates an immutable List containing exactly ONE item.
// It is more memory-efficient than Arrays.asList("*") when you only need to store a single element.


// setAllowedHeaders: This defines which extra metadata (Headers) the client is allowed to send in the request.
// For example, 'Content-Type' (required for sending JSON data). If the client sends a header that is not listed here, the browser will directly reject the entire request.

// allowedHeaders() specifies which request headers are permitted during cross-origin requests.
// If the frontend sends headers not included in Access-Control-Allow-Headers, the browser blocks the actual request after the preflight validation fails.
        corsConfiguration.setAllowedHeaders(Arrays.asList("Content-Type"));

// setAllowCredentials: Set this to 'true' if the browser needs to send Cookies or Authorization headers.
// Note: If this is set to 'true', using the wildcard '*' in setAllowedOrigins is strictly prohibited (Security Rule).
        corsConfiguration.setAllowCredentials(true);

// setMaxAge: This tells the browser how long (in seconds) it should cache the CORS response (Pre-flight result).
// Example: 3600L (1 hour). This means the browser won't repeatedly send 'OPTIONS' (Pre-flight) requests for the next hour,
// which speeds up the application and reduces the load on the server.
        corsConfiguration.setMaxAge(36000L);

// 2. UrlBasedCorsConfigurationSource: This acts as a mapper that defines where these CORS rules should be applied.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

// registerCorsConfiguration: This specifies which server paths/endpoints will enforce the rules defined above.
// Example: "/**" means 'All endpoints'.
// If we had written "/api/public/**", the rules would apply only to public APIs, leaving admin APIs uncovered.
// If this line is omitted, the rules won't apply to any path, and the default browser blocking behavior will continue.
        source.registerCorsConfiguration("/**", corsConfiguration);

// 3. Final Step: The server packs all this configuration info into the 'Response Headers' (like Access-Control-Allow-Origin)
// and sends it back to the browser. The browser then inspects these headers to decide whether to expose the data to the UI or block it.
        return source;
    }
}

