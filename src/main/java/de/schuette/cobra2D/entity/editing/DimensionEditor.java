package de.schuette.cobra2D.entity.editing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.lang.reflect.Field;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class DimensionEditor extends PropertyEditor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String KEY = "de.schuette.cobra2D.entity.editing.DimensionEditor";

	private JLabel lblPropertyname;
	private JSpinner txtWidth;
	private JSpinner txtHeight;

	public DimensionEditor(Field field, Object object) {
		super(field, object);

		setLayout(new BorderLayout(0, 0));

		lblPropertyname = new JLabel("Value for " + getFieldName() + ": ");
		add(lblPropertyname, BorderLayout.WEST);

		JPanel pnlTwoIntContainer = new JPanel();
		add(pnlTwoIntContainer, BorderLayout.CENTER);
		pnlTwoIntContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JPanel pnlXContainer = new JPanel();
		pnlTwoIntContainer.add(pnlXContainer);
		pnlXContainer.setLayout(new BorderLayout(0, 0));

		txtWidth = new JSpinner();
		SpinnerNumberModel model1 = new SpinnerNumberModel(0, 0,
				Integer.MAX_VALUE, 1);
		txtWidth.setModel(model1);
		pnlXContainer.add(txtWidth, BorderLayout.WEST);

		Component horizontalStrut = Box.createHorizontalStrut(5);
		pnlXContainer.add(horizontalStrut, BorderLayout.CENTER);

		JLabel lblXText = new JLabel("width");
		pnlXContainer.add(lblXText, BorderLayout.EAST);

		JPanel pnlYContainer = new JPanel();
		pnlTwoIntContainer.add(pnlYContainer);
		pnlYContainer.setLayout(new BorderLayout(0, 0));

		txtHeight = new JSpinner();
		SpinnerNumberModel model2 = new SpinnerNumberModel(0, 0,
				Integer.MAX_VALUE, 1);
		txtHeight.setModel(model2);
		pnlYContainer.add(txtHeight, BorderLayout.WEST);

		Component horizontalStrut_1 = Box.createHorizontalStrut(5);
		pnlYContainer.add(horizontalStrut_1, BorderLayout.CENTER);

		JLabel lblYText = new JLabel("height");
		pnlYContainer.add(lblYText, BorderLayout.EAST);
	}

	@Override
	public Object getValue() {

		try {
			int w = (Integer) txtWidth.getValue();
			int h = (Integer) txtHeight.getValue();
			return new Dimension(w, h);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void setValue(Object value) {
		Dimension dim = (Dimension) value;
		txtWidth.setValue(dim.width);
		txtHeight.setValue(dim.height);
	}

	@Override
	public JComponent getDisplayComponent() {
		String label = null;

		try {
			Dimension value = (Dimension) EditableUtil.callGetMethod(field,
					object);
			label = value.width + ", " + value.height;
		} catch (Exception e) {
			label = "ERROR!";
		}

		return new JLabel(label);
	}
}
