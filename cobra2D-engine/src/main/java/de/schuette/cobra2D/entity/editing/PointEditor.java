package de.schuette.cobra2D.entity.editing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import de.schuette.cobra2D.math.Point;
import java.lang.reflect.Field;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class PointEditor extends PropertyEditor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String KEY = "de.schuette.cobra2D.entity.editing.PointEditor";

	private JLabel lblPropertyname;
	private JSpinner txtX;
	private JSpinner txtY;

	public PointEditor(Field field, Object object) {
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

		txtX = new JSpinner();
		SpinnerNumberModel model1 = new SpinnerNumberModel(0,
				Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
		txtX.setModel(model1);
		pnlXContainer.add(txtX, BorderLayout.WEST);

		Component horizontalStrut = Box.createHorizontalStrut(5);
		pnlXContainer.add(horizontalStrut, BorderLayout.CENTER);

		JLabel lblXText = new JLabel("x-Axis");
		pnlXContainer.add(lblXText, BorderLayout.EAST);

		JPanel pnlYContainer = new JPanel();
		pnlTwoIntContainer.add(pnlYContainer);
		pnlYContainer.setLayout(new BorderLayout(0, 0));

		txtY = new JSpinner();
		SpinnerNumberModel model2 = new SpinnerNumberModel(0,
				Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
		txtY.setModel(model2);
		pnlYContainer.add(txtY, BorderLayout.WEST);

		Component horizontalStrut_1 = Box.createHorizontalStrut(5);
		pnlYContainer.add(horizontalStrut_1, BorderLayout.CENTER);

		JLabel lblYText = new JLabel("y-Axis");
		pnlYContainer.add(lblYText, BorderLayout.EAST);
	}

	@Override
	public Object getValue() {

		try {
			int x = (Integer) txtX.getValue();
			int y = (Integer) txtY.getValue();
			return new Point(x, y);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void setValue(Object value) {
		Point p = (Point) value;
		txtX.setValue(p.x);
		txtY.setValue(p.y);
	}

	@Override
	public JComponent getDisplayComponent() {

		String label = null;

		try {
			Point value = (Point) EditableUtil.callGetMethod(field, object);
			label = value.x + ", " + value.y;
		} catch (Exception e) {
			label = "ERROR!";
		}

		return new JLabel(label);

	}

}
