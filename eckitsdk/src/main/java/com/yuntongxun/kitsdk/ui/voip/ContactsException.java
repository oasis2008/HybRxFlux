package com.yuntongxun.kitsdk.ui.voip;

public class ContactsException extends Exception {

	/**
	 * 通讯录异常
	 */
	private static final long serialVersionUID = 8957778315193142579L;

	public ContactsException() {
		super();
	}

	public ContactsException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ContactsException(String detailMessage) {
		super(detailMessage);
	}

	public ContactsException(Throwable throwable) {
		super(throwable);
	}

}
