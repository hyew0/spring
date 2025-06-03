package hello.login;

import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
/*
    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }*/

    @Bean
    public FilterRegistrationBean loginCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
    /*
    setFilter(new LoginCheckFilter()) : 로그인 필터를 등록한다.
    setOrder(2) : 순서를 2번으로 잡았다. 로그 필터 다음에 로그인 필터가 적용된다.
    addUrlPatterns("/*") : 모든 요청에 로그인 필터를 적용한다.
    * */

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error");
    }
    /*
    - 인터셉터와 필터가 중복되지 않도록 필터를 등록하기 위한 logFilter() 의 @Bean 은 주석처리하자.

    - WebMvcConfigurer 가 제공하는 addInterceptors() 를 사용해서 인터셉터를 등록할 수 있다.
        - registry.addInterceptor(new LogInterceptor()) : 인터셉터를 등록한다.
        - order(1) : 인터셉터의 호출 순서를 지정한다. 낮을 수록 먼저 호출된다.
        - addPathPatterns("/**") : 인터셉터를 적용할 URL 패턴을 지정한다.
        - excludePathPatterns("/css/**", "/*.ico", "/error") : 인터셉터에서 제외할 패턴을 지정한다.

    * */
}
