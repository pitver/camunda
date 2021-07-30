package ru.vershinin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.vershinin.model.ProcessInstanceInfo;

@Configuration
public class ConfigBean {

   @Bean
    ProcessInstanceInfo getProcessInstanceInfo(){
       return new ProcessInstanceInfo();
   }


}
