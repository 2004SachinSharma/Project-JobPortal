package com.myproject.jobportal.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
         configurer.useMediaTypeParameter(MediaType.parseMediaType("application/vnd.myapp+json"),"v")
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


}
