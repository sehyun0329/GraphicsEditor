package shapeTools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;

public class GTextBox extends GShape implements Serializable {
    private static final long serialVersionUID = 1L;
    private String text;
    private Font font;
    private Color color;
    private Color originalBorderColor;
    private MouseAdapter mouseAdapter;

    public GTextBox() {
        super(EDrawingStyle.e2PStyle, new Rectangle());
        this.text = "";
        this.font = new Font("나눔고딕", Font.PLAIN, 15);
        this.color = Color.BLACK;
        this.originalBorderColor = Color.BLACK;
    }
        // 마우스 이벤트 핸들러 생성
    private class TextBoxMouseAdapter extends MouseAdapter implements Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                showTextBoxProperties();
            }
        }
    }

    public void showTextBoxProperties() {
        String newText = JOptionPane.showInputDialog(null, "텍스트를 입력하세요:", "Edit Text", JOptionPane.INFORMATION_MESSAGE);
        if (newText != null) {
            setText(newText);
        }

        Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        Font[] koreanFonts = new Font[fonts.length];
        int count = 0;
        for (Font font : fonts) { //이게 없으면 한글 Text 입력하면 깨지는 오류가 생겼었음!
            if (font.canDisplayUpTo("한글") == -1) {
                koreanFonts[count++] = font;
            }
        }
        Font newFont = (Font) JOptionPane.showInputDialog(null, "폰트를 고르세요:", "Edit Font",
                JOptionPane.INFORMATION_MESSAGE, null, koreanFonts, getFont());
        if (newFont != null) {
            setFont(new Font(newFont.getName(), newFont.getStyle(), 15));
        }

        Color newColor = JColorChooser.showDialog(null, "Choose color:", getColor());
        if (newColor != null) {
            setColor(newColor);
        }

        // 테두리 색상 초기화
        setBorderColor(this.originalBorderColor);
    }

    @Override
    public void draw(Graphics graphics) {
        super.draw(graphics);
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setFont(this.font);
        g2d.setColor(this.color);
        if (this.text != null && !this.text.isEmpty()) {
            int textWidth = g2d.getFontMetrics().stringWidth(this.text);
            int textHeight = g2d.getFontMetrics().getHeight();
            g2d.drawString(this.text, this.x1 - (textWidth / 2), this.y1 + (textHeight / 2));
        }
    }

    // Getters and setters for text, font, and color
    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return this.font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    // 테두리 색상 설정 메서드
    public void setBorderColor(Color color) {
        this.originalBorderColor = color;
        super.setFillColor(color);
    }
    
    // 초기 테두리 색상 반환 메서드
    public Color getOriginalBorderColor() {
        return this.originalBorderColor;
    }

    @Override
    public void drag(Graphics graphics) {
        // TODO Auto-generated method stub
    }

    @Override
    public GShape clone() {
        // TODO Auto-generated method stub
        return null;
    }
}
