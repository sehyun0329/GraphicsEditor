package frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import global.Constants.EShapeButtons;
import shapeTools.GTextBox;

public class GShapeToolBar extends JToolBar {
	private static final long serialVersionUID = 1L;
	private GDrawingPanel drawingPanel;
	private HashMap<EShapeButtons, JButton> buttonMap;

	public GShapeToolBar(GDrawingPanel drawingPanel) {
	    this.drawingPanel = drawingPanel;
	    ShapeActionHandler shapeActionHandler = new ShapeActionHandler();
	    buttonMap = new HashMap<>();

	    // Add Rectangle button
	    JButton rectButton = new JButton(new ImageIcon(EShapeButtons.eRectangle.getIconPath()));
	    rectButton.addActionListener(shapeActionHandler);
	    rectButton.setActionCommand(EShapeButtons.eRectangle.toString());
	    add(rectButton);
	    buttonMap.put(EShapeButtons.eRectangle, rectButton);

	    // Add Oval button
	    JButton ovalButton = new JButton(new ImageIcon(EShapeButtons.eOval.getIconPath()));
	    ovalButton.addActionListener(shapeActionHandler);
	    ovalButton.setActionCommand(EShapeButtons.eOval.toString());
	    add(ovalButton);
	    buttonMap.put(EShapeButtons.eOval, ovalButton);

	    // Add Line button
	    JButton lineButton = new JButton(new ImageIcon(EShapeButtons.eLine.getIconPath()));
	    lineButton.addActionListener(shapeActionHandler);
	    lineButton.setActionCommand(EShapeButtons.eLine.toString());
	    add(lineButton);
	    buttonMap.put(EShapeButtons.eLine, lineButton);

	    // Add Polygon button
	    JButton polyButton = new JButton(new ImageIcon(EShapeButtons.ePolygon.getIconPath()));
	    polyButton.addActionListener(shapeActionHandler);
	    polyButton.setActionCommand(EShapeButtons.ePolygon.toString());
	    add(polyButton);
	    buttonMap.put(EShapeButtons.ePolygon, polyButton);

	    // Add Heart button
	    JButton heartButton = new JButton(new ImageIcon(EShapeButtons.eStar.getIconPath()));
	    heartButton.addActionListener(shapeActionHandler);
	    heartButton.setActionCommand(EShapeButtons.eStar.toString());
	    add(heartButton);
	    buttonMap.put(EShapeButtons.eStar, heartButton);

	    // Add Free Draw button
	    JButton freeDrawButton = new JButton(new ImageIcon(EShapeButtons.eFreeDraw.getIconPath()));
	    freeDrawButton.addActionListener(shapeActionHandler);
	    freeDrawButton.setActionCommand(EShapeButtons.eFreeDraw.toString());
	    add(freeDrawButton);
	    buttonMap.put(EShapeButtons.eFreeDraw, freeDrawButton);
	    
	    // Add TextBox button
	    JButton textBoxButton = new JButton(new ImageIcon(EShapeButtons.eTextBox.getIconPath()));
	    textBoxButton.addActionListener(shapeActionHandler);
	    textBoxButton.setActionCommand(EShapeButtons.eTextBox.toString());
	    add(textBoxButton);
	    buttonMap.put(EShapeButtons.eTextBox, textBoxButton);
	}
	
	public void initialize() {
		buttonMap.get(EShapeButtons.eRectangle).doClick();
	}

	public void associate(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}

	private void setShapeTool(EShapeButtons eShapeButton) {
		if (eShapeButton == EShapeButtons.eTextBox) {
			this.drawingPanel.setShapeTool(new GTextBox());
		} else {
			this.drawingPanel.setShapeTool(eShapeButton.getShapeTool());
		}
	}

	private class ShapeActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			EShapeButtons eShapeButton = EShapeButtons.valueOf(e.getActionCommand());
			setShapeTool(eShapeButton);
		}
	}
}