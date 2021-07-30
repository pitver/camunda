package ru.vershinin.config;

import org.camunda.bpm.engine.impl.plugin.AdministratorAuthorizationPlugin;
//import org.camunda.bpm.identity.impl.ldap.plugin.LdapIdentityProviderPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/*import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;*/

/*
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/").permitAll();
        http.headers().frameOptions().disable();
    }

    @Bean
    public static AdministratorAuthorizationPlugin administratorAuthorizationPlugin() {
        AdministratorAuthorizationPlugin plugin = new AdministratorAuthorizationPlugin();
        plugin.setAdministratorUserName("camunda");
        return plugin;
    }

    @Bean
    public static LdapIdentityProviderPlugin ldapIdentityProviderPlugin() {
        LdapIdentityProviderPlugin plugin = new LdapIdentityProviderPlugin();
        plugin.setServerUrl("ldap://localhost:389");
        plugin.setAcceptUntrustedCertificates(true);
        plugin.setManagerDn("cn=Manager,dc=maxcrc,dc=com");
        plugin.setManagerPassword("secret");

        plugin.setBaseDn("dc=maxcrc,dc=com");

        plugin.setUserSearchFilter("(objectclass=person)");

        plugin.setUserFirstnameAttribute("cn");
        plugin.setUserLastnameAttribute("sn");
        plugin.setUserEmailAttribute("mail");
        plugin.setUserPasswordAttribute("userPassword");

        plugin.setGroupSearchFilter("(objectclass=groupOfNames)");
        plugin.setGroupIdAttribute("cn");
        plugin.setGroupNameAttribute("cn");
        plugin.setGroupMemberAttribute("member");

      */
/*  plugin.setAuthorizationCheckEnabled(true);*//*

        return plugin;
    }

}*/
