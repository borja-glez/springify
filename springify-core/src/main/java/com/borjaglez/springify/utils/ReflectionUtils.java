/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.borjaglez.springify.utils;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.borjaglez.springify.exception.ReflectionException;

import java.lang.reflect.Method;
import java.lang.reflect.Field;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 * @author Borja González Enríquez
 * @see <a href=
 *      "https://github.com/davinryan/common-rest-service">https://github.com/davinryan/common-rest-service</a>
 * @see <a href=
 *      "https://github.com/marcelormourao/spring-util-core">https://github.com/marcelormourao/spring-util-core</a>
 */
public class ReflectionUtils {

	private static final String SETTER_PREFIX = "set";

	private static final String GETTER_PREFIX = "get";

	private ReflectionUtils() {
	}

	/**
	 * <p>
	 * Invoke the getter method with the given {@code name} on the supplied target
	 * object with the supplied {@code value}.
	 * </p>
	 * <p>
	 * This method traverses the class hierarchy in search of the desired method. In
	 * addition, an attempt will be made to make non-{@code public} methods
	 * <em>accessible</em>, thus allowing one to invoke {@code protected},
	 * {@code private}, and <em>package-private</em> getter methods.
	 * </p>
	 * <p>
	 * In addition, this method supports JavaBean-style <em>property</em> names. For
	 * example, if you wish to get the {@code name} property on the target object,
	 * you may pass either &quot;name&quot; or &quot;getName&quot; as the method
	 * name.
	 * </p>
	 *
	 * @param target the target object on which to invoke the specified getter
	 *               method
	 * @param name   the name of the getter method to invoke or the corresponding
	 *               property name
	 * @return the value returned from the invocation
	 * @see org.springframework.util.ReflectionUtils#findMethod(Class, String,
	 *      Class[])
	 * @see org.springframework.util.ReflectionUtils#makeAccessible(Method)
	 * @see org.springframework.util.ReflectionUtils#invokeMethod(Method, Object,
	 *      Object[])
	 */
	public static Object invokeGetterMethod(Object target, String name) {
		Assert.notNull(target, "Target object must not be null");
		Assert.hasText(name, "Method name must not be empty");

		Method method = findGetterMethod(target, name);
		Assert.notNull(method, "Method object must not be null");

		org.springframework.util.ReflectionUtils.makeAccessible(method);
		return org.springframework.util.ReflectionUtils.invokeMethod(method, target);
	}

