package com.softpala.SalesDeepDive_ERP.security;

import org.springframework.http.HttpMethod;
import com.softpala.SalesDeepDive_ERP.auth.ApplicationUserService;
import com.softpala.SalesDeepDive_ERP.jwt.JwtConfig;
import com.softpala.SalesDeepDive_ERP.jwt.JwtTokenVerifier;
import com.softpala.SalesDeepDive_ERP.jwt.JwtEmailAndPasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;

import javax.crypto.SecretKey;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = false, jsr250Enabled = true)
@CrossOrigin(origins = "https://production-frontend.ecoshelter.com")
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    private final boolean requireSsl;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService, SecretKey secretKey, JwtConfig jwtConfig,
                                     @Value("${security.require-ssl:false}") boolean requireSsl) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
        this.requireSsl = requireSsl;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowedOrigins(List.of("https://production-frontend.ecoshelter.com/","http://83.149.110.184:8087/","https://production-frontend.ecoshelter.com/","https://production-frontend.ecoshelter.com"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT","OPTIONS","PATCH", "DELETE"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(List.of("Authorization"));


       // http.cors().configurationSource(request -> corsConfiguration);
        http.cors();


        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtEmailAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey))
                .addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfig), JwtEmailAndPasswordAuthenticationFilter.class);

        if (requireSsl) {
            http.requiresChannel(channel -> channel.anyRequest().requiresSecure());
        }

        http.authorizeRequests()
                // Laravel's erp:sync-transfers pulls this outbox every ten
                // minutes. The "/..." list below uses "/api/*", which matches a
                // single path segment only, so it never covered this two-segment
                // path and every pull came back 403. GET only - the ack endpoint
                // beneath it mutates state and nothing calls it.
                .antMatchers(HttpMethod.GET, "/api/transfers/outbox").permitAll()
                .antMatchers("/","/login","login","/login/refreshToken","/api3/*","/api3/cl/*","api3/cl/*","/api2/*","/api2","/api/*","/user/*", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api2d/**").hasAuthority("read:authority")
                .anyRequest()
                .authenticated();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }

}
