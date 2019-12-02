import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

public class ToolBar extends JPanel implements ActionListener {
	private JButton select, line, rectangle, oval, pen, text;
	private StringListener listener;
	private MainFrame frame;
	public ToolBar() {
		
	}
	
	public JToolBar createToolBar(){
		JToolBar toolBar = new JToolBar();
		toolBar.setOrientation(SwingConstants.VERTICAL);
		ImageIcon arrowIcon = new ImageIcon(ToolBar.class.getResource("/resources/icons8-cursor-30.png"));
        ImageIcon lineIcon = new ImageIcon(ToolBar.class.getResource("/resources/icons8-line-24.png"));
        ImageIcon rectIcon = new ImageIcon(ToolBar.class.getResource("/resources/icons8-rectangular-50.png"));
        ImageIcon ovalIcon = new ImageIcon(ToolBar.class.getResource("/resources/icons8-oval-50.png"));
        ImageIcon penIcon = new ImageIcon(ToolBar.class.getResource("/resources/icons8-marker-pen-24.png"));
        ImageIcon textIcon = new ImageIcon(ToolBar.class.getResource("/resources/icons8-text-box-32.png"));
		
		select = new JButton(new ImageIcon(arrowIcon.getImage().getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH)));
		line = new JButton(new ImageIcon(lineIcon.getImage().getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH)));
		rectangle = new JButton(new ImageIcon(rectIcon.getImage().getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH)));
		oval = new JButton(new ImageIcon(ovalIcon.getImage().getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH)));
		pen = new JButton(new ImageIcon(penIcon.getImage().getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH)));
		text = new JButton(new ImageIcon(textIcon.getImage().getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH)));
	
		select.addActionListener(this);
		line.addActionListener(this);
		rectangle.addActionListener(this);
		oval.addActionListener(this);
		pen.addActionListener(this);
		text.addActionListener(this);
		
		//ButtonModel lineModel = line.getModel();
		//lineModel.setPressed(true);

		toolBar.add(select);
		toolBar.add(line);
		toolBar.add(rectangle);
		toolBar.add(oval);
		toolBar.add(pen);	
		toolBar.add(text);
		
		toolBar.setFloatable(false);
		
		return toolBar;
	}
	
	public void setStringListener(StringListener listener){
		this.listener = listener;
	}
	
	public void setFrameObject(MainFrame frame) {
		this.frame = frame;
	}
	
	public void actionPerformed(ActionEvent e) {
		JButton item = (JButton)e.getSource();
		ButtonModel lineModel = item.getModel();
		System.out.println(lineModel.isPressed());
		lineModel.setPressed(true);
		System.out.println(lineModel.isPressed());
		if(item == this.select) {
			listener.emitString("Select Tool");
		} else if (item == line) {
			listener.emitString("Line Tool Selected");
			frame.boards.get(frame.currBoardCount).drawShape("line");
		} else if (item == rectangle) {
			listener.emitString("Rectangle Tool Selected");
			frame.boards.get(frame.currBoardCount).drawShape("rect");
		} else if (item == oval) {
			listener.emitString("Oval Tool Selected");
			frame.boards.get(frame.currBoardCount).drawShape("oval");
		} else if (item == pen) {
			listener.emitString("Pen Tool Selected");
			frame.boards.get(frame.currBoardCount).drawShape("pen");
		} else if (item == text) {
			listener.emitString("Text Tool Selected");
			frame.boards.get(frame.currBoardCount).addTextTool();
		} 
	}
}
