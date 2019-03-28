package com.bgd.support.base;

/**
 * 
 * @ClassName  : DictionaryItem
 * @Description: 数据字典项
 */
public interface DictionaryItem<T> {
	String toCode();
	String toName();
	String getEnumName();

	String getEnumCode();

	T parseByCode(String code);

	T parseByName(String name);
}
