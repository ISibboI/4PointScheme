package ui;

import geometry.Curve;
import geometry.Point;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JFrame;

public class CurveVisualizer {
	private static final int MARGIN = 50;
	
	private final DrawFrame _drawFrame;
	private final int _width;
	private final int _height;

	public CurveVisualizer() {
		_width = 700;
		_height = 700;
		_drawFrame = new DrawFrame("4 Point Scheme", _width, _height, 400, 100, 1);
		_drawFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void drawCurve(Curve curve, Color color) {
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;

		for (Point point : curve) {
			if (point.getX() < minX) {
				minX = point.getX();
			}

			if (point.getY() < minY) {
				minY = point.getY();
			}

			if (point.getX() > maxX) {
				maxX = point.getX();
			}

			if (point.getY() > maxY) {
				maxY = point.getY();
			}
		}

		double width = maxX - minX;
		double height = maxY - minY;
		double scaleX = (_width - 2 * MARGIN) / width;
		double scaleY = (_height - 2 * MARGIN) / height;
		double scale = Math.min(scaleX, scaleY);

		Graphics2D g = _drawFrame.startRender();
		g.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(color);

		g.translate(-minX * scale + MARGIN, -minY * scale + MARGIN);

		curve.draw(g, scale, scale);

		_drawFrame.switchToUIRender();
		_drawFrame.finishRender();
	}

	public void dispose() {
		_drawFrame.dispose();
	}

	public void drawCurves(Curve startingPoints, Curve result, Color color) {
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;

		for (Point point : result) {
			if (point.getX() < minX) {
				minX = point.getX();
			}

			if (point.getY() < minY) {
				minY = point.getY();
			}

			if (point.getX() > maxX) {
				maxX = point.getX();
			}

			if (point.getY() > maxY) {
				maxY = point.getY();
			}
		}

		double width = maxX - minX;
		double height = maxY - minY;
		double scaleX = (_width - 2 * MARGIN) / width;
		double scaleY = (_height - 2 * MARGIN) / height;
		double scale = Math.min(scaleX, scaleY);

		Graphics2D g = _drawFrame.startRender();
		g.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(color);

		g.translate(-minX * scale + MARGIN, -minY * scale + MARGIN);

		result.draw(g, scale, scale);
		
		g.setColor(Color.GRAY);
		startingPoints.draw(g, scale, scale);

		_drawFrame.switchToUIRender();
		_drawFrame.finishRender();
	}
}