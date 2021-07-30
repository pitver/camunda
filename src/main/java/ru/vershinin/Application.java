package ru.vershinin;

import org.springframework.beans.factory.annotation.Value;
import org.camunda.bpm.engine.rest.security.auth.ProcessEngineAuthenticationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

  public static void main(String... args) {
    SpringApplication.run(Application.class, args);
  }

  @Value("${key.regEnabled}")
  private boolean regEnabled;

  @Bean
  public FilterRegistrationBean myFilterRegistration() {

    FilterRegistrationBean registration = new FilterRegistrationBean();
    registration.setFilter(new ProcessEngineAuthenticationFilter());
    registration.addUrlPatterns("/rest/*","/engine-rest/*","/*");
    registration.addInitParameter("authentication-provider", "org.camunda.bpm.engine.rest.security.auth.impl.HttpBasicAuthenticationProvider");
    registration.setName("camunda-auth");
    registration.setOrder(1);
    registration.setEnabled(regEnabled);
    return registration;

  }

}