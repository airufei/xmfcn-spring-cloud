/**
package com.cn.xmf.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

*/
/**
 *当前服务安全密码验证
 *//**

@EnableWebSecurity
@Configuration
public class ServiceSecurity extends WebSecurityConfigurerAdapter {


    @Value("${users.admin.name}")
    private String adminName;
    @Value("${users.admin.password}")
    private String adminPassword;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
         http.csrf().disable();
        http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser(adminName).password(adminPassword).roles("admin");// 管理员
    }

}
*/
