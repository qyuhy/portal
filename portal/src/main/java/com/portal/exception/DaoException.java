package com.portal.exception;

public class DaoException extends RuntimeException {
	private static final long serialVersionUID = -8690174831980453973L;

	public DaoException(String msg) {
		super(msg);
	}

	public DaoException(Throwable cause) {
		super(cause);
	}

	public DaoException(String msg, Throwable cause) {
		super(msg, cause);
	}
}