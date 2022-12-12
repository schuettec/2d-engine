package de.schuette.cobra2DSandbox.texture.editing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.RGBImageFilter;
import java.lang.reflect.Field;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.editing.PropertyEditor;
import de.schuette.cobra2D.math.Math2D;
import de.schuette.cobra2D.rendering.GradientTransparencyFilter;
import de.schuette.cobra2D.rendering.HSBTransparencyFilter;
import de.schuette.cobra2D.rendering.HueBrightnessTransparencyFilter;

public class TransparencyEditor extends PropertyEditor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JSpinner hsbMaxHue;
	private JSpinner hsbMinSat;
	private JSpinner hsbMinBright;
	private JSpinner hbMaxBrightness;
	private JButton hbHueSelect;
	private JSpinner hbHue;
	private JButton hsbHueMaxSelect;
	private JButton hsbHueSelect;
	private JButton btnEndcolor;
	private JPanel pnlEndcolor;
	private JButton btnStartColor;
	private JPanel pnlStartcolor;

	private JRadioButton rdbtnhueBrightnessTransparency;
	private JRadioButton rdbtnHSB;
	private JRadioButton rdbtnGradient;
	private JSpinner hsbHue;

	private RGBImageFilter value;

	private PreviewPanel pnlPreview;

	private ChangeListener changeListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			createFilter();
		}

	};
	private JRadioButton rdbtnUseNoFilter;

	public TransparencyEditor(final Field field, final Object object) {
		super(field, object);
		setMinimumSize(new Dimension(600, 700));
		setMaximumSize(new Dimension(600, 700));
		setPreferredSize(new Dimension(600, 704));
		setLayout(new BorderLayout(0, 0));

		JPanel pnlHeader = new JPanel();
		add(pnlHeader, BorderLayout.NORTH);
		pnlHeader.setLayout(new BorderLayout(0, 0));

		JLabel lblDescription = new JLabel(
				"<html>Select one of the available filters and adjust the values to your needs. The selected filter will be created and set to the entity.</html>");
		pnlHeader.add(lblDescription);

		Component horizontalStrut = Box.createHorizontalStrut(5);
		pnlHeader.add(horizontalStrut, BorderLayout.WEST);

		Component horizontalStrut_1 = Box.createHorizontalStrut(5);
		pnlHeader.add(horizontalStrut_1, BorderLayout.EAST);

		Component verticalStrut = Box.createVerticalStrut(5);
		pnlHeader.add(verticalStrut, BorderLayout.NORTH);

		Component verticalStrut_1 = Box.createVerticalStrut(5);
		pnlHeader.add(verticalStrut_1, BorderLayout.SOUTH);

		JPanel pnlSelection = new JPanel();
		add(pnlSelection);
		pnlSelection.setLayout(null);

		pnlPreview = new PreviewPanel();
		pnlPreview
				.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		pnlPreview.setBounds(6, 544, 588, 111);
		pnlSelection.add(pnlPreview);

		rdbtnGradient = new JRadioButton(
				"<html>Gradient Transparency Filter<br/>\r\nUse this filter to apply a gradient from a start to end color, where the end color is full transparent. The translucency of a pixel is determined by the amount of endcolor in its color.\r\n</html>");
		rdbtnGradient.addChangeListener(changeListener);
		rdbtnGradient.setBounds(6, 7, 588, 59);
		pnlSelection.add(rdbtnGradient);

		JLabel lblStartColor = new JLabel("Start color:");
		lblStartColor.setBounds(30, 93, 92, 22);
		pnlSelection.add(lblStartColor);

		pnlStartcolor = new JPanel();
		pnlStartcolor.setBackground(Color.MAGENTA);
		pnlStartcolor.setBounds(89, 90, 82, 25);
		pnlSelection.add(pnlStartcolor);

		JLabel lblEndColor = new JLabel("End color:");
		lblEndColor.setBounds(312, 93, 92, 22);
		pnlSelection.add(lblEndColor);

		pnlEndcolor = new JPanel();
		pnlEndcolor.setBackground(Color.BLACK);
		pnlEndcolor.setBounds(371, 90, 82, 25);
		pnlSelection.add(pnlEndcolor);

		btnStartColor = new JButton("Select color");
		btnStartColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(TransparencyEditor.this,
						"Select color", pnlStartcolor.getBackground());
				pnlStartcolor.setBackground(color);
				createFilter();

			}
		});
		btnStartColor.setBounds(181, 90, 101, 25);
		pnlSelection.add(btnStartColor);

		btnEndcolor = new JButton("Select color");
		btnEndcolor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(TransparencyEditor.this,
						"Select color", pnlStartcolor.getBackground());
				pnlEndcolor.setBackground(color);
				createFilter();
			}
		});
		btnEndcolor.setBounds(463, 90, 101, 25);
		pnlSelection.add(btnEndcolor);

		rdbtnHSB = new JRadioButton(
				"<html>HSB Transparency Filter<br/>\r\nUse this filter to define a color rande between 'Hue value' and 'Max Hue Value' that will be full transcluent in the result image. Additionally you can define a minimum value for saturation and brightness according to the HSB color model. Every pixel in the texture with a saturation or brightness lower than the minimum value will remain untouched. Every Pixel with a saturation or brightness higher than the minumum will get an alpha value that is calculated by the hue distance of the pixel color to the defined hue of this filter.   \r\n</html>\r\n");
		rdbtnHSB.setVerticalAlignment(SwingConstants.TOP);
		rdbtnHSB.addChangeListener(changeListener);
		rdbtnHSB.setBounds(6, 136, 588, 111);
		pnlSelection.add(rdbtnHSB);

		JLabel lblHsbHueValue = new JLabel("Hue value:");
		lblHsbHueValue.setBounds(30, 254, 92, 22);
		pnlSelection.add(lblHsbHueValue);

		SpinnerNumberModel model1 = new SpinnerNumberModel(300, 0, 360, 1);
		hsbHue = new JSpinner(model1);
		hsbHue.addChangeListener(changeListener);
		hsbHue.setBounds(124, 254, 66, 25);
		pnlSelection.add(hsbHue);

		hsbHueSelect = new JButton("Select");
		hsbHueSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				float hsbHueVal = ((Integer) hsbHue.getValue()) / 360.0f;
				int color = Color.HSBtoRGB(hsbHueVal, 1f, 1f);
				Color resultColor = JColorChooser.showDialog(
						TransparencyEditor.this, "Select color", new Color(
								color));
				float[] hsbvals = new float[3];
				Color.RGBtoHSB(resultColor.getRed(), resultColor.getGreen(),
						resultColor.getBlue(), hsbvals);
				int hsbResultHueVal = Math2D.saveRound(hsbvals[0] * 360.0f);
				hsbHue.setValue(hsbResultHueVal);
				createFilter();
			}
		});
		hsbHueSelect.setBounds(200, 254, 82, 23);
		pnlSelection.add(hsbHueSelect);

		JLabel lblMaxHsbHue = new JLabel("Max Hue distance:");
		lblMaxHsbHue.setBounds(292, 254, 111, 22);
		pnlSelection.add(lblMaxHsbHue);

		SpinnerNumberModel model2 = new SpinnerNumberModel(5, 0, 360, 1);
		hsbMaxHue = new JSpinner(model2);
		hsbMaxHue.addChangeListener(changeListener);
		hsbMaxHue.setBounds(398, 254, 74, 25);
		pnlSelection.add(hsbMaxHue);

		hsbHueMaxSelect = new JButton("Select");
		hsbHueMaxSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				float hsbHueVal = ((Integer) hsbMaxHue.getValue()) / 360.0f;
				int color = Color.HSBtoRGB(hsbHueVal, 1f, 1f);
				Color resultColor = JColorChooser.showDialog(
						TransparencyEditor.this, "Select color", new Color(
								color));
				float[] hsbvals = new float[3];
				Color.RGBtoHSB(resultColor.getRed(), resultColor.getGreen(),
						resultColor.getBlue(), hsbvals);
				int hsbResultHueVal = Math2D.saveRound(hsbvals[0] * 360.0f);
				hsbMaxHue.setValue(hsbResultHueVal);
				createFilter();
			}
		});
		hsbHueMaxSelect.setBounds(482, 254, 82, 23);
		pnlSelection.add(hsbHueMaxSelect);

		JLabel lblHsbMinSaturation = new JLabel("Min saturation value:");
		lblHsbMinSaturation.setBounds(30, 298, 172, 22);
		pnlSelection.add(lblHsbMinSaturation);

		SpinnerNumberModel model3 = new SpinnerNumberModel(40, 0, 360, 1);
		hsbMinSat = new JSpinner(model3);
		hsbMinSat.addChangeListener(changeListener);
		hsbMinSat.setBounds(200, 297, 82, 25);
		pnlSelection.add(hsbMinSat);

		JLabel lblHsbMinBrightness = new JLabel("Min brightness value:");
		lblHsbMinBrightness.setBounds(292, 298, 193, 22);
		pnlSelection.add(lblHsbMinBrightness);

		SpinnerNumberModel model4 = new SpinnerNumberModel(20, 0, 360, 1);
		hsbMinBright = new JSpinner(model4);
		hsbMinBright.addChangeListener(changeListener);
		hsbMinBright.setBounds(482, 297, 82, 25);
		pnlSelection.add(hsbMinBright);

		rdbtnhueBrightnessTransparency = new JRadioButton(
				"<html>Hue Brightness Transparency Filter<br/>\r\nUse this filter to define a color that will be transcluent in the result image. If the pixel color has a brightness value lower than the defined maximum bightness the alpha value is determined by the brightness relative to the maximum brightness.\r\n</html>\r\n");
		rdbtnhueBrightnessTransparency.addChangeListener(changeListener);
		rdbtnhueBrightnessTransparency.setVerticalAlignment(SwingConstants.TOP);
		rdbtnhueBrightnessTransparency.setBounds(6, 350, 588, 74);
		pnlSelection.add(rdbtnhueBrightnessTransparency);

		JLabel label = new JLabel("HSB Hue value:");
		label.setBounds(30, 431, 92, 22);
		pnlSelection.add(label);

		hbHueSelect = new JButton("Select");
		hbHueSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				float hsbHueVal = ((Integer) hbHue.getValue()) / 360.0f;
				int color = Color.HSBtoRGB(hsbHueVal, 1f, 1f);
				Color resultColor = JColorChooser.showDialog(
						TransparencyEditor.this, "Select color", new Color(
								color));
				float[] hsbvals = new float[3];
				Color.RGBtoHSB(resultColor.getRed(), resultColor.getGreen(),
						resultColor.getBlue(), hsbvals);
				int hsbResultHueVal = Math2D.saveRound(hsbvals[0] * 360.0f);
				hbHue.setValue(hsbResultHueVal);
				createFilter();
			}
		});
		hbHueSelect.setBounds(200, 431, 82, 23);
		pnlSelection.add(hbHueSelect);

		JLabel lblHsbMaxBrightness = new JLabel("HSB max brightness value:");
		lblHsbMaxBrightness.setBounds(292, 432, 193, 22);
		pnlSelection.add(lblHsbMaxBrightness);

		SpinnerNumberModel model5 = new SpinnerNumberModel(0.3f, 0.0f, 1.0f,
				0.1f);
		hbMaxBrightness = new JSpinner(model5);
		hbMaxBrightness.addChangeListener(changeListener);
		hbMaxBrightness.setBounds(482, 430, 82, 25);
		pnlSelection.add(hbMaxBrightness);

		SpinnerNumberModel model = new SpinnerNumberModel(300, 0, 360, 1);
		hbHue = new JSpinner(model);
		hbHue.addChangeListener(changeListener);
		hbHue.setBounds(124, 431, 66, 25);
		pnlSelection.add(hbHue);

		/*
		 * Create initial state
		 */
		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnGradient);
		group.add(rdbtnHSB);
		group.add(rdbtnhueBrightnessTransparency);
		rdbtnGradient.setSelected(true);

		rdbtnUseNoFilter = new JRadioButton(
				"<html>Remove Filter<br/>\r\nUse no filter on this entity.\r\n</html>");
		rdbtnUseNoFilter.setBounds(13, 489, 570, 48);
		pnlSelection.add(rdbtnUseNoFilter);
		createFilter();
	}

	private void createFilter() {
		Entity e = (Entity) object;

		if (rdbtnhueBrightnessTransparency.isSelected()) {
			this.value = new HueBrightnessTransparencyFilter(
					(Integer) hbHue.getValue(),
					((Double) hbMaxBrightness.getValue()).floatValue());
		} else if (rdbtnHSB.isSelected()) {
			this.value = new HSBTransparencyFilter((Integer) hsbHue.getValue(),
					(Integer) hsbMaxHue.getValue(),
					(Integer) hsbMinSat.getValue(),
					(Integer) hsbMinBright.getValue());
		} else if (rdbtnGradient.isSelected()) {
			this.value = new GradientTransparencyFilter(
					pnlStartcolor.getBackground(), pnlEndcolor.getBackground());
		} else if (rdbtnUseNoFilter.isSelected()) {
			this.value = null;
		}

		pnlPreview.setImage(e.getImage());
		pnlPreview.setFilter(this.value);
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) {
		this.value = (RGBImageFilter) value;
	}

	@Override
	public JComponent getDisplayComponent() {
		return new JLabel(getFilterName());
	}

	public String getFilterName() {
		String filterName = null;
		if (rdbtnhueBrightnessTransparency.isSelected()) {
			filterName = "Hue Brightness Filter";
		} else if (rdbtnHSB.isSelected()) {
			filterName = "HSB Filter";
		} else if (rdbtnGradient.isSelected()) {
			filterName = "RGB Gradient Filter";
		} else if (rdbtnUseNoFilter.isSelected()) {
			filterName = "No Filter";
		}

		return filterName;
	}
}
