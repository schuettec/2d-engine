package de.schuette.cobra2D.entity.editing;

import java.lang.reflect.Field;

import javax.swing.JComponent;
import javax.swing.JPanel;

public abstract class PropertyEditor extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Field field;
	protected Object object;

	public PropertyEditor(Field field, Object object) {
		this.field = field;
		this.object = object;
	}

	public Class<?> getInheritedFrom() {
		return field.getDeclaringClass();
	}

	/**
	 * Returns the field name of the editing property.
	 * 
	 * @return The name of the field.
	 */
	public String getFieldName() {
		return field.getName();
	}

	/**
	 * @return Returns the field representation this editor is working with.
	 */
	public Field getField() {
		return field;
	}

	/**
	 * @return Returns the value created by this editor.
	 */
	public abstract Object getValue();

	/**
	 * @param value
	 *            Sets the value that exists before editing.
	 */
	public abstract void setValue(Object value);

	/**
	 * Returns a small display component to only display the current value.
	 * 
	 * @return Returns the component, set up to display the current value.
	 */
	public abstract JComponent getDisplayComponent();

	/**
	 * Shows the property editor for the object and field within this object,
	 * this editor is bound to. This method shows a dialog with the property
	 * editor specified by the annotation and asks the user to modify the
	 * current value. If the user accepts any new value or modification in the
	 * dialog, the value will be injected into the field in the object. In this
	 * case true is returned indicating, that the user has successfully changed
	 * the value. If the user cancels the action, nothing happens, the old value
	 * remains and false is returned.
	 * 
	 */
	public boolean showEditor() throws Exception {
		return EditableUtil.showEditorForField(field, object);
	}

	/**
	 * Calls a get method of a field on the given object.
	 * 
	 * @param field
	 *            The field within the object.
	 * @param object
	 *            The object.
	 * @return Returns the current value for the field in this object.
	 * @throws Exception
	 */
	protected static Object callGetMethod(Field field, Object object)
			throws Exception {

		return EditableUtil.callGetMethod(field, object);
	}

	/**
	 * Calls a set method of a field on the given object.
	 * 
	 * @param value
	 *            The value to set for the field as object.
	 * @param field
	 *            The field within the object.
	 * @param object
	 *            The object.
	 * @throws Exception
	 */
	protected static void callSetMethod(Object value, Field field, Object object)
			throws Exception {
		EditableUtil.callSetMethod(value, field, object);
	}

}
