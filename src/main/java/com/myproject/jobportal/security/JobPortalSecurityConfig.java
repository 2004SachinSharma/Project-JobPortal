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

        return http.authorizeHttpRequests((requests) -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl) requests.anyRequest()).authenticated())
//                .formLogin(Customizer.withDefaults())
//                .httpBasic(Customizer.withDefaults()) //You might think commenting out them will make the form and
//                input pop-up to disappear from the browser and spring security have configured to not read the
//                "Authorization header" from the request to authenticate and the default login form as well, will be
//                disappeared from the client or browser.No it will not work in that way, rather it will redirect to configure
//                default configuration. You can do that but not by commenting but by disabling them. See below how.

//                The correct way to configure it:
                .formLogin(form -> form.disable())
                .httpBasic(withDefaults())
                .build();

    }

    //

}

