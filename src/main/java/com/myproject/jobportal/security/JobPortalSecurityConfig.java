package com.myproject.jobportal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
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
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) {

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
                .authorizeHttpRequests(requests -> requests.requestMatchers("/api/companies").permitAll() // Allows all users (authenticated or not) to access the /api/companies endpoint without a login. Or Publicly viewable, means this particular api end-point is non-secured and anyone can view it publicly without the client required to be authenticated.
                        // Remember when we only had .permitAll() without the .requestMatchers("") i.e. "http.authorizeHttpRequests((requests) -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl) requests.anyRequest()).permitAll())"  (look on the github with commit id: 07ce52e, learnt in previous lessons), it would make all the api-endpoints public to access without the client to be log in or authenticated. That's the difference between specifying the endpoints to permitAll() and simply putting permitAll().
                                                           .requestMatchers("/api/contacts").authenticated()//It does opposite to the above configuration, it Restricts access to the /api/contacts endpoint, allowing only logged-in users to view or interact with it. you can imagine like this protects the api-endpoints from unauthenticated access while above one says I will make all these api endpoints to be publicly available to be accessed without the authentication of the client
                )
                .build();

    }

    //

}

