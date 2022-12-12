package de.schuette.cobra2D.entity.editing;

import java.lang.reflect.Field;

import javax.swing.SpinnerNumberModel;

public class DegreesEditor extends NumberEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String KEY = "de.schuette.cobra2D.entity.editing.DegreesEditor";

	public DegreesEditor(Field field, Object object) {
		super(field, object);
		this.setSpinnerNumberModel(new SpinnerNumberModel(0.0, 0.0, 360.0, 1.0));
	}
}
