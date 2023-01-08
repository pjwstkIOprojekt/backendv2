package com.gary.backendv2.config;

import com.gary.backendv2.model.enums.RoleName;
import com.gary.backendv2.security.jwt.AuthEntryPointJwt;
import com.gary.backendv2.security.jwt.AuthTokenFilter;
import com.gary.backendv2.security.service.UserDetailsService;
import com.gary.backendv2.security.RoleOrder;
import com.gary.backendv2.utils.MaptilerConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            // other public endpoints of your API may be appended to this array
            "/enum/**",
            "/facility/**"
    };
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();

        RoleOrder roleOrder = new RoleOrder();
        try {
            roleOrder.addRule(RoleName.ADMIN, RoleName.AMBULANCE_MANAGER);
            roleOrder.addRule(RoleName.ADMIN, RoleName.DISPATCHER);
            roleOrder.addRule(RoleName.DISPATCHER, RoleName.EMPLOYEE);
            roleOrder.addRule(RoleName.AMBULANCE_MANAGER, RoleName.EMPLOYEE);
            roleOrder.addRule(RoleName.MEDIC, RoleName.EMPLOYEE);
            roleOrder.addRule(RoleName.AMBULANCE_MANAGER, RoleName.MEDIC);
            roleOrder.addRule(RoleName.MEDIC, RoleName.USER);
            roleOrder.addRule(RoleName.DISPATCHER, RoleName.USER);
        } catch (Exception e) {
            log.error(e.getMessage());

            System.exit(SpringApplication.exit(applicationContext, () -> -1));
        }

        String hierarchy = roleOrder.getRoleHierarchyOrder();

        roleHierarchy.setHierarchy(hierarchy);

        return roleHierarchy;
    }

    @Bean
    public DefaultWebSecurityExpressionHandler roleHierarchyWebSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }

    @Bean
    public MaptilerConstants maptilerConstants() {
        return new MaptilerConstants();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                // TODO change permitAlls to correct role access
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/hello/user/**").hasRole("USER")
                .antMatchers("/hello/admin/**").hasRole("ADMIN")
                .antMatchers("/hello/dispatch/create").permitAll()
                .antMatchers("/allergy").permitAll()
                .antMatchers("/allergy/**").permitAll()
                .antMatchers("/trusted/**").permitAll()
                .antMatchers("/medical_info").permitAll()
                .antMatchers("/medical_info/**").permitAll()
                .antMatchers("/ambulance/**").permitAll()
                .antMatchers("/disease/**").permitAll()
                .antMatchers("/equipment/**").permitAll()
                .antMatchers("/tutorial/**").permitAll()
                .antMatchers("/dispatch/**").hasRole("DISPATCHER")
                .antMatchers("/employee/**").hasRole("EMPLOYEE")
                .antMatchers("/incident/**").hasRole("EMPLOYEE")
                .antMatchers("/backup/**").hasRole("EMPLOYEE")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/accident_report/**").permitAll()
                .antMatchers("/item/**").permitAll()
                .antMatchers("/employee/**").hasRole("EMPLOYEE")
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
