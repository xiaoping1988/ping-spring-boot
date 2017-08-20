package com.mybatis.ping.spring.boot.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Date;

public class ClassUtils {

	/**
	 * 从全路径class名称中获取class简称，并把首字母小写,
	 * 例如:com.springboot.ping.mybatis.utils.ClassUtils  返回结果是classUtils
	 * @param className  全路径class名称
	 * @return
     */
	public static String getLowerFirstLetterSimpleClassName(String className) {
		if (StringUtils.isBlank(className))
			return "";
		String[] parts = className.split("\\.");
		String result = parts[parts.length - 1];
		return result.substring(0, 1).toLowerCase()
				+ (result.length() > 1 ? result.substring(1) : "");
	}

//	/**
//	 * 1.获取属性名称的简单名，如果属性名称是entity.property，那么将返回property 2.如何输入的名称有 [ 标识
//	 * 说明是查询字段，原样返回即可 ，不做任何处理
//	 *
//	 * @param fullName
//	 *            属性名称
//	 * @return 处理后的属性名称
//	 */
//	public static String getSimplePropertyName(String fullName) {
//
//		if (!StringUtils.isBlank(fullName) && fullName.indexOf("[") != -1) {
//			// 如果字段含有 [ 说明是查询字段，直接返回
//			return fullName;
//		}
//
//		if (!StringUtils.isBlank(fullName) && fullName.indexOf(".") != -1) {// 检查要获取的属性名是否含有.
//			// 检查.是否是最后一个字符
//			if (fullName.indexOf(".") != fullName.length()) {
//				fullName = fullName.substring(fullName.indexOf(".") + 1);
//			}
//		}
//
//		return fullName;
//	}
//
//	/**
//	 * 根据字符串转化成ID 1.如果属性名称是entity.property，那么将返回property 2.如何输入的名称有 [ 标识 说明是查询字段
//	 * 如 ：conditions['date_date_gt'].value 用 split("'") 截断 返回第二个
//	 *
//	 * @param fullName
//	 *            属性名称
//	 * @return 处理后的属性名称
//	 */
//	public static String changeToId(String fullName) {
//
//		if (!StringUtils.isBlank(fullName) && fullName.indexOf("[") != -1) {
//			// 如果字段含有 [ 说明是查询字段，直接返回
//			String[] ids = fullName.split("'");
//			if (ids.length == 3)
//				return ids[1];
//			return fullName;
//		}
//
//		if (!StringUtils.isBlank(fullName) && fullName.indexOf(".") != -1) {// 检查要获取的属性名是否含有.
//			// 检查.是否是最后一个字符
//			if (fullName.indexOf(".") != fullName.length()) {
//				fullName = fullName.substring(fullName.indexOf(".") + 1);
//			}
//		}
//
//		return fullName;
//	}
	/**
	 * 获取 get 方法对应的属性名
	 * 
	 * @param readMethod
	 * @return
	 */
	public static String getPropertyName(Method readMethod) {
		String methodName = readMethod.getName();
		int getPosition = methodName.indexOf("get");
		if (getPosition == -1) {
			throw new RuntimeException(methodName + " 不是 以get开关的方法");
		}
		return getLowerFirstLetterSimpleClassName(methodName
				.substring(getPosition + 3));
	}



	/**
	 * 克隆一个对象
	 * <br>
	 * 该对象必须实现了Serializable接口,<br/>
	 * 使用序列化和反序列化形式进行克隆
	 * </p>
	 * 
	 * @param entity
	 *            要克隆的对象
	 * @return 克隆结果
	 */
	@SuppressWarnings("unchecked")
	public static <T> T cloneObject(T entity) {
		T result = null;
		if (entity == null) {
			return result;
		}
		// 检查克隆实体是否实现了Serializable接口
		if (!Serializable.class.isAssignableFrom(entity.getClass())) {
			throw new IllegalArgumentException("需要克隆的实体必须实现Serializable接口");
		}
		ByteArrayOutputStream t = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		ByteArrayInputStream t2 = null;
		try {
			t = new ByteArrayOutputStream();
			out = new ObjectOutputStream(t);
			out.writeObject(entity);
			out.flush();
			t2 = new ByteArrayInputStream(t.toByteArray());
			in = new ObjectInputStream(t2);
			result = (T) in.readObject();
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (t2 != null) {
				try {
					t2.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
			if (t != null) {
				try {
					t.close();
				} catch (IOException e) {
				}
			}
		}
		return result;
	}

	public static boolean isCustomType(Class<?> clazz) {
		if (Number.class.isAssignableFrom(clazz)
				|| String.class.isAssignableFrom(clazz)
				|| Date.class.isAssignableFrom(clazz)) {
			return false;
		} else {
			return true;
		}
	}

}
