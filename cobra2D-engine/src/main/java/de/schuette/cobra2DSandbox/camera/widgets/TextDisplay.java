package de.schuette.cobra2DSandbox.camera.widgets;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import de.schuette.cobra2D.math.Point;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Scanner;

public class TextDisplay implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Point position;

	public TextDisplay(final Point position) {
		this.position = position;
	}

	public void drawTextdisplay(final String text, final Graphics2D g) {
		final Scanner scan = new Scanner(new ByteArrayInputStream(
				text.getBytes()));

		g.setColor(Color.CYAN);
		Font font = g.getFont();
		g.setFont(new Font("Courier", Font.PLAIN, 10));

		int y = this.position.getRoundY();
		int x = this.position.getRoundX();

		while (scan.hasNextLine()) {
			final String line = scan.nextLine();
			for (int i = 0; i < line.length(); i++) {
				final String character = String.valueOf(line.charAt(i));
				if (character.equals("\t")) {
					x += 20;
				}
				g.drawString(character, x, y);
				x += g.getFontMetrics().stringWidth(character);
			}
			x = this.position.getRoundX();
			y += g.getFontMetrics().getHeight();
		}
		g.setFont(font);
	}
}
