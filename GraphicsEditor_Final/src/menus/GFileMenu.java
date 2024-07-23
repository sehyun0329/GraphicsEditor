package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import frames.GDrawingPanel;
import shapeTools.GShape;

public class GFileMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	private GDrawingPanel drawingPanel;

	public GFileMenu(String s) {
		super(s);
		ActionHandler actionHandler = new ActionHandler();

		JMenuItem menuItemSave = new JMenuItem("save");
		menuItemSave.setActionCommand("save");
		menuItemSave.addActionListener(actionHandler);
		this.add(menuItemSave);
		
		JMenuItem menuItemOpen = new JMenuItem("open");
		menuItemOpen.setActionCommand("open");
		menuItemOpen.addActionListener(actionHandler);
		this.add(menuItemOpen);
		
		JMenuItem clearItem = new JMenuItem("clear");
	    clearItem.setActionCommand("clear");
	    clearItem.addActionListener(actionHandler);
	    this.add(clearItem);
	}

	public GFileMenu(GDrawingPanel drawingPanel) {
		this("file");
		this.drawingPanel = drawingPanel;
	}

	public void associate(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}

	public void initialize() {
		
	}

	private void open() {
		try {
			File file = new File("output");
			ObjectInputStream objectInputStream = new ObjectInputStream(
					new BufferedInputStream(new FileInputStream(file)));
			Object object = objectInputStream.readObject();
			this.drawingPanel.setShapes((Vector<GShape>) object);
			objectInputStream.close();
			this.repaint();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void save() {
		try {
			File file = new File("output");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					new BufferedOutputStream(
							new FileOutputStream(file)));
			objectOutputStream.writeObject(this.drawingPanel.getShapes());
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class ActionHandler implements ActionListener {
	    @Override
	    public void actionPerformed(ActionEvent e) {
	        if (e.getActionCommand().equals("open")) {
	            open();
	        } else if (e.getActionCommand().equals("save")) {
	            save();
	        } else if (e.getActionCommand().equals("clear")) {
	            clear();
	        }
	    }

	    private void clear() {
	        drawingPanel.clearShapes();
	    }
	}
}