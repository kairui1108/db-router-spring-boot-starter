package com.ruikai.middleware.db.router.annotation;

import java.lang.annotation.*;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/4/14 15:22
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DbRouter {

    /**
     * 用于分库分表字段
     * @return key
     */
    String key() default "";
}
