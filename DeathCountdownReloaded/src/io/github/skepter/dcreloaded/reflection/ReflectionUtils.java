/*******************************************************************************
 * Skepter's Licence
 * Copyright © 2015
 *
 * AllAssets, created by Skepter and MCSpartans
 *
 * You are able to:
 * * View AllAssets' source code on GitHub
 * * Experiment with the code as you wish
 * * Download the .jar files supplied on GitHub for your server
 *
 * You are NOT allowed to:
 * * Sell AllAssets - It is COMPLETELY free for ALL users
 * * Claim it as your own. AllAssets is created by Skepter and MCSpartans
 * * Distribute it on any other website
 * * Decompile the code - It's pointless, time consuming and the source code is already on GitHub
 * * Steal the code from GitHub. Just ask and we're more than likely to let you copy some of it
 *
 * You cannot:
 * * Hold us liable for your actions
 ******************************************************************************/
package io.github.skepter.dcreloaded.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtils {

	public static Object getPerfectField(final Object object, final Class<?> clazz, final String fieldName) {
		try {
			for (final Field f : clazz.getFields())
				if (f.getName().toLowerCase().contains(fieldName.toLowerCase()))
					return ReflectionUtils.getPrivateFieldValue(object, f);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void setPerfectField(final Object object, final Class<?> clazz, final String fieldName, final Object value) {
		try {
			for (final Field f : clazz.getFields())
				if (f.getName().toLowerCase().contains(fieldName.toLowerCase())) {
					f.setAccessible(true);
					f.set(object, value);
				}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/** Retrieves an Enumeration via Reflection */
	public static Object getEnum(final Class<?> enumClass, final String enumName) throws NullPointerException {
		for (final Object object : enumClass.getEnumConstants())
			if (object.toString().equals(enumName))
				return object;
		throw new NullPointerException();
	}

	/** Return the private field */
	public static Field getPrivateField(final Object object, final String fieldName) throws Exception {
		final Field field = object.getClass().getDeclaredField(fieldName);
		return field;
	}

	/** Return the value from a private field */
	public static Object getPrivateFieldValue(final Object object, final String fieldName) throws Exception {
		final Field field = object.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(object);
	}

	/** Return the value from a private field */
	public static Object getPrivateFieldValue(final Object object, final Field field) throws Exception {
		field.setAccessible(true);
		return field.get(object);
	}

	/** Return the value from a non private field */
	public static Object getFieldValue(final Object object, final String fieldName) throws Exception {
		return object.getClass().getDeclaredField(fieldName).get(object);
	}

	/** Sets the value of a private field */
	public static void setPrivateField(final Object object, final String fieldName, final Object data) throws Exception {
		final Field field = object.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(object, data);
	}

	/** Sets the value of a final static field */
	public static void setFinalStaticField(final Field field, final Object data) throws Exception {
		field.setAccessible(true);

		final Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

		field.set(null, data);
	}
	
	/** Sets the value of a final static field */
	public static void setFinalPrivateField(final Object object, final String fieldName, final Object data) throws Exception {
		Field field = getPrivateField(object, fieldName);
		field.setAccessible(true);

		final Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

		field.set(object, data);
	}

}
