### SpringBoot集成Swagger

在我们的日常开发中会有很多的接口，如何对这些接口进行管理是一个非常困难的问题。Swagger 可以很好的帮助我们规范接口的开发，清晰的看到每一个接口的详情信息，可以大大的提高我们团队的效率
需要使用 Swagger 我们先引入依赖包，如下：

```xml
<dependency>
  <groupId>io.springfox</groupId>
  <artifactId>springfox-swagger2</artifactId>
  <version>2.9.2</version>
</dependency>
<dependency>
  <groupId>io.springfox</groupId>
  <artifactId>springfox-swagger-ui</artifactId>
  <version>2.9.2</version>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>com.github.xiaoymin</groupId>
  <artifactId>swagger-bootstrap-ui</artifactId>
  <version>1.9.3</version>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
然后我们需要写一个配置类，来配置我们的 Swagger
```java
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI // 该注解开启了用户名密码登录功能
public class SwaggerConfig {

    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(Predicates.or(
                        RequestHandlerSelectors.basePackage("com.demo.controller")
                ))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("描述")
                .version("1.0.0")
                .build();
    }
}
```
到这里如果不开启用户名密码登录就已经可以开始使用了。如果开启了注解`@EnableSwaggerBootstrapUI`，我们需要在 springboot 中配置一下用户名密码
```java
swagger.basic.enable=true
swagger.basic.username=username
swagger.basic.password=password
spring.mvc.pathmatch.matching-strategy = ANT_PATH_MATCHER
```
到这里基本上就可以没问题的使用了
但是很多同学还是无法使用，大部分都会遇到以下的报错信息，导致无法启动：
```java
Failed to start bean 'documentationPluginsBootstrapper'; nested exception is java.lang.NullPointerException
```
这个是 springboot 和 Swagger 版本的兼容问题。我也找了很多的解决办法，最终的解决办法如下：
首先，我们需要在配置文件增加以下配置
```java
spring.mvc.pathmatch.matching-strategy = ANT_PATH_MATCHER
```
然后在我们的配置类中增加以下内容：
```java
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfig {

    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(Predicates.or(
                        RequestHandlerSelectors.basePackage("com.demo.controller")
                ))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("描述")
                .version("1.0.0")
                .build();
    }

    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties, Environment environment) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping, null);
    }

    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment, String basePath) {
        return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
    }
}
```
到这里，就可以完整的使用啦。打开网址[http://localhost:8086/swagger-ui.html](http://localhost:8086/swagger-ui.html) 即可使用
#### 额外补充
如果在我们的项目中使用了spring security 的话，那么还会遇到登录被拦截的情况，因为我们的请求路径首先会交给spring security 进行过滤。那我们需要怎么解决这个问题呢，只要让我们的资源路径直接进行放行就可以了。所以使用spring security 的同学需要多一个步骤，就是配置spring security 放行 Swagger 的资源。
```java
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/wabjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v2/**").permitAll();
    }
}
```
到这里就结束啦
#### spring security 的高级配置法
```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
@ConditionalOnClass(WebSecurityConfigurerAdapter.class)
public class MultiSecurityConfig {

    @Configuration
    @EnableConfigurationProperties(value = SwaggerSecurityProperties.class)
    static class SwaggerConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Autowired
        private SwaggerSecurityProperties swaggerSecurityProperties;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            //  这个配置只针对 swagger-ui 相关请求
            http.authorizeRequests()
                    .antMatchers("/swagger-resources/**",
                            "/webjars/**", "/v2/**", "/swagger-ui.html",  "/doc.html")
                    .authenticated()
                    .and().httpBasic()
                    .and().csrf().disable();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            auth.inMemoryAuthentication()
                    .passwordEncoder(new BCryptPasswordEncoder())
                    .withUser(swaggerSecurityProperties.getUsername())
                    .password(passwordEncoder.encode(swaggerSecurityProperties.getPassword()))
                    .roles("admin");
        }
    }

    @Configuration
    @Import(SecurityProblemSupport.class)
    @Order(1)
    static class CommonConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        private JHipsterProperties jHipsterProperties;

        private TokenValidater tokenProvider;

        @Resource
        private CorsFilter corsFilter;

        @Autowired
        private CustomAccessDeniedHandler accessDeniedHandler;

        @Value("${app.id}")
        private Long applicationId;

        @Value("${super-admin.user}")
        private String superAdmin;

        @Autowired
        private AuthConfigService authConfigService;

        @Autowired
        private CustomAccessDecisionManager customAccessDecisionManager;

        @Autowired
        private SecurityProblemSupport securityProblemSupport;

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Override
        public void configure(WebSecurity web) {

        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                    .csrf().disable()
                    .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling()
                    .authenticationEntryPoint(securityProblemSupport)
                    .accessDeniedHandler(accessDeniedHandler)
                    .and()
                    .headers()
                    .contentSecurityPolicy(jHipsterProperties.getSecurity().getContentSecurityPolicy())
                    .and()
                    .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                    .and()
                    .permissionsPolicy().policy("camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()")
                    .and()
                    .frameOptions()
                    .deny()
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .requestMatchers(x -> x.antMatchers("/knowlege-markResource/**",
                            "/knowlege-baike/**", "/knowlege-markResource/**"))
                    .httpBasic()
                    .and()
                    .apply(securityConfigurerAdapter());
            // @formatter:on

        }

        private JWTConfigurer securityConfigurerAdapter() {
            if (tokenProvider == null) {
                tokenProvider = SpringContextUtil.getBean(TokenValidater.class);
            }
            return new JWTConfigurer(tokenProvider);
        }

        @Bean
        @Qualifier("vanillaRestTemplate")
        public RestTemplate vanillaRestTemplate() {
            return new RestTemplate();
        }
    }

}
```
