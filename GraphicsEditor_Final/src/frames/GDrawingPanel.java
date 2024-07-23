package frames;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JColorChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import shapeTools.GFreeDraw;
import shapeTools.GOval;
import shapeTools.GPolygon;
import shapeTools.GRectangle;
import shapeTools.GShape;
import shapeTools.GShape.EAnchors;
import shapeTools.GShape.EDrawingStyle;
import shapeTools.GStar;
import shapeTools.GTextBox;

public class GDrawingPanel extends JPanel {
	// attributes
	private static final long serialVersionUID = 1L;
	private enum EDrawingState {
		eIdle,
		e2PState,
		eNPState,
		eTransformation
	}
	private EDrawingState eDrawingState;

	//components
	private Vector<GShape> shapes;
	private GShape shapeTool;
	private GShape currentShape;

	//Undo and Redo stacks
	private Stack<Vector<GShape>> undoStack;
	private Stack<Vector<GShape>> redoStack;
	
	private int lastMouseX;
	private int lastMouseY;


	//constructors
	public GDrawingPanel() {
		// attributes
		this.setBackground(Color.white);
		this.eDrawingState = EDrawingState.eIdle;

		// components
		MouseEventHandler mouseEventHandler = new MouseEventHandler();
		this.addMouseListener(mouseEventHandler);      
		this.addMouseMotionListener(mouseEventHandler);
		// dynamic components
		this.shapes = new Vector<GShape>();

		// Initialize Undo and Redo stacks
		this.undoStack = new Stack<>();
		this.redoStack = new Stack<>();

		// Initialize the shapeTool
		this.shapeTool = new GRectangle();
		this.shapeTool = new GOval();
		this.shapeTool = new GPolygon();
		this.shapeTool = new GStar();
		this.shapeTool = new GFreeDraw();
		this.shapeTool = new GTextBox();

		// Add PopupMenu
		addPopupMenu();
	}

