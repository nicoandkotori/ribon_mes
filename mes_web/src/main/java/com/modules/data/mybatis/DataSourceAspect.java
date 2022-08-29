package com.modules.data.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
@Aspect
@Order(-100)
public class DataSourceAspect {

   /* @Pointcut("execution(* com.web.*.mapper..*.*(..))")
    private void db1Aspect() {
    }

    @Before( "db1Aspect()" )
    public void db1(JoinPoint joinPoint) {
        log.info("切换到db1 数据源...");
        //DbContextHolder.setDbType(DBTypeEnum.db1);
        setDataSource(joinPoint, DBTypeEnum.db1);
    }*/
    /**
     * 添加注解方式,如果有注解优先注解,没有则按传过来的数据源配置
     * @param joinPoint
     * @param dbTypeEnum
     */
    private void setDataSource(JoinPoint joinPoint, DBTypeEnum dbTypeEnum) {
        MapperMethod.MethodSignature methodSignature = (MapperMethod.MethodSignature) joinPoint.getSignature();
        DataSourceSwitch dataSourceSwitch = methodSignature.getReturnType().getAnnotation(DataSourceSwitch.class);//.getMethod().getAnnotation(DataSourceSwitch.class);
        if (Objects.isNull(dataSourceSwitch) || Objects.isNull(dataSourceSwitch.value())) {
            DbContextHolder.setDbType(dbTypeEnum);
        }else{
            log.info("根据注解来切换数据源,注解值为:"+dataSourceSwitch.value());
            switch (dataSourceSwitch.value().getValue()) {
                case "db1":
                    DbContextHolder.setDbType(DBTypeEnum.db1);
                    break;
                case "db2":
                    DbContextHolder.setDbType(DBTypeEnum.db2);
                    break;
                case "db3":
                    DbContextHolder.setDbType(DBTypeEnum.db3);
                    break;
                default:
                    DbContextHolder.setDbType(dbTypeEnum);
            }
        }
    }

    /*@Pointcut("@within(com.modules.data.mybatis.DataSource) || @annotation(com.modules.data.mybatis.DataSource)")
    public void pointCut(){

    }

    @Before("pointCut() && @annotation(dataSource)")
    public void doBefore(DBTypeEnum  dataSource){
        log.info("选择数据源---"+dataSource.getValue());
        DbContextHolder.setDbType(dataSource);
    }

    @After("pointCut()")
    public void doAfter(){
        DbContextHolder.clearDbType();
    }*/

    /*@Pointcut("execution(* com.warm.system.service.db1..*.*(..))")
    private void db1Aspect() {
    }

    @Pointcut("execution(* com.warm.system.service.db2..*.*(..))")
    private void db2Aspect() {
    }

    @Before( "db1Aspect()" )
    public void db1() {
        log.info("切换到db1 数据源...");
        DbContextHolder.setDbType(DBTypeEnum.db1);
    }

    @Before("db2Aspect()" )
    public void db2 () {
        log.info("切换到db2 数据源...");
        DbContextHolder.setDbType(DBTypeEnum.db2);
    }*/
}