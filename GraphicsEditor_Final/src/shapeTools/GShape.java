package shapeTools;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

public abstract class GShape implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum EDrawingStyle {
		e2PStyle,
		eNPStyle
	}
	private EDrawingStyle eDrawingStyle;
	public EDrawingStyle getEDrawingStyle() {
		return this.eDrawingStyle;
	}
	
	// int x[], int y[]
	protected int x1, y1, x2, y2, ox2, oy2;
	protected Shape shape;
	
	public enum EAnchors {
		eRR(new Cursor(Cursor.HAND_CURSOR)),
		eNN(new Cursor(Cursor.N_RESIZE_CURSOR)),
		eSS(new Cursor(Cursor.S_RESIZE_CURSOR)),
		eEE(new Cursor(Cursor.E_RESIZE_CURSOR)),
		eWW(new Cursor(Cursor.W_RESIZE_CURSOR)),
		eNE(new Cursor(Cursor.NE_RESIZE_CURSOR)),
		eSE(new Cursor(Cursor.SE_RESIZE_CURSOR)),
		eNW(new Cursor(Cursor.NW_RESIZE_CURSOR)),
		eSW(new Cursor(Cursor.SW_RESIZE_CURSOR)),
		eMM(new Cursor(Cursor.CROSSHAIR_CURSOR));
		
		private Cursor cursor;
		private EAnchors(Cursor cursor) {
			this.cursor = cursor;
		}
		public Cursor getCursor() {
			return this.cursor;
		}
	}
	private EAnchors eSelectedAnchor;
	protected Ellipse2D.Float[] anchors;
	
	private double sx, sy;
	private double dx, dy;
	
	private double startAngle; // Starting angle for rotation
	private double rotationAngle = 0.0;
	private Point2D rotationStartPoint;
	
	private Color fillColor;
	private boolean selected;
	
	// setters and getters
	public void setRotationStartPoint(int x, int y) {
		this.rotationStartPoint = new Point2D.Double(x, y);
	}
	// 회전 시작 지점 반환
	public Point2D getRotationStartPoint() {
		return this.rotationStartPoint;
	}

	// 회전 각도 설정
	public void setRotationAngle(double angle) {
		this.rotationAngle = angle;
	}

	// 회전 각도 반환
	public double getRotationAngle() {
		return this.rotationAngle;
	}
	    
	public void setSelected(Graphics graphics) {
		this.drawAnchors(graphics);
	}
	public EAnchors getSelectedAnchor() {
		return this.eSelectedAnchor;
	}
	
	public void setFillColor(Color color) {
	    this.fillColor = color;
	}
	
	public Color getFillColor() {
	    return this.fillColor;
	}

	// selected 상태 set
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	// selected 상태 get
	public boolean isSelected() {
		return this.selected;
	}
	
	public void clearSelected() {
		this.anchors = null;
	}
	
	public Cursor getCursor() {
		return this.eSelectedAnchor.getCursor();
	}
	
	public GShape(EDrawingStyle eDrawingStyle, Shape shape) {
		this.eDrawingStyle = eDrawingStyle;
		this.shape = shape;
		this.fillColor = null;
		
		this.anchors = null;
		this.eSelectedAnchor = null;
		
		this.x1 = 0;
		this.y1 = 0;
		this.x2 = 0;
		this.y2 = 0;
		this.ox2 = 0;
		this.oy2 = 0;
	}
	public abstract GShape clone();	

	public void draw(Graphics graphics) {
		Graphics2D g2d = (Graphics2D) graphics;
		if (this.fillColor != null) {
			g2d.setColor(this.fillColor);
			g2d.fill(this.shape);
			g2d.setColor(Color.BLACK);
		}
		g2d.draw(this.shape);
	}

	public void drawAnchors(Graphics graphics) {
		Graphics2D graphics2D = (java.awt.Graphics2D) graphics;
		Rectangle rectangle = this.shape.getBounds();
		int x = rectangle.x;
		int y = rectangle.y;
		int w = rectangle.width;
		int h = rectangle.height;
		int ANCHOR_WIDTH = 10;
		int ANCHOR_HEIGHT = 10;

		this.anchors = new Ellipse2D.Float[EAnchors.values().length-1];
		this.anchors[EAnchors.eRR.ordinal()] = new Ellipse2D.Float(x+w / 2 - ANCHOR_WIDTH / 2, y-30, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eNN.ordinal()] = new Ellipse2D.Float(x + w / 2 - ANCHOR_WIDTH / 2, y - ANCHOR_HEIGHT / 2, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eSS.ordinal()] = new Ellipse2D.Float(x + w / 2 - ANCHOR_WIDTH / 2, y + h - ANCHOR_HEIGHT / 2, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eEE.ordinal()] = new Ellipse2D.Float(x + w - ANCHOR_WIDTH / 2, y + h / 2 - ANCHOR_HEIGHT / 2, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eWW.ordinal()] = new Ellipse2D.Float(x - ANCHOR_WIDTH / 2, y + h / 2 - ANCHOR_HEIGHT / 2, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eNW.ordinal()] = new Ellipse2D.Float(x - ANCHOR_WIDTH / 2, y - ANCHOR_HEIGHT / 2, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eNE.ordinal()] = new Ellipse2D.Float(x + w - ANCHOR_WIDTH / 2, y - ANCHOR_HEIGHT / 2, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eSW.ordinal()] = new Ellipse2D.Float(x - ANCHOR_WIDTH / 2, y + h - ANCHOR_HEIGHT / 2, ANCHOR_WIDTH, ANCHOR_HEIGHT);
		this.anchors[EAnchors.eSE.ordinal()] = new Ellipse2D.Float(x + w - ANCHOR_WIDTH / 2, y + h - ANCHOR_HEIGHT / 2, ANCHOR_WIDTH, ANCHOR_HEIGHT);

		for (int i = 0; i < EAnchors.values().length - 1; i++) {
			if (EAnchors.values()[i] == EAnchors.eRR) {
				// eRR Anchor를 빨간색으로 채우기
				graphics2D.setColor(Color.RED);
			} else {
				// 다른 Anchors는 회색으로 채우기
				graphics2D.setColor(Color.GRAY);
			}
			// Anchors를 채우고 테두리 그리기
			graphics2D.fill(this.anchors[i]);
			graphics2D.setColor(Color.BLACK);
			graphics2D.draw(this.anchors[i]);
		}

	}
	public void setOrigin(int x1, int y1) {
		this.x1 = x1;
		this.y1 = y1;

		this.ox2 = x1;
		this.oy2 = y1;
		this.x2 = x1;
		this.y2 = y1;
	}
	public void movePoint(int x2, int y2) {
		this.ox2 = this.x2;
		this.oy2 = this.y2;
		this.x2 = x2;
		this.y2 = y2;
	}
	public abstract void drag(Graphics graphics);

	public void addPoint(int x2, int y2) {
		this.x2 = x2;
		this.y2 = y2;
	}

	public boolean onShape(int x, int y) {
		this.eSelectedAnchor = null;
		if (this.anchors != null) {
			for (int i=0; i<EAnchors.values().length-1; i++) {
				if (anchors[i].contains(x, y)) {
					this.eSelectedAnchor = EAnchors.values()[i];
					return true;
				}
			}
		}
		boolean isOnShape = this.shape.contains(x, y);
		if (isOnShape) {
			this.eSelectedAnchor = EAnchors.eMM;
		} else if (this.shape instanceof Line2D.Float) { //Line을 그릴 경우 선택
			Line2D.Float line = (Line2D.Float) this.shape;
			double distance = Line2D.Float.ptSegDist(line.x1, line.y1, line.x2, line.y2, x, y);
			if (distance <= 10) { // 10 픽셀 이내의 거리에 있는 경우 선택
				this.eSelectedAnchor = EAnchors.eMM;
				return true;
			}
		}
		return isOnShape;
	}


	public void startRotate(Graphics graphics, int x, int y) {
		// 회전 시작 지점 설정
		setRotationStartPoint(x, y);
		// 회전 각도 초기화
		setRotationAngle(0.0);
		// 회전 앵커 표시
		drawRotationAnchor(graphics);
	}
	
	public void rotate(Graphics graphics, int x, int y) {
		if (rotationStartPoint == null) {
			return;
		}
		// 회전 중심을 도형의 중심으로 설정
		Point2D center = getShapeCenter();
		double angle = Math.atan2(y - center.getY(), x - center.getX());
		// 회전 각도 계산
		double deltaAngle = angle - rotationAngle;
		// 회전 각도 설정
		setRotationAngle(angle);
		// 회전 변환 적용
		AffineTransform transform = new AffineTransform();
		transform.rotate(deltaAngle, center.getX(), center.getY());
		this.shape = transform.createTransformedShape(this.shape);
		// 도형 다시 그리기
		draw(graphics);
		// 회전 앵커 다시 그리기
		drawRotationAnchor(graphics);
	}

	// 도형의 중심을 반환하는 메서드
	private Point2D getShapeCenter() {
		Rectangle bounds = this.shape.getBounds();
		return new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
	}

	// 회전 앵커 표시 메서드
	private void drawRotationAnchor(Graphics graphics) {
		if (rotationStartPoint != null) {
			Graphics2D g2d = (Graphics2D) graphics;
			g2d.setColor(Color.white);
			g2d.fillOval((int) rotationStartPoint.getX() - 5, (int) rotationStartPoint.getY() - 5, 10, 10);
		}
	}

	public void startMove(Graphics graphics, int x, int y) {
		this.ox2 = x;
		this.oy2 = y;
		this.x2 = x;
		this.y2 = y;
	}
	public void keepMove(Graphics graphics, int x, int y) {
		this.ox2 = this.x2;
		this.oy2 = this.y2;
		this.x2 = x;
		this.y2 = y;

		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setXORMode(graphics2D.getBackground());

		// Erase the previous shape
		graphics2D.draw(this.shape);

		if (this.shape instanceof Line2D.Float) {
			Line2D.Float line = (Line2D.Float) this.shape;
			line.setLine(line.x1 + (x - this.ox2), line.y1 + (y - this.oy2), line.x2 + (x - this.ox2), line.y2 + (y - this.oy2));
		} else {
			int dx = x2 - ox2;
			int dy = y2 - oy2;
			AffineTransform affineTransform = new AffineTransform();
			affineTransform.setToTranslation(dx, dy);
			this.shape = affineTransform.createTransformedShape(this.shape);
		}

		// Draw the shape at the new position
		graphics2D.draw(this.shape);

		// Update the anchors only once
		this.drawAnchors(graphics2D);
	}

	public void stopMove(Graphics graphics, int x, int y) {
	}

	public Shape getShape() {
		return this.shape;
	}

	public void startResize(Graphics graphics, int x, int y) {
		this.ox2 = x;
		this.oy2 = y;
		this.x2 = x;
		this.y2 = y;
	}
	public Point2D getResizeFactor() {
		sx = 1.0;
		sy = 1.0;
		dx = 0.0;
		dy = 0.0;

		double cx = this.anchors[this.eSelectedAnchor.ordinal()].getCenterX();
		double cy = this.anchors[this.eSelectedAnchor.ordinal()].getCenterY();
		double w = this.shape.getBounds().getWidth();
		double h = this.shape.getBounds().getHeight();
		double ox = this.shape.getBounds().getX();
		double oy = this.shape.getBounds().getY();

		switch (this.eSelectedAnchor) {
		case eEE:
			sx = (x2 - ox) / w;
			break;
		case eWW:
			sx = (ox2 - x2 + w) / w;
			dx = (cx - ox) * (1 - sx);
			break;
		case eSS:
			sy = (y2 - oy) / h;
			break;
		case eNN:
			sy = (oy2 - y2 + h) / h;
			dy = (cy - oy) * (1 - sy);
			break;
		case eNW:
			sx = (ox2 - x2 + w) / w;
			sy = (oy2 - y2 + h) / h;
			dx = (cx - ox) * (1 - sx);
			dy = (cy - oy) * (1 - sy);
			break;
		case eNE:
			sx = (x2 - ox) / w;
			sy = (oy2 - y2 + h) / h;
			dx = (cx - ox) * (1 - sx);
			dy = (cy - oy) * (1 - sy);
			break;
		case eSW:
			sx = (ox2 - x2 + w) / w;
			sy = (y2 - oy) / h;
			dx = (cx - ox) * (1 - sx);
			dy = (cy - oy) * (1 - sy);
			break;
		case eSE:
			sx = (x2 - ox) / w;
			sy = (y2 - oy) / h;
			dx = (cx - ox) * (1 - sx);
			dy = (cy - oy) * (1 - sy);
			break;
		default:
			break;
		}

		return new Point2D.Double(sx, sy);
	}

	public void keepResize(Graphics graphics, int x, int y) {
		this.ox2 = this.x2;
		this.oy2 = this.y2;
		this.x2 = x;
		this.y2 = y;

		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setXORMode(graphics2D.getBackground());

		// Erase the previous shape
		graphics2D.draw(this.shape);

		Point2D resizeFactor = getResizeFactor();

		AffineTransform affineTransform = new AffineTransform();

		double centerX = 0.0;
		double centerY = 0.0;
		double width = this.shape.getBounds().getWidth();
		double height = this.shape.getBounds().getHeight();

		switch (this.eSelectedAnchor) {
		case eEE:
			centerX = this.shape.getBounds2D().getMinX();
			centerY = this.shape.getBounds2D().getCenterY();
			break;
		case eWW:
			centerX = this.shape.getBounds2D().getMaxX();
			centerY = this.shape.getBounds2D().getCenterY();
			break;
		case eSS:
			centerX = this.shape.getBounds2D().getCenterX();
			centerY = this.shape.getBounds2D().getMinY();
			break;
		case eNN:
			centerX = this.shape.getBounds2D().getCenterX();
			centerY = this.shape.getBounds2D().getMaxY();
			break;
		case eNW:
			centerX = this.shape.getBounds2D().getMaxX();
			centerY = this.shape.getBounds2D().getMaxY();
			break;
		case eNE:
			centerX = this.shape.getBounds2D().getMinX();
			centerY = this.shape.getBounds2D().getMaxY();
			break;
		case eSW:
			centerX = this.shape.getBounds2D().getMaxX();
			centerY = this.shape.getBounds2D().getMinY();
			break;
		case eSE:
			centerX = this.shape.getBounds2D().getMinX();
			centerY = this.shape.getBounds2D().getMinY();
			break;
		default:
			break;
		}

		affineTransform.translate(centerX, centerY);
		affineTransform.scale(resizeFactor.getX(), resizeFactor.getY());
		affineTransform.translate(-centerX, -centerY);
		this.shape = affineTransform.createTransformedShape(this.shape);

		// Draw the shape at the new position
		graphics2D.draw(this.shape);

		// Update the anchors only once
		this.drawAnchors(graphics2D);
	}

	public void stopResize(Graphics graphics, int x, int y) {
	}
}
