package com.ruikai.middleware.db.router.annotation;

import java.lang.annotation.*;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/4/14 15:24
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DbRouterStrategy {

    boolean splitTable() default false;

}
