package com.modules.data.mybatis;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName MyBatisPlusConfig
 * @Author
 * @Description Mybatis-Plus配置
 * @Version 1.0
 **/

@EnableTransactionManagement
@Configuration
@MapperScan({"com.web.**.mapper"})
public class MyBatisPlusConfig implements TransactionManagementConfigurer {

   /* @Autowired
    private PreTenantHandler preTenantHandler;
*/
    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        List<ISqlParser> sqlParserList = new ArrayList<>();
        // 攻击 SQL 阻断解析器、加入解析链
        sqlParserList.add(new BlockAttackSqlParser());
        // 多租户拦截
        //TenantSqlParser tenantSqlParser = new TenantSqlParser();
        //tenantSqlParser.setTenantHandler(preTenantHandler);
        //sqlParserList.add(tenantSqlParser);
        paginationInterceptor.setSqlParserList(sqlParserList);
        return paginationInterceptor;
    }

    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        //白名单：
        //servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
        //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的即提示:Sorry, you are not permitted to view this page.
        //servletRegistrationBean.addInitParameter("deny", "192.168.1.100");
        //登录查看信息的账号密码.
        servletRegistrationBean.addInitParameter("loginUsername", "druid");
        servletRegistrationBean.addInitParameter("loginPassword", "123456");
        //是否能够重置数据.
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean statFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        //添加过滤规则.
        filterRegistrationBean.addUrlPatterns("/*");
        //添加不需要忽略的格式信息.
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }


    //定义db1
    @Bean(name = "db1")
    @ConfigurationProperties(prefix = "spring.datasource.db1")//与配置文件中的层次结构相同
    @Primary
    public DataSource db1() {
        return DruidDataSourceBuilder.create().build();
    }
    //定义db2
    @Bean(name = "db2")
    @ConfigurationProperties(prefix = "spring.datasource.db2")//与配置文件中的层次结构相同
    public DataSource db2() {
        return DruidDataSourceBuilder.create().build();
    }


    //定义db2
    @Bean(name = "db3")
    @ConfigurationProperties(prefix = "spring.datasource.db3")//与配置文件中的层次结构相同
    public DataSource db3() {
        return DruidDataSourceBuilder.create().build();
    }
    /**
     * 动态数据源配置
     * @return
     */
    @Bean
    public DataSource multipleDataSource(@Qualifier("db1") DataSource db1, @Qualifier("db2") DataSource db2 , @Qualifier("db3") DataSource db3 ) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DBTypeEnum.db1.getValue(), db1);
        targetDataSources.put(DBTypeEnum.db2.getValue(), db2);
        targetDataSources.put(DBTypeEnum.db3.getValue(), db3);
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(db1);
        return dynamicDataSource;
    }

    @Bean("sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        //***导入MybatisSqlSession配置***
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        //指明数据源
        sqlSessionFactory.setDataSource(multipleDataSource(db1(), db2(), db3() ));
        //指明mapper.xml位置(配置文件中指明的xml位置会失效用此方式代替，具体原因未知)
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/*/**Mapper.xml"));
        //指明实体扫描(多个package用逗号或者分号分隔)
        sqlSessionFactory.setTypeAliasesPackage("javademo.tyh.model.base;javademo.tyh.model.hotel");

        //***导入Mybatis配置***
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        sqlSessionFactory.setConfiguration(configuration);
        sqlSessionFactory.setPlugins(new Interceptor[]{paginationInterceptor()});

        //***导入全局配置***
        //sqlSessionFactory.setGlobalConfig(globalConfiguration());
        return sqlSessionFactory.getObject();
    }

    @Override
    public TransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(multipleDataSource(db1(), db2() , db3() ));
    }

    /**
     * 在代码中配置MybatisPlus替换掉application.yml中的配置
     * @return
     */
  /*  @Bean
    public GlobalConfiguration globalConfiguration() {
        GlobalConfiguration conf = new GlobalConfiguration(new LogicSqlInjector());
        //主键类型 0:数据库ID自增, 1:用户输入ID,2:全局唯一ID (数字类型唯一ID), 3:全局唯一ID UUID
        conf.setIdType(0);
        //字段策略(拼接sql时用于判断属性值是否拼接) 0:忽略判断,1:非NULL判断,2:非空判断
        conf.setFieldStrategy(2);
        //驼峰下划线转换含查询column及返回column(column下划线命名create_time，返回java实体是驼峰命名createTime，开启后自动转换否则保留原样)
        conf.setDbColumnUnderline(true);
        //是否动态刷新mapper
        conf.setRefresh(true);
        return conf;
    }*/
}
