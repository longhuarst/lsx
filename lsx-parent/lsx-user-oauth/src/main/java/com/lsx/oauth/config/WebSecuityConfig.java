package com.lsx.oauth.config;


import entity.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity          // 开启websecurity
@Order(-1)
public class WebSecuityConfig extends WebSecurityConfigurerAdapter {

    /*
    * 忽略安全拦截的URL
    * @param web
    * @throws Exception
    * */

    @Override
    public void configure(WebSecurity web) throws Exception {
        //super.configure(web);
        System.out.println("void configure(WebSecurity web) throws Exception");
        web.ignoring().antMatchers("/oauth/login",
                "/oauth/logout",
                "/oauth/toLogin",
                "/user/login",
                "/login.html",
                "/css/**",
                "/data/**",
                "/fonts/**",
                "/img/**",
                "/js/**");
    }

    /*
    * 创建授权管理认证对象
    * @return
    * @throws Exception
    * */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        //return super.authenticationManagerBean();
        AuthenticationManager manager = super.authenticationManagerBean();
        return manager;
    }

    /*
    * 采用BCryptPasswordEncoder对密码进行编码
    * @return
    * */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /*
    * @param http
    * @throws Exception
    * */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);

//        http.httpBasic();//设置basic登陆

        http.csrf().disable()
                    .httpBasic()    //启用Http基本身份认证
                .and()
                    .formLogin()    //启动表单身份认证
                .and()
                    .authorizeRequests()
                    .antMatchers("/login").permitAll()
                .and()
                    .authorizeRequests()
                    .antMatchers("/toLogin").permitAll()
                .and()
                    .authorizeRequests()    //限制基于request的请求访问
                    .anyRequest()
                    .authenticated();//其他都需要经过认证才可以访问
//                    .hasRole("USER");   //其他请求都需要经过认证   这样会出现问题

        //开启表单登陆
        http.formLogin().loginPage("/oauth/toLogin")//设置访问登陆页面的路径
                   .loginProcessingUrl("oauth/login");//设置执行登陆操作的路径

    }




    /*
    * 设置内存账户
    * */
    //这个似乎不能和 自定义的 同时使用
    //The name of the configureGlobal method is not important. However, it
    //is important to only configure AuthenticationManagerBuilder in a class
    //annotated with either @EnableWebSecurity, @EnableGlobalMethodSecurity,
    //or @EnableGlobalAuthentication. Doing otherwise has unpredictable
    //results.
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("lsx")
//                .password(new BCryptPasswordEncoder().encode("lsxlsx"))
//                .roles("USER")
//                .and()
//                .withUser("longhua")
//                .password(new BCryptPasswordEncoder().encode("longhua"))
//                .roles("ADMIN");
//
////        auth.jdbcAuthentication()
//
////        auth.userDetailsService();
//
//    }









    //springsecurity 的  UserDetailsService  好像会比 在oauth之前验证


//    @Bean
//    UserDetailsService customUserService(){
//        return new CustomUserService();
//    }
//
//
//    /*
//    *
//    * */
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
////        auth.inMemoryAuthentication().withUser("user").password("").roles("");
//
////        auth.inMemoryAuthentication()
////                .withUser("lsx")
////                .password(new BCryptPasswordEncoder().encode("lsxlsx"))
////                .roles("USER")
////                .and()
////                .withUser("longhua")
////                .password(new BCryptPasswordEncoder().encode("longhua"))
////                .roles("ADMIN");
//
//
////        auth.userDetailsService(customUserService()); //自定义的userDetailsService 服务
////        super.configure(auth);
//    }
















}
