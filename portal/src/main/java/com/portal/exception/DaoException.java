/**
 * DaoException.java
 * Created at 2014-04-20
 * Created by yuhaiyang
 * Copyright (C) 2014 SHANGHAI, All rights reserved.
 */

package com.portal.exception;

/**
 * <p>ClassName: DaoException</p>
 * <p>Description: 自定义非捕获异常，所有的DAO抛出的异常，都可以此类为基础</p>
 * <p>Author: 6910p</p>
 * <p>Date: 2014-4-20</p>
 */
public class DaoException extends RuntimeException {
    
    /** 版本号 **/
    private static final long serialVersionUID = -8690174831980453973L;

    /**
     * <p>Description: TODO</p>
     * @param msg 異常信息
     */
    public DaoException(String msg) {
        super(msg);
    }

    /**
     * <p>Description: TODO</p>
     * @param cause 异常类
     */
    public DaoException(Throwable cause) {
        super(cause);
    }

    
    /**
     * <p>Description: TODO</p>
     * @param msg 异常描述字符串
     * @param cause 异常类
     */
    public DaoException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
