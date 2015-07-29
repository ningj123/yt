package com.yt.error;

public enum StaticErrorEnum {
	FETCH_DB_DATA_FAIL(3001, "获取数据库数据发生错误。"), THE_DATA_NOT_EXIST(3002,
			"指定的数据不存在。"), THE_INPUT_IS_NULL(3003, "输入的数据对象为空。");

	public int errorCode;
	public String errorText;

	private StaticErrorEnum(int errorCode, String errorText) {
		this.errorCode = errorCode;
		this.errorText = errorText;
	}
}
