package de.schuette.cobra2D.rendering;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import de.schuette.cobra2D.benchmark.Benchmarker;
import de.schuette.cobra2D.system.Cobra2DEngine;

public interface Renderer {
	public void render();

	public void initializeRenderer(final Cobra2DEngine engine, final int resolutionX, final int resolutionY,
			final int bitDepth, final int refreshRate, final boolean fullscreen) throws RendererException;

	public FontMetrics getFontMetrics(Font font);

	public Font getFont();

	public boolean isCursorVisible();

	public void addMouseListener(MouseListener listener);

	public void addMouseMotionListener(MouseMotionListener listener);

	public void removeMouseListener(MouseListener listener);

	public void removeMouseMotionListener(MouseMotionListener listener);

	public void addKeyListener(KeyListener listener);

	public void removeKeyListener(KeyListener listener);

	public Benchmarker getBenchmarker();

	public void finish();

	public Dimension getWorldViewSize();

	public int getResolutionX();

	public int getResolutionY();

	public boolean isDrawEntityLines();

	public boolean isDrawEntities();

	public boolean isDrawEntityCenterPoint();

	public boolean isDrawEntityPoints();

	public java.awt.Point getLocation();

	public void setDrawEntityLines(boolean b);

	public void setDrawEntityPoints(boolean b);

	public void setDrawEntityCenterPoint(boolean b);

}
