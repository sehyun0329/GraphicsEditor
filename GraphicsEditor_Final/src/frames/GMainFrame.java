package frames;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import global.Constants;
import menus.GFileMenu;

public class GMainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	//attributes

	//components
	private GMenuBar menuBar;
	private GShapeToolBar shapeToolBar;    
	private GDrawingPanel drawingPanel;
	private JTabbedPane tabPane;
	private Vector<GDrawingPanel> drawingPanelList;
	private int pageCount = 1;

	//constructor
	public GMainFrame() {
		// set attributes

		this.setSize(Constants.GMainFrame.WIDTH, Constants.GMainFrame.HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// create components
		LayoutManager layoutManager = new BorderLayout();
		this.setLayout(layoutManager);

		this.drawingPanelList = new Vector<>();
		this.drawingPanel = new GDrawingPanel();
		this.drawingPanelList.add(this.drawingPanel);

		this.menuBar = new GMenuBar(this.drawingPanel);
		this.setJMenuBar(this.menuBar);        

		this.shapeToolBar = new GShapeToolBar(this.drawingPanel);
		this.add(shapeToolBar, BorderLayout.NORTH);

		this.tabPane = new JTabbedPane();
		this.tabPane.addTab("Page1", this.drawingPanel);
		this.add(tabPane, BorderLayout.CENTER);

		// associate
		this.menuBar.associate(this.drawingPanel);
		this.shapeToolBar.associate(this.drawingPanel);
	}

	//methods
	public void initialize() {
		this.menuBar.initialize();
		this.shapeToolBar.initialize();
		this.drawingPanel.initialize();
	}

	private class GMenuBar extends JMenuBar {
		private static final long serialVersionUID = 1L;

		private GFileMenu fileMenu;
		private GDrawingPanel drawingPanel;
		private JMenuItem addPanelItem;
		private JMenuItem removePanelItem;

		public GMenuBar(GDrawingPanel drawingPanel) {
			this.drawingPanel = drawingPanel;
			this.fileMenu = new GFileMenu(this.drawingPanel);
			this.add(this.fileMenu);

			// Add "Add Panel" and "Remove Panel" menu items
			JMenu editMenu = new JMenu("Edit");
			this.addPanelItem = new JMenuItem("Add Panel");
			this.removePanelItem = new JMenuItem("Remove Panel");
			editMenu.add(this.addPanelItem);
			editMenu.add(this.removePanelItem);
			this.add(editMenu);

			this.addPanelItem.addActionListener(new AddPanelActionListener());
			this.removePanelItem.addActionListener(new RemovePanelActionListener());
		}

		public void associate(GDrawingPanel drawingPanel) {
			this.drawingPanel = drawingPanel;
			this.fileMenu.associate(this.drawingPanel);
		}

		public void initialize() {
			this.fileMenu.initialize();
		}

		private class AddPanelActionListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				GDrawingPanel newDrawingPanel = new GDrawingPanel();
				drawingPanelList.add(newDrawingPanel);
				tabPane.addTab("Page" + (pageCount + 1), newDrawingPanel);
				pageCount++;
				tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
				shapeToolBar.associate(newDrawingPanel);
				menuBar.associate(newDrawingPanel);
			}
		}

		private class RemovePanelActionListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (drawingPanelList.size() > 1) {
					int selectedIndex = tabPane.getSelectedIndex();
					drawingPanelList.remove(selectedIndex);
					tabPane.remove(selectedIndex);
					if (selectedIndex > 0) {
						tabPane.setSelectedIndex(selectedIndex - 1);
					} else {
						tabPane.setSelectedIndex(0);
					}
					shapeToolBar.associate(drawingPanelList.get(tabPane.getSelectedIndex()));
					menuBar.associate(drawingPanelList.get(tabPane.getSelectedIndex()));
				}
			}
		}
	}
}
