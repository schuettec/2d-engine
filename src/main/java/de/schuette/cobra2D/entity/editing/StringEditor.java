package de.schuette.cobra2D.entity.editing;

import java.awt.BorderLayout;
import java.lang.reflect.Field;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class StringEditor extends PropertyEditor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String KEY = "de.schuette.cobra2D.entity.editing.StringEditor";

	private JTextField txtValue;
	private JLabel lblPropertyname;

	public StringEditor(Field field, Object object) throws Exception {
		super(field, object);

		setLayout(new BorderLayout(0, 0));

		lblPropertyname = new JLabel("Value for " + getFieldName() + ": ");
		add(lblPropertyname, BorderLayout.WEST);

		txtValue = new JTextField();
		Object value = EditableUtil.callGetMethod(field, object);
		setValue(value);
		add(txtValue);
		txtValue.setColumns(10);
	}

	@Override
	public Object getValue() {
		return txtValue.getText();
	}

	@Override
	public void setValue(Object value) {
		txtValue.setText((String) value);
	}

	@Override
	public JComponent getDisplayComponent() {

		String label = null;

		try {
			label = (String) EditableUtil.callGetMethod(field, object);
		} catch (Exception e) {
			label = "ERROR!";
		}

		return new JLabel(label);
	}

}