	// 팝업메뉴 for Copy, Paste, Undo, Redo and Remove
	private void addPopupMenu() {
	    JPopupMenu popupMenu = new JPopupMenu();
	    JMenuItem cutItem = new JMenuItem("Cut");
	    JMenuItem pasteItem = new JMenuItem("Paste");
	    JMenuItem undoItem = new JMenuItem("Undo");
	    JMenuItem redoItem = new JMenuItem("Redo");
	    JMenuItem removeItem = new JMenuItem("Remove");

	    cutItem.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            cut();
	        }
	    });
	    pasteItem.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            paste();
	        }
	    });
	    undoItem.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            undo();
	        }
	    });
	    redoItem.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            redo();
	        }
	    });
	    removeItem.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            GShape selectedShape = onShape(lastMouseX, lastMouseY);
	            if (selectedShape != null) {
	                removeShape(selectedShape);
	            }
	        }
	    });

	    popupMenu.add(cutItem);
	    popupMenu.add(pasteItem);
	    popupMenu.addSeparator();
	    popupMenu.add(undoItem);
	    popupMenu.add(redoItem);
	    popupMenu.addSeparator();
	    popupMenu.add(removeItem);

	    this.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseReleased(MouseEvent e) {
	            if (e.isPopupTrigger()) {
	                lastMouseX = e.getX();
	                lastMouseY = e.getY();
	                popupMenu.show(e.getComponent(), e.getX(), e.getY());
	            }
	        }
	    });
	}

	public void initialize() {
		// TODO Auto-generated method stub

	}


	// setters and getters	
	public void setShapeTool(GShape shapeTool) {
		this.shapeTool = shapeTool;		
	}
	public Vector<GShape> getShapes() { return this.shapes; }
	public void setShapes(Object object) { 
		this.shapes = (Vector<GShape>)object;
		this.repaint();
	}

	// Copy method
	public void cut() {
		Vector<GShape> selectedShapes = new Vector<>();
		for (GShape shape : shapes) {
			if (shape.isSelected()) {
				selectedShapes.add(shape.clone());
			}
		}
		// Push selected shapes to the undo stack
		undoStack.push(new Vector<>(shapes));
		shapes.clear();
		shapes.addAll(selectedShapes);
		repaint();
	}

	// Paste method
	public void paste() {
		if (!undoStack.isEmpty()) {
			Vector<GShape> copiedShapes = new Vector<>(undoStack.peek());
			redoStack.push(new Vector<>(shapes));
			shapes.clear();
			shapes.addAll(copiedShapes);
			repaint();
		}
	}

	// Undo method
	public void undo() {
		if (!undoStack.isEmpty()) {
			redoStack.push(new Vector<>(shapes));
			shapes.clear();
			shapes.addAll(undoStack.pop());
			repaint();
		}
	}

	// Redo method
	public void redo() {
		if (!redoStack.isEmpty()) {
			undoStack.push(new Vector<>(shapes));
			shapes.clear();
			shapes.addAll(redoStack.pop());
			repaint();
		}
	}
	
	public void removeShape(GShape shape) {
	    this.shapes.remove(shape);
	    this.repaint();
	}

	@Override
	public void paint(Graphics graphics) {
	    super.paint(graphics);
	    for (GShape shape : shapes) {
	        shape.draw(graphics);
	    }
	    for (GShape shape : shapes) {
	        shape.drawAnchors(graphics);
	    }
	    this.repaint();
	}
	
	public void clearShapes() {
	    this.shapes.clear();
	    this.repaint();
	}

	private void startDrawing(int x, int y) {
	    if (shapeTool instanceof GTextBox) {
	        currentShape = new GTextBox();
	        ((GTextBox) currentShape).setOrigin(x, y);
	        ((GTextBox) currentShape).setBorderColor(getCurrentBorderColor()); // 현재 선 색상 설정
	    } else {
	        currentShape = shapeTool.clone();
	        currentShape.setOrigin(x, y);
	        currentShape.setFillColor(getCurrentBorderColor()); // 현재 선 색상 설정
	    }
	}

	private void stopDrawing(int x, int y) {
	    if (currentShape != null) {
	        if (currentShape instanceof GTextBox) {
	            ((GTextBox) currentShape).showTextBoxProperties();
	            ((GTextBox) currentShape).setOrigin(x, y);
	            ((GTextBox) currentShape).setBorderColor(((GTextBox) currentShape).getOriginalBorderColor());
	            shapes.add(currentShape);
	            repaint();
	        } else {
	            currentShape.setFillColor(getCurrentBorderColor()); // 현재 선 색상 설정
	            shapes.add(currentShape);
	            currentShape.setSelected(getGraphics());
	        }
	        currentShape = null;
	    }
	}

	private void keepDrawing(int x, int y) {
		currentShape.movePoint(x, y);
		currentShape.drag(getGraphics());	

	}
	private void ContinueDrawing(int x, int y) {
		currentShape.addPoint(x, y);
	}

	private GShape onShape(int x, int y) {
		for (GShape shape: this.shapes) {
			boolean isShape = shape.onShape(x, y);
			if (isShape) {
				return shape;
			}
		}
		return null;		
	}

	private void changeCursor(int x, int y) {
		GShape shape = this.onShape(x, y);
		if (shape == null) {
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} else {
			this.setCursor(shape.getCursor());
		}

	}

	private void changeShapeFillColor(int x, int y) {
		GShape shape = this.onShape(x, y);
		if (shape != null) {
			Color newColor = JColorChooser.showDialog(this, "Choose Fill Color", shape.getFillColor());
			if (newColor != null) {
				shape.setFillColor(newColor);
				this.repaint();
			}
		}
	}
	
	private Color getCurrentBorderColor() {
	    if (shapeTool instanceof GTextBox) {
	        return ((GTextBox) shapeTool).getColor();
	    } else {
	        return shapeTool.getFillColor();
	    }
	}

	private class MouseEventHandler implements MouseListener, MouseMotionListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 1) {
				mouse1Clicked(e);
			} else if (e.getClickCount() == 2) {
				changeShapeFillColor(e.getX(), e.getY());
				mouse2Clicked(e);
			}
		}

		private void mouse1Clicked(MouseEvent e) {
			if (eDrawingState == EDrawingState.eIdle) {
				if (shapeTool.getEDrawingStyle() == EDrawingStyle.eNPStyle) {
					startDrawing(e.getX(), e.getY());
					eDrawingState = EDrawingState.eNPState;
				}
			} else if (eDrawingState == EDrawingState.eNPState) {
				ContinueDrawing(e.getX(), e.getY());
				eDrawingState = EDrawingState.eNPState;
			}			
		}
		private void mouse2Clicked(MouseEvent e) {
			if (eDrawingState == EDrawingState.eNPState) {
				stopDrawing(e.getX(), e.getY());
				eDrawingState = EDrawingState.eIdle;
			}		

		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if (eDrawingState == EDrawingState.eIdle) {
				changeCursor(e.getX(), e.getY());				
			} else if (eDrawingState == EDrawingState.eNPState) {
				keepDrawing(e.getX(), e.getY());
				eDrawingState = EDrawingState.eNPState;
			}		
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (eDrawingState == EDrawingState.eIdle) {
				currentShape = onShape(e.getX(), e.getY());
				if (currentShape == null) {
					if (shapeTool.getEDrawingStyle() == EDrawingStyle.e2PStyle) {
						startDrawing(e.getX(), e.getY());
						eDrawingState = EDrawingState.e2PState;
					}
				} else {
					if (currentShape.getSelectedAnchor() == EAnchors.eMM) {
						currentShape.startMove(getGraphics(), e.getX(), e.getY());
					} else if (currentShape.getSelectedAnchor() == EAnchors.eRR) {
						currentShape.startRotate(getGraphics(), e.getX(), e.getY());
					} else {
						currentShape.startResize(getGraphics(), e.getX(), e.getY());
					}
					eDrawingState = EDrawingState.eTransformation;
				}
			}
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			if (eDrawingState == EDrawingState.e2PState) {
				keepDrawing(e.getX(), e.getY());
			} else if (eDrawingState == EDrawingState.eTransformation) {
				if (currentShape.getSelectedAnchor() == EAnchors.eMM) {
					currentShape.keepMove(getGraphics(), e.getX(), e.getY());
				} else if (currentShape.getSelectedAnchor() == EAnchors.eRR) {
					currentShape.rotate(getGraphics(), e.getX(), e.getY());
				} else {
					currentShape.keepResize(getGraphics(), e.getX(), e.getY());
				}
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if (eDrawingState == EDrawingState.e2PState) {
				stopDrawing(e.getX(), e.getY());
				eDrawingState = EDrawingState.eIdle;
			} else if (eDrawingState == EDrawingState.eTransformation) {
				if (currentShape.getSelectedAnchor() == EAnchors.eMM) {
					currentShape.stopMove(getGraphics(), e.getX(), e.getY());
				} else if (currentShape.getSelectedAnchor() == EAnchors.eRR) {
					// 회전을 멈추는 메서드는 필요 없을 것 같습니다.
				} else {
					currentShape.stopResize(getGraphics(), e.getX(), e.getY());
				}
				eDrawingState = EDrawingState.eIdle;
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}
		@Override
		public void mouseExited(MouseEvent e) {
		}		
	}
}
