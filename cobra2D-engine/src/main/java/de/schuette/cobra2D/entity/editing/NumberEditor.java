package de.schuette.cobra2D.entity.editing;

import java.awt.BorderLayout;
import java.lang.reflect.Field;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class NumberEditor extends PropertyEditor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String KEY = "de.schuette.cobra2D.entity.editing.NumberEditor";

	private JLabel lblPropertyname;
	private JSpinner spinner;
	private SpinnerNumberModel model;

	public NumberEditor(Field field, Object object) {
		super(field, object);

		setLayout(new BorderLayout(0, 0));

		lblPropertyname = new JLabel("Value for " + getFieldName() + ": ");
		add(lblPropertyname, BorderLayout.WEST);

		this.model = new SpinnerNumberModel(0, Integer.MIN_VALUE,
				Integer.MAX_VALUE, 1);
		spinner = new JSpinner(model);
		add(spinner, BorderLayout.CENTER);
	}

	protected void setSpinnerNumberModel(SpinnerNumberModel model) {
		this.model = model;
		this.spinner.setModel(model);
		this.spinner.repaint();
	}

	@Override
	public Object getValue() {
		return spinner.getValue();
	}

	@Override
	public void setValue(Object value) {
		spinner.setValue(value);
	}

	@Override
	public JComponent getDisplayComponent() {
		String label = null;

		try {
			label = String.valueOf(EditableUtil.callGetMethod(field, object));
		} catch (Exception e) {
			label = "ERROR!";
		}

		return new JLabel(label);
	}

}
