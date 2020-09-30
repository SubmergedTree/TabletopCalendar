package de.submergedtree.tabletopcalendar.security
//
//import org.springframework.beans.factory.annotation.Qualifier
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.core.annotation.Order
//import org.springframework.security.authentication.ReactiveAuthenticationManager
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
//import org.springframework.security.config.web.server.SecurityWebFiltersOrder
//import org.springframework.security.config.web.server.ServerHttpSecurity
//import org.springframework.security.core.Authentication
//import org.springframework.security.core.authority.SimpleGrantedAuthority
//import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
//import org.springframework.security.oauth2.core.OAuth2TokenValidator
//import org.springframework.security.oauth2.jwt.*
//import org.springframework.security.web.server.SecurityWebFilterChain
//import org.springframework.security.web.server.authentication.AuthenticationWebFilter
//import reactor.core.publisher.Mono
//
//@EnableWebFluxSecurity
//class XSecurityConfig(@Value("\${auth0.audience}") private val audience: String,
//                      @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}") private val issuer: String) {
//
//    @Bean
//    fun jwtDecoder(): JwtDecoder? {
//        val jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuer) as NimbusJwtDecoder
//        val audienceValidator: OAuth2TokenValidator<Jwt> = AudienceValidator(audience)
//        val withIssuer: OAuth2TokenValidator<Jwt> = JwtValidators.createDefaultWithIssuer(issuer)
//        val withAudience: OAuth2TokenValidator<Jwt> = DelegatingOAuth2TokenValidator(withIssuer, audienceValidator)
//        jwtDecoder.setJwtValidator(withAudience)
//        return jwtDecoder
//    }
//
//    @Bean
//    @Order(1)
//    fun jwtFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain? {
//        http.authorizeExchange()
//                .pathMatchers("/api/management/**").authenticated()
//                .and()
//                .oauth2ResourceServer().jwt()
//        return http.build()
//    }
//
//    @Bean
//    fun fatUrlFilterChain(http: ServerHttpSecurity,
//                          @Qualifier("fatUrlAuthFilter") fatUrlAuthFilter: AuthenticationWebFilter)
//            : SecurityWebFilterChain? {
//        http.authorizeExchange()
//                .pathMatchers("/api/user/**").authenticated()
//                .and()
//                .addFilterAt(fatUrlAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
//        return http.build()
//    }
//
//    @Bean
//    fun getReactiveAuthenticationManager(): ReactiveAuthenticationManager {
//        return ReactiveAuthenticationManager { auth ->
//            Mono.justOrEmpty(auth)
//        }
//    }
//
//    @Bean("fatUrlAuthFilter")
//    fun fatUrlAuthFilter(authManager: ReactiveAuthenticationManager) : AuthenticationWebFilter {
//        val filter = AuthenticationWebFilter(authManager)
//        filter.setServerAuthenticationConverter {
//            Mono.justOrEmpty(it)
//                    .map { it.request.queryParams.getFirst("calendarToken") ?: "" }
//                    .map {
//                        print("calendar token is: $it")
//                        val authentication = UsernamePasswordAuthenticationToken("userkey",
//                                it,
//                                listOf(SimpleGrantedAuthority("PLAYER")))
//                        authentication as Authentication
//                    }
//        }
//        return filter
//    }
//
//}