	/**
	 * Find a getter method for a given field.
	 *
	 * @param target the target object on which to invoke the specified setter
	 *               method
	 * @param name   the name of the setter method to invoke or the corresponding
	 *               property name
	 * @return getter method corresponding to name.
	 */
	public static Method findGetterMethod(Object target, String name) {
		String getterMethodName = name;
		if (!name.startsWith(GETTER_PREFIX)) {
			getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name);
		}
		return findMethod(target, name, getterMethodName);
	}

	/**
	 * Find a setter method for a given field.
	 *
	 * @param target the target object on which to invoke the specified setter
	 *               method
	 * @param name   the name of the setter method to invoke or the corresponding
	 *               property name
	 * @param type   the formal parameter type declared by the setter method
	 * @return setter method corresponding to name.
	 */
	public static Method findSetterMethod(Object target, String name, Class<?> type) {
		String setterMethodName = name;
		if (!name.startsWith(SETTER_PREFIX)) {
			setterMethodName = SETTER_PREFIX + StringUtils.capitalize(name);
		}
		Class<?>[] paramTypes = type != null ? new Class<?>[] { type } : null;

		return findMethod(target, name, setterMethodName, paramTypes);
	}

	/**
	 * <p>
	 * Invoke the setter method with the given {@code name} on the supplied target
	 * object with the supplied {@code value}.
	 * </p>
	 * <p>
	 * This method traverses the class hierarchy in search of the desired method. In
	 * addition, an attempt will be made to make non-{@code public} methods
	 * <em>accessible</em>, thus allowing one to invoke {@code protected},
	 * {@code private}, and <em>package-private</em> setter methods.
	 * </p>
	 * <p>
	 * In addition, this method supports JavaBean-style <em>property</em> names. For
	 * example, if you wish to set the {@code name} property on the target object,
	 * you may pass either &quot;name&quot; or &quot;setName&quot; as the method
	 * name.
	 * </p>
	 *
	 * @param target the target object on which to invoke the specified setter
	 *               method
	 * @param name   the name of the setter method to invoke or the corresponding
	 *               property name
	 * @param value  the value to provide to the setter method
	 * @see org.springframework.util.ReflectionUtils#findMethod(Class, String,
	 *      Class[])
	 * @see org.springframework.util.ReflectionUtils#makeAccessible(Method)
	 * @see org.springframework.util.ReflectionUtils#invokeMethod(Method, Object,
	 *      Object[])
	 */
	public static void invokeSetterMethod(Object target, String name, Object value) {
		invokeSetterMethod(target, name, value, null);
	}

	/**
	 * <p>
	 * Invoke the setter method with the given {@code name} on the supplied target
	 * object with the supplied {@code value}.
	 * </p>
	 * <p>
	 * This method traverses the class hierarchy in search of the desired method. In
	 * addition, an attempt will be made to make non-{@code public} methods
	 * <em>accessible</em>, thus allowing one to invoke {@code protected},
	 * {@code private}, and <em>package-private</em> setter methods.
	 * </p>
	 * <p>
	 * In addition, this method supports JavaBean-style <em>property</em> names. For
	 * example, if you wish to set the {@code name} property on the target object,
	 * you may pass either &quot;name&quot; or &quot;setName&quot; as the method
	 * name.
	 * </p>
	 *
	 * @param target the target object on which to invoke the specified setter
	 *               method
	 * @param name   the name of the setter method to invoke or the corresponding
	 *               property name
	 * @param value  the value to provide to the setter method
	 * @param type   the formal parameter type declared by the setter method
	 * @see org.springframework.util.ReflectionUtils#findMethod(Class, String,
	 *      Class[])
	 * @see org.springframework.util.ReflectionUtils#makeAccessible(Method)
	 * @see org.springframework.util.ReflectionUtils#invokeMethod(Method, Object,
	 *      Object[])
	 */
	public static void invokeSetterMethod(Object target, String name, Object value, Class<?> type) {
		Assert.notNull(target, "Target object must not be null");
		Assert.hasText(name, "Method name must not be empty");

		Method method = findSetterMethod(target, name, type);
		Assert.notNull(method, "Method object must not be null");

		org.springframework.util.ReflectionUtils.makeAccessible(method);
		org.springframework.util.ReflectionUtils.invokeMethod(method, target, value);
	}

	/**
	 * Find a method for a given field.
	 *
	 * @param target     the target object on which to invoke the specified setter
	 *                   method
	 * @param name       the corresponding property name
	 * @param methodName the name of the method to invoke
	 * @param paramTypes the formal parameter types declared by the method
	 * @return setter method corresponding to name.
	 */
	private static Method findMethod(Object target, String name, String methodName, Class<?>... paramTypes) {
		Method method = org.springframework.util.ReflectionUtils.findMethod(target.getClass(), methodName, paramTypes);
		if (method == null && !methodName.equals(name)) {
			return org.springframework.util.ReflectionUtils.findMethod(target.getClass(), name, paramTypes);
		}
		return method;
	}

	/**
	 * Find a method for a given field.
	 *
	 * @param target     the target object on which to invoke the specified setter
	 *                   method
	 * @param name       the corresponding property name
	 * @param methodName the name of the method to invoke
	 * @return setter method corresponding to name.
	 */
	private static Method findMethod(Object target, String name, String methodName) {
		Method method = org.springframework.util.ReflectionUtils.findMethod(target.getClass(), methodName);
		if (method == null && !methodName.equals(name)) {
			return org.springframework.util.ReflectionUtils.findMethod(target.getClass(), name);
		}
		return method;
	}

	/**
	 * Returns true if the @Id annotation is present on this element, else false.
	 * 
	 * @param field the field object to check @Id annotation
	 * @return true if the @Id annotation is present on this element, else false
	 */
	public static boolean isIdField(Field field) {
		return field.isAnnotationPresent(Id.class);
	}

	/**
	 * Returns true if the @Transient annotation is present on this element, else
	 * false.
	 * 
	 * @param field the field object to check @Transient annotation
	 * @return true if the @Transient annotation is present on this element, else
	 *         false
	 */
	public static boolean isTransient(Field field) {
		return field.isAnnotationPresent(Transient.class);
	}

	/**
	 * Returns true if the field is an {@link Iterable} class, else false.
	 * 
	 * @param field the field object to check an {@link Iterable} class
	 * @return true if the field is an {@link Iterable} class, else false
	 */
	public static boolean isSubclassItarable(Field field) {
		return Iterable.class.isAssignableFrom(field.getType());
	}

	/**
	 * Returns true if the field is "serialVersionUID", else false.
	 * 
	 * @param field the field object to check if is "serialVersionUID" field
	 * @return true if the field is "serialVersionUID", else false
	 */
	public static boolean isSerialVersionField(Field field) {
		return field.getName().equals("serialVersionUID");
	}

	/**
	 * Returns true if the @Entity annotation is present on this element, else
	 * false.
	 * 
	 * @param field the field object to check @Entity annotation
	 * @return true if the @Entity annotation is present on this element, else false
	 */
	public static boolean isEntity(Field field) {
		return field.getType().isAnnotationPresent(Entity.class);
	}

	/**
	 * Returns true if the @OneToOne, @OneToMany or @ManyToMany annotation is
	 * present on this element, else false.
	 * 
	 * @param field the field object to check @OneToOne, @OneToMany or @ManyToMany
	 *              annotation
	 * @return true if the @OneToOne, @OneToMany or @ManyToMany annotation is
	 *         present on this element, else false
	 */
	public static boolean isMappedByField(Field field) {
		return (field.getAnnotation(OneToOne.class) != null
				&& !"".equals(field.getAnnotation(OneToOne.class).mappedBy()))
				|| (field.getAnnotation(OneToMany.class) != null
						&& !"".equals(field.getAnnotation(OneToMany.class).mappedBy()))
				|| (field.getAnnotation(ManyToMany.class) != null
						&& !"".equals(field.getAnnotation(ManyToMany.class).mappedBy()));
	}

	/**
	 * Returns true if the field value is not null, else false.
	 * 
	 * @param field  the field object to check not null value
	 * @param entity the object parent
	 * @return true if the field value is not null, else false
	 */
	public static boolean isFieldNotNull(Field field, Object entity) {
		return getValueByField(field, entity) != null;
	}

	/**
	 * Returns true if the field value is null, else false.
	 * 
	 * @param field  the field object to check null value
	 * @param entity the object parent
	 * @return true if the field value is null, else false
	 */
	public static boolean isFieldNull(Field field, Object entity) {
		return getValueByField(field, entity) == null;
	}

	/**
	 * Returns the field's value of the entity.
	 * 
	 * @param field  the field object to get the value
	 * @param entity the object parent
	 * @return the field's value of the entity.
	 */
	public static Object getValueByField(Field field, Object entity) {
		try {
			field.setAccessible(true);
			return field.get(entity);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new ReflectionException(e.getMessage());
		}
	}
}
