package de.schuette.cobra2D.entity.editing;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import de.schuette.cobra2D.entity.Entity;

public class EditableUtil {

	public static boolean isEntityEditable(Entity entity) {
		return (entity.getClass().isAnnotationPresent(Editable.class));
	}

	public static HashMap<Class<?>, Field[]> getEditableFields(Class<?> clazz) {

		HashMap<Class<?>, Field[]> fieldSet = new HashMap<Class<?>, Field[]>();
		getEditableFieldsIntern(fieldSet, clazz);
		return fieldSet;
	}

	protected static void getEditableFieldsIntern(
			HashMap<Class<?>, Field[]> toAdd, Class<?> clazz) {
		if (clazz.equals(Object.class))
			return;
		ArrayList<Field> fields = new ArrayList<Field>();
		Field[] allFields = clazz.getDeclaredFields();
		for (Field field : allFields) {
			if (field.isAnnotationPresent(EditableProperty.class)) {
				fields.add(field);
			}
		}

		toAdd.put(clazz, fields.toArray(new Field[] {}));

		getEditableFieldsIntern(toAdd, clazz.getSuperclass());
	}

	@SuppressWarnings("unchecked")
	public static Class<? extends PropertyEditor> getEditorClass(
			String modifierClass) throws ClassNotFoundException {
		Class<?> clazz = Class.forName(modifierClass);
		if (PropertyEditor.class.isAssignableFrom(clazz)) {
			return (Class<? extends PropertyEditor>) clazz;
		} else {
			return null;
		}
	}

	public static PropertyEditor getEditor(Field field, Object object) {
		EditableProperty annotation = field
				.getAnnotation(EditableProperty.class);
		if (annotation != null) {
			String modifierClass = annotation.modifierClass();

			if (modifierClass == null || modifierClass.trim().length() == 0)
				throw new RuntimeException(
						"Entity has declared an editable field Name='"
								+ field.getName() + "' in "
								+ field.getDeclaringClass().getCanonicalName()
								+ ", but invalid modifierClass: '"
								+ modifierClass + "'");

			try {
				Class<? extends PropertyEditor> clazz = getEditorClass(modifierClass);

				Constructor<?> constructor = clazz.getConstructor(Field.class,
						Object.class);
				Object instance = constructor.newInstance(field, object);
				PropertyEditor editor = (PropertyEditor) instance;
				Object value = callGetMethod(field, object);
				editor.setValue(value);
				return editor;
			} catch (Exception e) {
				throw new RuntimeException(
						"Entity has declared an editable field Name='"
								+ field.getName() + "' in "
								+ field.getDeclaringClass().getCanonicalName()
								+ ", but modifierClass: '" + modifierClass
								+ "' cannot be accessed.", e);
			}

		} else {
			return null;
		}
	}

	/**
	 * Shows the property editor for the given object and a specified field
	 * within this object that must be declared with annotation
	 * EditableProperty. This method shows a dialog with the property editor
	 * specified by the annotation and asks the user to modify the current
	 * value. If the user accepts any new value or modification in the dialog,
	 * the value will be injected into the field in the object. In this case
	 * true is returned indicating, that the user has successfully changed the
	 * value. If the user cancels the action, nothin happens, the old value
	 * remains and false is returned.
	 * 
	 * @param field
	 *            The field within the object that should be modified. This
	 *            field must be annotated with @EditableProperty
	 * @param object
	 *            The object wich is to modify.
	 * @return Returns true if the user accepts the modification, or false if
	 *         the user chose cancel action.
	 * @throws Exception
	 *             Is thrown whenever an error occurs.
	 */
	public static boolean showEditorForField(Field field, Object object)
			throws Exception {

		PropertyEditor editor = getEditor(field, object);

		return showEditor(editor, object);

	}

	public static boolean showEditor(PropertyEditor editor, Object object)
			throws Exception {

		// Setting the current value
		Object value = callGetMethod(editor.getField(), object);
		editor.setValue(value);

		PropertyEditorDialog dialog = new PropertyEditorDialog(
				"Editing value '" + editor.getFieldName() + "'", editor);

		if (dialog.isAborted()) {
			return false;
		} else {
			/*
			 * Set new value:
			 */
			value = editor.getValue();
			callSetMethod(value, editor.getField(), object);
			return true;
		}
	}

	protected static Object callGetMethod(Field field, Object object)
			throws Exception {
		String fieldName = field.getName();

		boolean isbool = field.getType().equals(boolean.class);
		String praefix = (isbool) ? "is" : "get";

		String expectedMethodName = praefix
				+ fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1, fieldName.length());
		Class<?> clazz = object.getClass();
		Method method = clazz.getMethod(expectedMethodName);
		Object value = method.invoke(object);

		return value;
	}

	protected static void callSetMethod(Object value, Field field, Object object)
			throws Exception {
		Class<?> fieldType = field.getType();
		String fieldName = field.getName();
		String expectedMethodName = "set"
				+ fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1, fieldName.length());
		Class<?> clazz = object.getClass();
		Method method = clazz.getMethod(expectedMethodName, fieldType);
		method.invoke(object, value);
	}
}
