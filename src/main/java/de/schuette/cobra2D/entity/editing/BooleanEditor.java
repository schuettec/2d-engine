package de.schuette.cobra2D.entity.editing;

import java.awt.BorderLayout;
import java.lang.reflect.Field;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class BooleanEditor extends PropertyEditor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String KEY = "de.schuette.cobra2D.entity.editing.BooleanEditor";

	private JLabel lblPropertyname;
	private JCheckBox checkBox;

	public BooleanEditor(Field field, Object object) throws Exception {
		super(field, object);

		setLayout(new BorderLayout(0, 0));

		lblPropertyname = new JLabel("Value for " + getFieldName() + ": ");
		add(lblPropertyname, BorderLayout.WEST);
		checkBox = new JCheckBox("");
		add(checkBox, BorderLayout.CENTER);

		Object value = EditableUtil.callGetMethod(field, object);
		setValue(value);

	}

	@Override
	public Object getValue() {
		return (boolean) checkBox.isSelected();
	}

	@Override
	public void setValue(Object value) {
		checkBox.setSelected(Boolean.valueOf((Boolean) value));

	}

	@Override
	public JComponent getDisplayComponent() {

		String label = null;

		try {
			label = String.valueOf((Boolean) EditableUtil.callGetMethod(field,
					object));
		} catch (Exception e) {
			label = "ERROR!";
		}

		return new JLabel(label);
	}

}
