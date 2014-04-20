/**
 * Log.java
 * Created at 2014-04-20
 * Created by yuhaiyang
 * Copyright (C) 2014 SHANGHAI, All rights reserved.
 */
package com.portal.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>ClassName: Log</p>
 * <p>Description: 日志接口类</p>
 * <p>Author: yuhaiyang</p>
 * <p>Date: 2014-4-20</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    
    /**
     * <p>Description: TODO</p>
     * @return
     */
    String value();
}
