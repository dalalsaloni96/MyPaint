import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import javax.swing.JPanel;

// canvas class is the drawing board of the paint application


public class Canvas extends JPanel {
	public  int panelNumber;
	private Graphics2D g2;
	//display list to maintain shapes
	List<Shape> displayList = new ArrayList<Shape>();
	int displayListCounter = -1;
	// variables maintained for dragging
	int oldX, oldY, currX, currY, finalX, finalY;
	// variables for maintaining drag of TextBox
	int text1X, text1Y, text2X, text2Y;
	// text display list for maintaining texts
	List<TextBox> textDisplayList = new ArrayList<TextBox>();
	int textDisplayListCount = -1;
	int isReleased = 0;
	int isDragging = 0;
	int isPressed = 0;
	String type ;
	TextBox textBox;
	// stroke obj for putting dashed line around text box
	final static float dash1[] = { 10.0f };
	final static BasicStroke dashed = new BasicStroke(1.0f,BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
	// constructor gets called on creation of a new canvas
	public Canvas(int n) {
		super();
		this.panelNumber = n;
		this.setOpaque(true);
		this.setBackground(Color.white);
		setPreferredSize(new Dimension(1200,1200));
	}
	public void setToolType(String type){
		this.type =  type;
	}
	
	protected void paintComponent(Graphics g){
		g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		super.paintComponent(g2);
		// storing the default stroke
		Stroke defaultStroke = g2.getStroke();
		
		//traverse through display list and put all shapes on the canvas
		displayList.forEach(shape -> {
			if(shape.type == "line" || shape.type == "pen"){
				g2.drawLine(shape.x1, shape.y1, shape.x2, shape.y2);
			} else if(shape.type == "rect"){
				g2.drawRect(Math.min(shape.x2,shape.x1),Math.min(shape.y2, shape.y1),Math.abs(shape.x2 - shape.x1), Math.abs(shape.y2 - shape.y1));
			} else if(shape.type=="oval"){
				g2.drawOval(Math.min(shape.x2,shape.x1),Math.min(shape.y2, shape.y1),Math.abs(shape.x2 - shape.x1), Math.abs(shape.y2 - shape.y1));
			}
		});
		
		//traverse through text display list and put back all the text
		textDisplayList.forEach(textObj -> {
			String[] tempText = textObj.text.split("\n");
			if(textObj.number != textDisplayListCount){
				int len = tempText.length;
				int height = g2.getFontMetrics().getHeight();
				for(int i=0;i<len;i++){
					g2.drawString(tempText[i], Math.min(textObj.x1,textObj.x2), Math.min(textObj.y1, textObj.y2)+ 10 + i*height);
				}
			}
				
		});
		
		if(this.type == "text" && textDisplayListCount>-1 ){
			//setting dashed line as stroke for type text
			g2.setStroke(dashed);
			TextBox currActive = textDisplayList.get(textDisplayListCount);
			int height = g2.getFontMetrics().getHeight();
			if(currActive.text.length() > 0){
				// get the number of lines and texts
				String[] tempTexts = currActive.text.split("\n");
				
				// if number of lines greater than the current height of the box, increase the height
				if((tempTexts.length+1)*height > Math.abs(currActive.y1-currActive.y2)){
					if(currActive.y1 > currActive.y2){
						currActive.y1 += height;
					} else {
						currActive.y2 += height;
					}
					g2.drawRect(Math.min(currActive.x1,currActive.x2),Math.min(currActive.y1, currActive.y2),Math.abs(currActive.x1-currActive.x2), Math.abs(currActive.y1-currActive.y2));
					//set the default line stroke back
					g2.setStroke(defaultStroke);
				} else {
					
					// draw box with the original dimensions
					g2.drawRect(Math.min(currActive.x1,currActive.x2),Math.min(currActive.y1, currActive.y2),Math.abs(currActive.x1-currActive.x2), Math.abs(currActive.y1-currActive.y2));
					//set the default line stroke back
					g2.setStroke(defaultStroke);
				}
			} else {
				g2.drawRect(Math.min(text1X,text2X),Math.min(text1Y, text2Y),Math.abs(text1X-text2X), Math.abs(text1Y-text2Y));
				//g2.drawRect(Math.min(currActive.x1,currActive.x2),Math.min(currActive.y1, currActive.y2),Math.abs(currActive.x1-currActive.x2), Math.abs(currActive.y1-currActive.y2));
				g2.setStroke(defaultStroke);
			}
			if(currActive.text.length() > 0){
				//get the number of lines by splitting by next line character
				String[] texts = currActive.text.split("\n");
				int arrLen = texts.length;
				for(int i=0;i<arrLen;i++){
					System.out.println(texts[i]);
					// finding width of the current text
					int width = g2.getFontMetrics().stringWidth(texts[i]);
					System.out.println("width of the current text "+width);
					// if width less than current text box width then drawString()
					if(width <= Math.abs(currActive.x1-currActive.x2)){
						g2.drawString(texts[i],Math.min(currActive.x1,currActive.x2) , Math.min(currActive.y1, currActive.y2)+ 10 + i*height);
					} else {
						//get the last space character
						int lastWhiteSpace = texts[i].lastIndexOf(" ");
						// if last white space, split the line by space
						if(lastWhiteSpace != -1){
							String temp = texts[i].substring(0, lastWhiteSpace);
							String temp2 = texts[i].substring(lastWhiteSpace+1, texts[i].length());
							g2.drawString(temp,Math.min(currActive.x1,currActive.x2) , Math.min(currActive.y1, currActive.y2)+ 10 + i*height);
							g2.drawString(temp2,Math.min(currActive.x1,currActive.x2) , Math.min(currActive.y1, currActive.y2)+ 10 + (i+1)*height);
							texts[i]= temp + "\n" + temp2;
						} else {
							if(texts[i].length() != 0){
								// split by last character
								int lastCharacter = texts[i].length()-1;
								String temp = texts[i].substring(0, lastCharacter);
								String temp2 = texts[i].substring(lastCharacter, texts[i].length());
								g2.drawString(temp,Math.min(currActive.x1,currActive.x2) , Math.min(currActive.y1, currActive.y2)+ 10 + i*height);
								g2.drawString(temp2,Math.min(currActive.x1,currActive.x2) , Math.min(currActive.y1, currActive.y2)+ 10 + (i+1)*height);
								texts[i]= temp + "\n" + temp2;
							}
						}
					}
				}
				currActive.text = "";
				// form the currActive text again with added \n
				for(int k =0;k<arrLen;k++){
					if(k == arrLen-1)
						currActive.text += texts[k];
					else
						currActive.text = currActive.text + texts[k] + "\n";
				}
				System.out.println(currActive.text);
			}
			
		}
		//drawing line with continuous rubber banding
		if(this.type == "pen" && (isReleased ==1 || isDragging == 1)){
			Shape s = new Shape();
			s.x1 = oldX;
			s.y1 = oldY;
			s.x2 = isReleased == 1 ? finalX : currX;
			s.y2 = isReleased == 1 ? finalY : currY;
			s.type = this.type;
			displayList.add(s);
			displayListCounter ++;
			g2.drawLine(oldX, oldY, s.x2, s.y2);
			oldX = s.x2;
			oldY = s.y2;
			if(isReleased == 1){
				isReleased = 0;
			}
		} else {
			if(isReleased == 1){
				int width =  Math.abs(oldX - finalX);
				int height = Math.abs(oldY - finalY);
				g2.setPaint(Color.BLACK);
				Shape s = new Shape();
				s.x1 = oldX;
				s.y1 = oldY;
				s.x2 = finalX;
				s.y2 = finalY;
				s.type = this.type;
				isReleased = 0;
				// when released add the shape  with final coordinates to display list
				displayList.add(s);
				displayListCounter ++;
				// use type to decide which shape to draw
				if(this.type == "line"){
					g2.drawLine(oldX, oldY, finalX, finalY);
				} else if(this.type == "rect"){
					g2.drawRect(Math.min(finalX,oldX),Math.min(finalY, oldY),width, height);
				} else if(this.type == "oval"){
					g2.drawOval(Math.min(finalX,oldX),Math.min(finalY, oldY),width, height);
				}
			} else if(isDragging == 1){
				// when dragging draw the shape with updated curr coordinates
				int width =  Math.abs(currX - oldX);
				int height = Math.abs(currY - oldY);
				g2.setPaint(Color.BLACK);
				if(this.type == "line"){
					g2.drawLine(oldX, oldY, currX, currY);
				} else if(this.type == "rect"){
					g2.drawRect(Math.min(currX,oldX),Math.min(currY, oldY),width, height);
				} else if(this.type == "oval"){
					g2.drawOval(Math.min(currX,oldX),Math.min(currY, oldY),width, height);
				}
			}
		}
		 
	}
	
	// function which gets called from mainframe when tool is selected
	public void drawShape(String type){
		setDoubleBuffered(true);
		
		this.type = type;
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				oldX = e.getX();
		        oldY = e.getY();
				currX = finalX = oldX;
				currY = finalY = oldY;
				isDragging = 1	;
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				isReleased = 1;
				isPressed = 0;
				isDragging = 0;
				finalX = e.getX();
				finalY = e.getY();
				repaint();
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter(){
			@Override
			public void mouseDragged(MouseEvent e) {
				
				currX = e.getX();
				currY = e.getY();
				repaint();
			}	
		});
	}
	
	// function used to add text tool, gets called from mainframe
	public void addTextTool(){
		this.type = "text";
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				//initiate a new TextBox object
				textBox = new TextBox();
				// set start coordinates
				textBox.x1 = e.getX();
				textBox.y1 = e.getY();
				text1X = textBox.x1;
				text1Y = textBox.y1;
				
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				textBox.x2 = e.getX();
				textBox.y2 = e.getY();
				text2X  = textBox.x2;
				text2Y = textBox.y2;
				repaint();
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter(){
			@Override
			public void mouseDragged(MouseEvent e) {
				textBox.x2 = e.getX();
				textBox.y2 = e.getY();
				text2X  = textBox.x2;
				text2Y = textBox.y2;
				//add the current text  box to the text display list
				textDisplayList.add(textBox);
				textDisplayListCount ++;
				textBox.number = textDisplayListCount;
				repaint();
			}	
		});
		// listening to character being typed and adding to the text of the current active text box
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				char c = e.getKeyChar();
				StringBuilder sb = new StringBuilder();
				sb.append(c);
				sb.toString();
				textBox.text = textBox.text + sb;
				repaint();
			}
            
        }); 
		this.requestFocusInWindow(true);
	}

}
