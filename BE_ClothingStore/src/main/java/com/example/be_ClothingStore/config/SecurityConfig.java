package com.example.be_ClothingStore.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;


@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    @Value("${jwt.secret}")
    private String jwtKey;

    public SecurityConfig(@Lazy JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(
             HttpSecurity http,
             CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler) throws Exception {
        http
                .csrf(c -> c.disable())
                .authorizeHttpRequests(
                        authz -> authz
                                .requestMatchers("/no-auth/**").permitAll() //người chưa đăng nhập
                                .requestMatchers("/auth/customer/**").hasRole("CUSTOMER") //người đã đăng nhập
                                .requestMatchers("/auth/admin/**", "/auth/customer/**").hasRole("ADMIN") //người đã đăng nhập làm admin
                                .anyRequest().authenticated())
                                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults())
                                .authenticationEntryPoint(customAuthenticationEntryPoint))
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                                .exceptionHandling(exception -> exception
                .authenticationEntryPoint(customAuthenticationEntryPoint) // 401 
                .accessDeniedHandler(customAccessDeniedHandler) //403 
            )
                .formLogin(f -> f.disable()) //disable form login
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.addAllowedOrigin("http://localhost:5173");  // Cho phép frontend tại localhost:5173
                    config.addAllowedMethod("*");  // Cho phép tất cả các phương thức HTTP
                    config.addAllowedHeader("*");  // Cho phép tất cả các header
                    config.setAllowCredentials(true);  // Quan trọng: cho phép credentials
                    return config;
                }));
        return http.build();
    }

    // @Bean
    // public JwtDecoder jwtDecoder() {
    //     NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
    //             getSecretKey()).macAlgorithm(SecurityUtil.JWT_ALGORITHM).build();
    //     return token -> {
    //         try {
    //             return jwtDecoder.decode(token);
    //         } catch (Exception e) {
    //             System.out.println("JWR error: " + e.getMessage());
    //             throw e;
    //         }
    //     };
    // }

    // @Bean
    // public JwtEncoder jwtEncoder() {
    //     return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    // }

    // private SecretKey getSecretKey() {
    //     byte[] keyBytes = Base64.from(jwtKey).decode();
    //     return new SecretKeySpec(keyBytes, 0, keyBytes.length, SecurityUtil.JWT_ALGORITHM.getName());
    // }

    @Bean
     public JwtAuthenticationConverter jwtAuthenticationConverter() {
         JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
         grantedAuthoritiesConverter.setAuthorityPrefix("");
         grantedAuthoritiesConverter.setAuthoritiesClaimName("role");
         JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
         jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
         return jwtAuthenticationConverter;
     }
    
}
