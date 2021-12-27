package com.github.pdgs.MyPengAPI.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()  // rest api이므로 기본설정 안함. 기본설정은 비인증 시 로그인 폼 화면으로 리다이렉트 된다.
                .csrf().disable() // rest api이므로 crsf 보안 필요 없음 disable
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT token으로 생성하므로 세션이 필요없음 (생성 안함)
                .and()// 다음 리퀘스트에 대한 사용권한 체크
                .authorizeRequests() // 가입 및 인증 주소는 누구나 접근 가능
                .antMatchers(HttpMethod.GET, "/MyPengAPI/**").permitAll() // MyPeng 로 시작하는 GET 요청 리소스는 누구나 접근 가능
                .anyRequest().hasRole("USER") // 그외 나머지 요청은 모두 인증된 회원만 가능
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);
                // JWT 토큰 필터를 ID / Password 인증 필터 전에 넣는다.
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resource/**",
                "/swagger-ui.html", "/webjars/**", "/swagger/**");
    }
}
