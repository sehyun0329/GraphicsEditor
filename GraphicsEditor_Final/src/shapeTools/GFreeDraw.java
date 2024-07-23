package shapeTools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GFreeDraw extends GShape {
    private static final long serialVersionUID = 1L;
    private List<Point2D> points;

    public GFreeDraw() {
        super(EDrawingStyle.eNPStyle, new Path2D.Float());
        this.points = new ArrayList<>();
    }

    @Override
    public void setOrigin(int x, int y) {
        this.x1 = x;
        this.y1 = y;
        this.points.add(new Point2D(x, y));
    }

    @Override
    public void addPoint(int x, int y) {
        this.points.add(new Point2D(x, y));
        ((Path2D.Float) this.shape).moveTo(this.x1, this.y1);
        for (Point2D point : this.points) {
            ((Path2D.Float) this.shape).lineTo(point.x, point.y);
        }
    }

    @Override
    public void drag(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(getFillColor() != null ? getFillColor() : Color.BLACK);

        ((Path2D.Float) this.shape).moveTo(this.x1, this.y1);
        for (Point2D point : this.points) {
            ((Path2D.Float) this.shape).lineTo(point.x, point.y);
        }
        graphics2D.draw((Path2D.Float) this.shape);

        this.points.add(new Point2D(this.x2, this.y2));
        ((Path2D.Float) this.shape).lineTo(this.x2, this.y2);
    }
    
    @Override
    public GShape clone() {
        GFreeDraw freeDraw = new GFreeDraw();
        freeDraw.points = new ArrayList<>(this.points);
        return freeDraw;
    }

    private static class Point2D implements Serializable {
        private static final long serialVersionUID = 1L;
        int x;
        int y;

        Point2D(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}
