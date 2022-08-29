package com.modules.security.config;

import com.modules.security.MyUserDetailsServiceImpl;
import com.modules.security.filter.JwtAuthenticationTokenFilter;
import com.modules.security.handle.JwtAuthenticationEntryPoint;
import com.modules.security.handle.RestAuthenticationAccessDeniedHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Classname WebSecurityConfig
 * @Description Security配置类
 * @Date 2019-05-07 09:10
 * @Version 1.0
 */
@Slf4j
@Configuration
@EnableWebSecurity
//添加方法级别权限控制，prePostEnabled=true会拦截注解了@PreAuthorize的方法
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private RestAuthenticationAccessDeniedHandler restAuthenticationAccessDeniedHandler;

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;


    @Autowired
    private MyUserDetailsServiceImpl userDetailsService;    //引用自己的

    // 注入短信登录的相关配置
//    @Autowired
//    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;


    /**
     * 解决 无法直接注入 AuthenticationManager
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /**
     * 配置策略
     *
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()

                // 短信登录配置
                //.apply(smsCodeAuthenticationSecurityConfig).and()
                //.apply(springSocialConfigurer).and()
                // 认证失败处理类
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .exceptionHandling().accessDeniedHandler(restAuthenticationAccessDeniedHandler).and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 过滤请求
                .authorizeRequests()
                // 对于登录login 图标 要允许匿名访问
                .antMatchers("/login/**", "/account/login/**", "/favicon.ico", "/app-debug.apk", "/huba.apk", "/pad.apk", "/socialSignUp", "/bind", "/register/**", "/**/*export*", "/**/export1", "/**/export/*", "/open/**", "/**/down", "/system/index/getaccountname").anonymous()
                .antMatchers(HttpMethod.GET, "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/**/*.jpg", "/**/*.jpeg", "/**/*.png", "/**/*.gif", "/**/*.ico", "/**/*.woff", "/**/*.ttf", "/**/*.xls", "/**/*.xlsx", "/**/*.doc", "/**/*.docx", "/**/*.pdf")
                .permitAll()
                .antMatchers("/captcha.jpg", "/*.txt", "/**/*.txt", "/druid/**").anonymous()
                .antMatchers("/st/pickingplan/**", "/st/**", "/report/attendance/importdata").permitAll()
                .antMatchers("/**/down", "/**/down*", "/**/export*", "/**/viewpdf").permitAll()
                .antMatchers("/st/pickingplan/**", "/st/**", "/st/pickingplan/find*").permitAll()
                .antMatchers("/mo/**").permitAll()
                .antMatchers("/basicinfo/**").permitAll()
                //工位机全部以/work 开头，  然后转发工位机单独的应用

                .antMatchers("/api/**", "/test/**").anonymous()
                .antMatchers("/api/pda/**").anonymous()
                .antMatchers("/demo/rabbitmqtest/**").anonymous()
                .antMatchers("/demo/**").anonymous()
                .antMatchers("/api/st/input/**")
                .permitAll()
                .antMatchers("/login.html").anonymous()

                .antMatchers("/").permitAll()
                // 访问/user 需要拥有admin权限
                //  .antMatchers("/user").hasAuthority("ROLE_ADMIN")
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable();

                /*.and()
                .formLogin()
                .loginPage("/login.html")
               // .failureHandler(preAuthenticationFailureHandler)
                //.successHandler(preAuthenticationSuccessHandler)
                .and()
                .headers().frameOptions().disable();*/
        // 添加JWT filter 用户名登录
        httpSecurity
                // 添加图形证码校验过滤器
                // .addFilterBefore(imageCodeFilter, UsernamePasswordAuthenticationFilter.class)
                // 添加JWT验证过滤器
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加短信验证码过滤器
        //.addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /*@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                // 设置UserDetailsService
                .userDetailsService(userDetailsService)
                // 使用BCrypt进行密码的hash
                .passwordEncoder(passwordEncoder());
    }*/

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                // 设置UserDetailsService
                .userDetailsService(this.userDetailsService)
                // 使用BCrypt进行密码的hash
                .passwordEncoder(passwordEncoder());
    }

    /**
     * 装载BCrypt密码编码器 密码加密
     *
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

