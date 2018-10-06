package de.schuette.cobra2D.entity.editing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import de.schuette.cobra2D.math.Math2D;

public class PropertyEditorDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final JPanel contentPanel = new JPanel();
	protected boolean aborted = false;

	protected Action cancelAction = new AbstractAction("Cancel") {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			aborted = true;
			dispose();
		}

	};

	protected Action okAction = new AbstractAction("OK") {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			aborted = false;
			dispose();
		}

	};

	private PropertyEditor editorComponent;

	/**
	 * Create the dialog.
	 */
	public PropertyEditorDialog(String windowTitle,
			PropertyEditor editorComponent) {
		this.editorComponent = editorComponent;
		setTitle(windowTitle);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okayButton = new JButton(okAction);

				// configure the Action with the accelerator (aka: short cut)
				okAction.putValue(Action.ACCELERATOR_KEY,
						KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));

				InputMap inputMap = okayButton
						.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
				inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
						okAction);
				ActionMap actionMap = okayButton.getActionMap();
				actionMap.put(okAction, okAction);

				buttonPane.add(okayButton);
				getRootPane().setDefaultButton(okayButton);
			}
			{
				JButton cancelButton = new JButton(cancelAction);

				// configure the Action with the accelerator (aka: short cut)
				cancelAction.putValue(Action.ACCELERATOR_KEY,
						KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));

				InputMap inputMap = cancelButton
						.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
				inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
						cancelAction);
				ActionMap actionMap = cancelButton.getActionMap();
				actionMap.put(cancelAction, cancelAction);

				buttonPane.add(cancelButton);
			}
		}

		setEditorComponent(editorComponent);

		this.pack();
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {

			}

			@Override
			public void windowIconified(WindowEvent e) {

			}

			@Override
			public void windowDeiconified(WindowEvent e) {

			}

			@Override
			public void windowDeactivated(WindowEvent e) {

			}

			@Override
			public void windowClosing(WindowEvent e) {
				cancelAction.actionPerformed(null);
			}

			@Override
			public void windowClosed(WindowEvent e) {

			}

			@Override
			public void windowActivated(WindowEvent e) {

			}
		});

		Point centerOfScreen = Math2D
				.getCenterOfScreen(getWidth(), getHeight());
		this.setLocation(centerOfScreen);
		this.setVisible(true);

	}

	public void setEditorComponent(PropertyEditor editorComponent) {
		if (editorComponent != null) {
			contentPanel.removeAll();
			contentPanel.add(editorComponent);
			contentPanel.validate();
		} else {
			throw new IllegalArgumentException(
					"Editor component cannot be null.");
		}
	}

	public boolean isAborted() {
		return aborted;
	}
}
