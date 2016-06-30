package com.yuntongxun.kitsdk.ui.voip;

import java.io.Serializable;

/**
 * 用来保存姓名全拼的高亮信息
 * 
 * @author zhangyp
 */
public class NameHighLight implements Serializable {
	private static final long serialVersionUID = 1L;
	public int begin = -1;
	public int end = -1;

	public NameHighLight(int begin, int end) {
		this.begin = begin;
		this.end = end;
	}

	public NameHighLight() {
	}
}
