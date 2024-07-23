package global;

import shapeTools.GFreeDraw;
import shapeTools.GLine;
import shapeTools.GOval;
import shapeTools.GPolygon;
import shapeTools.GRectangle;
import shapeTools.GShape;
import shapeTools.GStar;
import shapeTools.GTextBox;

public class Constants {
    public enum EShapeButtons {
    	eRectangle("rectangle", new GRectangle(), "image/rectangle.png"),
        eOval("oval", new GOval(), "image/oval.png"), 
        eLine("line", new GLine(), "image/line.png"),
        ePolygon("polygon", new GPolygon(), "image/polygon.png"),
        eTextBox("TextBox", new GTextBox(), "image/text_box.png"),
        eFreeDraw("Free Draw", new GFreeDraw(), "image/free_draw.png"),
        eStar("Star", new GStar(), "image/star.png");
        
        private String text;
        private GShape shapeTool;
        private String iconPath;
        
        private EShapeButtons(String text, GShape shapeTool, String iconPath) {
            this.text = text;
            this.shapeTool = shapeTool;
            this.iconPath = iconPath;
        }
        
        public String getText() { return this.text; }
        public GShape getShapeTool() { return this.shapeTool; }
        public String getIconPath() { return this.iconPath; }
    }
    
    public final static int NUM_POINTS = 20; 
    
    public static class GMainFrame {
        public final static int WIDTH = 500;
        public final static int HEIGHT = 600;
    }
    
    public static class GMenuBar {
        public enum EMemu {
            eFile("파일"),
            eEdit("편집");
            
            private String text;
            private EMemu(String text) {
                this.text = text;
            }
            public String getText() { return this.text; }
        }
    }
}
