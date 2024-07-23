package shapeTools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public class GStar extends GShape {
    private static final long serialVersionUID = 1L;

    public GStar() {
        super(EDrawingStyle.e2PStyle, new Path2D.Float());
    }

    @Override
    public void setOrigin(int x, int y) {
        this.x1 = x;
        this.y1 = y;
        this.x2 = x;
        this.y2 = y;
    }

    @Override
    public void addPoint(int x, int y) {
        this.x2 = x;
        this.y2 = y;
    }

    @Override
    public void drag(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setXORMode(graphics2D.getBackground());

        Path2D.Float starShape = new Path2D.Float();
        double centerX = (this.x1 + this.x2) / 2.0;
        double centerY = (this.y1 + this.y2) / 2.0;
        double outerRadius = Math.min(Math.abs(this.x2 - this.x1), Math.abs(this.y2 - this.y1)) / 2.0;
        double innerRadius = outerRadius / 2.5;

        starShape.moveTo(centerX, centerY - outerRadius);
        for (int i = 1; i < 10; i++) {
            double angle = Math.PI * i / 5;
            double radius = (i % 2 == 0) ? outerRadius : innerRadius;
            starShape.lineTo(
                    centerX + radius * Math.sin(angle),
                    centerY - radius * Math.cos(angle)
            );
        }
        starShape.closePath();

        this.shape = starShape;
        graphics2D.draw(this.shape);
    }

    @Override
    public GShape clone() {
        return new GStar();
    }
}
