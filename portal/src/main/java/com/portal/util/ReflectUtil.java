package com.portal.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectUtil {
	/**
	 * 利用反射获取指定对象的指定属性
	 * 
	 * @param obj
	 *            目标对象
	 * @param fieldName
	 *            目标属性
	 * @return 目标属性的值
	 */
	public static Object getFieldValue(Object obj, String fieldName) {
		Object result = null;
		Field field = ReflectUtil.getField(obj, fieldName);
		if (field != null) {
			field.setAccessible(true);
			try {
				result = field.get(obj);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 利用反射获取指定对象里面的指定属性
	 * 
	 * @param obj
	 *            目标对象
	 * @param fieldName
	 *            目标属性
	 * @return 目标字段
	 */
	private static Field getField(Object obj, String fieldName) {
		Field field = null;
		for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz
				.getSuperclass()) {
			try {
				field = clazz.getDeclaredField(fieldName);
				break;
			} catch (NoSuchFieldException e) {
				// 这里不用做处理，子类没有该字段可能对应的父类有，都没有就返回null。
			}
		}
		return field;
	}

	/**
	 * 利用反射设置指定对象的指定属性为指定的值
	 * 
	 * @param obj
	 *            目标对象
	 * @param fieldName
	 *            目标属性
	 * @param fieldValue
	 *            目标值
	 */
	public static void setFieldValue(Object obj, String fieldName,
			String fieldValue) {
		Field field = ReflectUtil.getField(obj, fieldName);
		if (field != null) {
			try {
				field.setAccessible(true);
				field.set(obj, fieldValue);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Class getSuperClassParameterizedType(Object obj){
		return getSuperClassParameterizedType(obj,0);
	}
	
	
	@SuppressWarnings("unchecked")
	public static Class getSuperClassParameterizedType(Object obj,int index){
		Type type = obj.getClass().getGenericSuperclass();
		if(type instanceof ParameterizedType){
			ParameterizedType p = (ParameterizedType)type;
			Type types[] = p.getActualTypeArguments();
			if(index<0 || index>=types.length){
				throw new RuntimeException(obj.getClass().getName()+"的父类的参数化类型下标在0-"+(types.length-1)+"范围内");
			}
			return (Class)types[index];
			
		}
		return null;
	}
	

}
