package com.ruikai.middleware.db.router;

import com.ruikai.middleware.db.router.annotation.DbRouter;
import com.ruikai.middleware.db.router.strategy.IDbRouterStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/4/14 15:31
 * 数据路由切面， 通过自定义注解的方式，拦截被切面的方法，进行数据库路由
 */
@Aspect
@Slf4j
public class DbRouterJoinPoint {

    /**
     * 数据库路由配置
     */
    private DbRouterConfig dbRouterConfig;

    /**
     * 数据库路由策略
     */
    private IDbRouterStrategy dbRouterStrategy;

    public DbRouterJoinPoint(DbRouterConfig dbRouterConfig, IDbRouterStrategy dbRouterStrategy) {
        this.dbRouterConfig = dbRouterConfig;
        this.dbRouterStrategy = dbRouterStrategy;
    }

    @Pointcut("@annotation(com.ruikai.middleware.db.router.annotation.DbRouter)")
    public void aopPoint() {}

    @Around("aopPoint() && @annotation(dbRouter)")
    public Object dbRouter(ProceedingJoinPoint jp, DbRouter dbRouter) throws Throwable {
        String dbKey = dbRouter.key();
        if (StringUtils.isBlank(dbKey) && StringUtils.isBlank(dbRouterConfig.getRouterKey())) {
            throw new RuntimeException("annotation dbRouter key is Null! ");
        }
        dbKey = StringUtils.isNotBlank(dbKey) ? dbKey : dbRouterConfig.getRouterKey();
        // 路由属性
        String dbKeyAttr = getAttrValue(dbKey, jp.getArgs());
        // 路由策略
        dbRouterStrategy.doRouter(dbKeyAttr);
        try {
            return jp.proceed();
        } finally {
            dbRouterStrategy.clear();
        }
    }

    /**
     * 根据路由key，从入参对象中获取其值 比如key是uId, 则返回uId对应的值
     * @param attr
     * @param args
     * @return
     */
    public String getAttrValue(String attr, Object[] args) {
        if (1 == args.length) {
            Object arg = args[0];
            if (arg instanceof String) {
                return arg.toString();
            }
        }

        String filedValue = null;
        for (Object arg : args) {
            try {
                if (StringUtils.isNotBlank(filedValue)) {
                    break;
                }
                filedValue = BeanUtils.getProperty(arg, attr);
            } catch (Exception e) {
                log.error("获取路由属性失败 attr: {}", attr, e);
            }
        }
        return filedValue;
    }

    public Method getMethod(JoinPoint jp) throws NoSuchMethodException{
        Signature sig = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        return jp.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }
}
