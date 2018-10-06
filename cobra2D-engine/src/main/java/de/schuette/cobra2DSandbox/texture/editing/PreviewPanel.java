package de.schuette.cobra2DSandbox.texture.editing;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.RGBImageFilter;
import java.awt.image.VolatileImage;

import javax.swing.JPanel;

import de.schuette.cobra2D.math.Math2D;
import de.schuette.cobra2D.rendering.RenderToolkit;

public class PreviewPanel extends JPanel implements MouseMotionListener,
		MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int SQUARE_WIDTH = 5;

	protected int yOffset;
	protected int xOffset;
	protected Point previewPoint = new Point(0, 0);
	protected RGBImageFilter filter;
	protected VolatileImage image;

	public PreviewPanel() {
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
	}

	public void setFilter(RGBImageFilter filter) {
		this.filter = filter;
		repaint();
	}

	public void setImage(VolatileImage image) {
		this.image = image;
		repaint();
	}

	protected void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);

		boolean bright = true;
		boolean startLineBright = true;
		for (int y = 0; y < this.getSize().height; y += SQUARE_WIDTH) {
			bright = !startLineBright;
			startLineBright = !startLineBright;

			for (int x = 0; x < this.getSize().width; x += SQUARE_WIDTH) {

				if (bright) {
					g.setColor(Color.LIGHT_GRAY);
				} else {
					g.setColor(Color.GRAY);
				}

				g.fillRect(x, y, SQUARE_WIDTH, SQUARE_WIDTH);
				bright = !bright;
			}
		}

		if (image == null) {
			g.setColor(Color.CYAN);
			String message = "No Texture set!";
			FontMetrics fontMetrics = g.getFontMetrics();
			int halfStrWidth = Math2D.saveRound(fontMetrics
					.stringWidth(message) / 2.0);
			g.drawString(message,
					Math2D.saveRound(getSize().width / 2.0 - halfStrWidth),
					Math2D.saveRound(getSize().height / 2.0));
		} else {
			VolatileImage converted;
			if (filter != null) {
				converted = RenderToolkit.convertSpriteToTransparentSprite(
						image, filter);
			} else {
				converted = image;
			}
			RenderToolkit.renderTo(previewPoint, (Graphics2D) g, converted);
		}

	};

	@Override
	public void mouseDragged(MouseEvent e) {
		previewPoint.x = e.getPoint().x - xOffset;
		previewPoint.y = e.getPoint().y - yOffset;
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		xOffset = e.getPoint().x - previewPoint.x;
		yOffset = e.getPoint().y - previewPoint.y;
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	public Point getRenderPoint() {
		return previewPoint;
	}

	public void setRenderPoint(Point previewPoint) {
		this.previewPoint = previewPoint;
	}

}
