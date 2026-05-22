package com.myproject.jobportal.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer.useMediaTypeParameter(MediaType.parseMediaType("application/vnd.myapp+json"), "v")
                .addSupportedVersions("1.0", "2.0", "3.0").setDefaultVersion("1.0"); //For this project we are using the mediaTypeVersioning.
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api", _ -> true); // so with this prefix path every api in my application is going to have this prefix path
//        Here another argument is passed as a predicate which decided whether to use the particular prefix path for that particular controller or not.
//        here it returns true means it would be true for all controllers to use this prefix path, hence it would set for each controller

//        One more thing here (_) in predicate is nothing but the 'unnamed-variable', released in java 22, it's for suppressing the warning the variable not used,
//        when the variable is not used inside the body in that condition we can put this unnamed variable(_) but that's the argument space is for passing the
//        'controllerType'


//      By the way here are some examples more of how configurePathMatch can be leveraged
        /**

         you can filter your controllers:
         1. By Class NameApply the prefix only if the controller's class name ends with a specific word (like ApiController).

         configurer.addPathPrefix("/api",
         c -> c.getSimpleName().endsWith("ApiController"));

         2. By a Custom AnnotationCreate your own annotation (e.g., @ApiPrefixRequired) and apply the prefix only to controllers marked with it.

         configurer.addPathPrefix("/api",
         c -> c.isAnnotationPresent(ApiPrefixRequired.class));

         3.  By Assignable Type (Interface or Base Class)Apply the prefix to any controller that implements a specific interface or extends a base API class.

         configurer.addPathPrefix("/api",
         c -> BaseApiController.class.isAssignableFrom(c));

         4. Negative Matching (Exclusion)Apply the prefix to everything except a specific package or class (e.g., keeping your web view controllers clean).

         configurer.addPathPrefix("/api",
         c -> !c.getPackageName().startsWith("com.example.web"));

         */


    }

    /**
     * Yeh method CORS (Cross-Origin Resource Sharing) ko configure karne ke liye ek Bean banata hai.
     *
     * <p><strong>CORS kya hai?</strong></p>
     * Jab aapka Frontend (jaise React/localhost:5173) aur Backend (localhost:8080) alag-alag ports par hote hain,
     * toh browser security ki wajah se request block kar deta hai. Usse allow karne ke liye yeh filter chahiye hota hai.
     *
     * @return CorsFilter jo browser ko batayega ki kaunse Origins aur Methods allowed hain.
     */
    @Bean
    public CorsFilter corsFilter() {

        // 1. CorsConfiguration: Isme hum saare rules likhte hain.
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // setAllowedOrigins: Yahan sirf un specific URLs ko allow karte hain jo humari trusted UI hain.
        // SECURITY NOTE: Yahan '*' (wildcard) use nahi karna chahiye, kyunki iska matlab hai "Duniya ka koi bhi domain".
        // Agar '*' use kiya toh hackers bhi apni fake website se aapke server ko request bhej payenge (Security Risk).
        // Agar yeh nahi hota, toh browser localhost:5173 ki request ko 'CORS Policy Error' bol kar block kar deta.
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));

        // setAllowedMethods: Server browser ko batata hai ki client kaunse actions (GET, POST etc.) kar sakta hai.
        // Agar yeh mention nahi karenge, toh default mein browser sirf 'Safe' requests (jaise simple GET) allow karega.
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        //    corsConfiguration.setAllowedMethods(Collections.singletonList("*"));// it says all methods are allowed. Yeh java.util.Collections class ka ek static method hai jo ek Aisi List banata hai jisme sirf EK hi item hota hai.
        //Arrays.asList("*") ke muqable yeh zyada memory-efficient hai agar aapko sirf ek hi element rakhna ho.


        // setAllowedHeaders: Batate hain ki request mein kaunsi extra info (Headers) client bhej sakta hai.
        // Jaise 'Content-Type' (JSON data ke liye). Agar client koi aisa header bheje jo yahan list nahi hai toh browser will directly reject that wholesome request.

        //allowedHeaders() specifies which request headers are permitted during cross-origin requests. If the frontend sends headers not included in Access-Control-Allow-Headers, the browser blocks the actual request after preflight validation fails.

        corsConfiguration.setAllowedHeaders(Arrays.asList("Content-Type"));

        // setAllowCredentials: Agar browser ko Cookies ya Authorization headers send karne hain, toh isse 'true' rakhein.
        // Note: Agar yeh 'true' hai, toh setAllowedOrigins mein '*' use karna allowed nahi hota (Security Rule).
        corsConfiguration.setAllowCredentials(true);

        // setMaxAge: Yeh bataata hai ki browser CORS response (Pre-flight result) ko kitni der tak cache karega.
        // Example: 3600L (1 hour). Iska matlab hai ki 1 ghante tak browser baar-baar 'OPTIONS' (Pre-flight) request nahi bhejega,
        // jisse application fast chalegi aur server par load kam hoga.
        corsConfiguration.setMaxAge(36000L);

        // 2. UrlBasedCorsConfigurationSource: Yeh ek mapper hai jo batata hai rules kahan-kahan lagenge.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // registerCorsConfiguration: Yeh batata hai ki upar waale rules server ke kis-kis path par apply honge.
        // Example: "/**" ka matlab hai 'All endpoints'.
        // Agar "/api/public/**" likhte, toh rules sirf public APIs par lagte, admin APIs par nahi.
        // Agar yeh line nahi hoti, toh rules kisi bhi path par apply nahi hote aur default blocking chalti rehti.
        source.registerCorsConfiguration("/**", corsConfiguration);

        // 3. Final Step: Server yeh sab info 'Response Header' (Access-Control-Allow-Origin) mein pack karke
        // browser ko bhejta hai. Browser us header ko check karke decide karta hai ki UI ko data dikhana hai ya nahi.
        return new CorsFilter(source);

    }




//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000")
//                .allowedMethods("GET", "POST", "PUT", "DELETE")
//                .allowedHeaders("*")
//                .allowCredentials(true).maxAge(3600);
//    }

    //Does the same thing as CorsFilter but addCorsMappings() configures CORS at Spring MVC level, while CorsFilter handles CORS at servlet filter level before requests reach Spring Security or controllers, making it more suitable for complex secured applications. So basically, CorsFilter is the recommended way to apply CORS policy.

    // Difference between addCorsMappings vs CorsFilter (Hinglish):
    // 1. addCorsMappings: Yeh Spring MVC ka part hai. Request jab 'DispatcherServlet' tak pahunch jati hai, tab yeh kaam karta hai.
    // 2. CorsFilter: Yeh Servlet Filter hai. Request server mein enter hote hi sabse pehle isi se takraati hai (Before DispatcherServlet & Spring Security).
    // 3. Recommendation: Agar aap Spring Security use kar rahe hain, toh CorsFilter zyada reliable hai kyunki yeh security check se pehle hi CORS resolve kar deta hai.
}
