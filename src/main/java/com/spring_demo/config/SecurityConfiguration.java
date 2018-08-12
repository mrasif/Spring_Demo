package com.spring_demo.config;

import com.spring_demo.repositories.UserRepository;
import com.spring_demo.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(getbCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] resources = new String[]{
                "/", "/css/**","/icons/**","/images/**","/js/**"
        };
        String[] anonymous_resources = new String[]{
                "/registration", "/login**"
        };
        String[] admin_resources=new String[]{
            "/admins/**"
        };
        String[] moderator_resources=new String[]{
            "/moderators/**"
        };
        String[] auth_resources=new String[]{
            "/users/**"
        };
//        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers(resources).permitAll()
                .antMatchers(admin_resources).hasAuthority("ROLE_ADMIN")
                .antMatchers(moderator_resources).hasAuthority("ROLE_MODERATOR")
                .antMatchers(auth_resources).authenticated()
                .antMatchers(anonymous_resources).not().authenticated()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout");

    }

    /*private PasswordEncoder getPasswordEncoder(){
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                System.out.println("encode: "+rawPassword);
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                System.out.println("matches: raw=>"+rawPassword+"\tEncoded=>"+encodedPassword);
                return encodedPassword.equalsIgnoreCase(rawPassword.toString());
            }
        };
    }*/

    private BCryptPasswordEncoder getbCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
