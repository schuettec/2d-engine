package de.schuette.cobra2DSandbox.camera.widgets;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import de.schuette.cobra2D.system.Cobra2DEngine;

public class ButtonListController extends ButtonController implements
		ActionListener {

	protected LabeledButton previous;
	protected List<Button> allButtons = new ArrayList<Button>();
	protected LabeledButton next;

	protected int buttonWidth = 60;
	protected int buttonHeight = 60;

	protected int pageCount;
	protected int page;
	protected int buttonsPerPage;
	protected int visibleButtons;

	public ButtonListController(final Cobra2DEngine engine,
			final ButtonLayout layout) {
		this(engine, layout, 0);
	}

	public ButtonListController(final Cobra2DEngine engine,
			final ButtonLayout layout, final int spacing) {
		this(engine, layout, spacing, 5, 60, 60, Color.BLACK);
	}

	public ButtonListController(final Cobra2DEngine engine,
			final ButtonLayout layout, final int spacing,
			final int visibleButtons, final int buttonWidth,
			final int buttonHeight, final Color background) {
		super(engine, layout, spacing, background);
		this.previous = new LabeledButton(engine, "<<", 20, 60);
		this.next = new LabeledButton(engine, ">>", 20, 60);
		this.buttonWidth = buttonWidth;
		this.buttonHeight = buttonHeight;
		this.visibleButtons = visibleButtons;
		this.next.addActionListener(this);
		this.previous.addActionListener(this);

		this.updateList();
	}

	@Override
	public void addButton(final Button button) {
		this.allButtons.add(button);
		this.updateList();
	}

	@Override
	public void removeAll() {
		this.allButtons.clear();
		this.updateList();
	}

	@Override
	public void removeButton(final Button button) {
		this.allButtons.remove(button);
		this.updateList();
	}

	public void updateList() {

		this.buttonsPerPage = this.visibleButtons;
		this.pageCount = this.allButtons.size() / this.visibleButtons;

		this.buttons.clear();

		this.buttons.add(this.previous);

		int max = (this.page * this.buttonsPerPage) + this.buttonsPerPage;
		if (max > this.allButtons.size()) {
			max = this.allButtons.size();
		}

		for (int i = this.page * this.buttonsPerPage; i < max; i++) {
			this.buttons.add(this.allButtons.get(i));
		}
		this.buttons.add(this.next);

	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		final Button button = (Button) e.getSource();
		this.updateList();
		if (button == this.next) {
			if (this.page + 1 > this.pageCount) {
				return;
			} else {
				this.page++;

			}
		}

		if (button == this.previous) {
			if (this.page - 1 < 0) {
				return;
			} else {
				this.page--;
			}
		}

		this.updateList();

	}

}
