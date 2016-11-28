package com.ail.narad.config;

import javax.inject.Inject;

import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import com.ail.narad.security.AuthoritiesConstants;
import com.ail.narad.security.Http401UnauthorizedEntryPoint;
import com.ail.narad.security.xauth.TokenProvider;
import com.ail.narad.security.xauth.XAuthTokenConfigurer;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
public class SecurityConfiguration extends KeycloakWebSecurityConfigurerAdapter {

  @Inject
  private UserDetailsService userDetailsService;

  @Inject
  private TokenProvider tokenProvider;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Autowired
  public KeycloakClientRequestFactory keycloakClientRequestFactory;

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public KeycloakRestTemplate keycloakRestTemplate() {
    return new KeycloakRestTemplate(keycloakClientRequestFactory);
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/scripts/**.{js,html}").antMatchers("/bower_components/**")
        .antMatchers("/i18n/**").antMatchers("/assets/**").antMatchers("/swagger-ui/index.html")
        .antMatchers("/test/**").antMatchers("/console/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    super.configure(http);
    http.exceptionHandling().and().csrf().disable().headers().frameOptions().disable().and().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
        .antMatchers("/api/register").permitAll().antMatchers("/api/activate").permitAll()
        .antMatchers("/api/v1/sendcallbackdetails").permitAll()
        .antMatchers("/api/authenticate").permitAll().antMatchers("/api/account/reset_password/init")
        .permitAll().antMatchers("/api/account/reset_password/finish").permitAll().antMatchers("/api/logs/**")
        .hasAuthority(AuthoritiesConstants.ADMIN).antMatchers("/api/audits/**")
        .hasAuthority(AuthoritiesConstants.ADMIN).antMatchers("/api/**")        
        .hasAuthority(AuthoritiesConstants.ADMIN).antMatchers("/api/*/*").authenticated()
        .antMatchers("/metrics/**").hasAuthority(AuthoritiesConstants.ADMIN).antMatchers("/health/**")
        .hasAuthority(AuthoritiesConstants.ADMIN).antMatchers("/trace/**")
        .hasAuthority(AuthoritiesConstants.ADMIN).antMatchers("/dump/**")
        .hasAuthority(AuthoritiesConstants.ADMIN).antMatchers("/shutdown/**")
        .hasAuthority(AuthoritiesConstants.ADMIN).antMatchers("/beans/**")
        .hasAuthority(AuthoritiesConstants.ADMIN).antMatchers("/configprops/**")
        .hasAuthority(AuthoritiesConstants.ADMIN).antMatchers("/info/**")
        .hasAuthority(AuthoritiesConstants.ADMIN).antMatchers("/autoconfig/**")
        .hasAuthority(AuthoritiesConstants.ADMIN).antMatchers("/env/**")
        .hasAuthority(AuthoritiesConstants.ADMIN).antMatchers("/trace/**")
        .hasAuthority(AuthoritiesConstants.ADMIN).antMatchers("/mappings/**")
        .hasAuthority(AuthoritiesConstants.ADMIN).antMatchers("/v2/api-docs/**").permitAll()
        .antMatchers("/configuration/security").permitAll().antMatchers("/configuration/ui").permitAll()
        .antMatchers("/swagger-ui/index.html").hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers("/protected/**").authenticated()
        .and()
              .apply(securityConfigurerAdapter());

  }

  private XAuthTokenConfigurer securityConfigurerAdapter() {
    return new XAuthTokenConfigurer(userDetailsService, tokenProvider);
  }

  @Bean
  public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
    return new SecurityEvaluationContextExtension();
  }

  /**
   * Registers the KeycloakAuthenticationProvider with the authentication
   * manager.
   */
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(keycloakAuthenticationProvider());
  }

  /**
   * Defines the session authentication strategy.
   */
  @Bean
  @Override
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new NullAuthenticatedSessionStrategy();
  }

  @Bean
  public FilterRegistrationBean keycloakAuthenticationProcessingFilterRegistrationBean(
      KeycloakAuthenticationProcessingFilter filter) {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
    registrationBean.setEnabled(true);
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean keycloakPreAuthActionsFilterRegistrationBean(KeycloakPreAuthActionsFilter filter) {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
    registrationBean.setEnabled(true);
    return registrationBean;
  }
}