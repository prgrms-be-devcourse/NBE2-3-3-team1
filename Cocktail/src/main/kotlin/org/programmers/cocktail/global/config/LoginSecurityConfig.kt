package org.programmers.cocktail.global.config

import org.programmers.cocktail.login.service.LoginService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class LoginSecurityConfig {
    @Autowired
    private val loginService: LoginService? = null

//    @Bean
//    @Throws(Exception::class)
//    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
//        http
//            .csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
//            .authorizeHttpRequests(
//                Customizer { authorize: AuthorizationManagerRequestMatcherRegistry ->
//                    authorize
//
//                        .anyRequest().permitAll() // 나머지 요청은 인증 필요
//                }
//            )
//
//
//        //            .formLogin(
////                (login) -> login
////                    .loginPage("/login")
////                    //.defaultSuccessUrl("/loginsuccess")
////                    .failureUrl("/login")
////            );
//        return http.build()
//    }

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests(Customizer { authorize ->
                authorize.anyRequest().permitAll()
            })
        return http.build()
    }


    @Bean
    fun authProvider(): DaoAuthenticationProvider {
        val daoAuthenticationProvider = DaoAuthenticationProvider()

        daoAuthenticationProvider.setUserDetailsService(loginService)
        daoAuthenticationProvider.setPasswordEncoder(BCryptPasswordEncoder())

        return daoAuthenticationProvider
    }
}