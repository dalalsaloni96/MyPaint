import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

// this class is for the menu that appears on the top of application and its
// components and action implementations
public class MenuBar extends JPanel implements ActionListener {
	public JMenuItem newMenuItem, deleteMenuItem, exitMenuItem, nextMenuItem, prevMenuItem;
	private StringListener listener;
	private MainFrame frame;
	public MenuBar() {
	}
	public JMenuBar createMenuBar(){
		JMenuBar menuBar =  new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
		JMenu viewMenu = new JMenu("View");
		newMenuItem = new JMenuItem("New");
		deleteMenuItem = new JMenuItem("Delete");
		exitMenuItem = new JMenuItem("Exit");
		nextMenuItem = new JMenuItem("Next");
		prevMenuItem = new JMenuItem("Previous");
		
		// when lunching the application there will be no new, previous or delete
		nextMenuItem.setEnabled(false);
		prevMenuItem.setEnabled(false);
		deleteMenuItem.setEnabled(false);
		
		// adding to the corroesponding menu 
		fileMenu.add(newMenuItem);
		fileMenu.add(deleteMenuItem);
		fileMenu.add(exitMenuItem);
		viewMenu.add(nextMenuItem);
		viewMenu.add(prevMenuItem);
		
		//adding menus to  the class object
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(viewMenu);
		
		exitMenuItem.addActionListener((event) -> System.exit(0));
		deleteMenuItem.addActionListener(this);
		newMenuItem.addActionListener(this);
		nextMenuItem.addActionListener(this);
		prevMenuItem.addActionListener(this);
		return menuBar;
	}
	
	public void setStringListener(StringListener listener){
		this.listener = listener;
	}
	
	public void setFrameObject(MainFrame frame) {
		this.frame = frame;
	}
	
	public void actionPerformed(ActionEvent e) {
		JMenuItem item = (JMenuItem)e.getSource();
		
		// if delete clicked then delete the current canvas
		if(item == this.deleteMenuItem) {
			listener.emitString("Delete Menu Item Clicked");
			
			// if the last drawing board(canvas) is deleted display the previous one
			if(frame.currBoardCount == frame.totalBoardCount -1){
				// remove the current canvas
				frame.boards.remove(frame.currBoardCount);
				frame.outerTextPanel.removeAll();
				//add the previous canvas to the current display
				frame.outerTextPanel.add(frame.boards.get(frame.currBoardCount-1), BorderLayout.CENTER);
				// current count of canvas decreased
				frame.currBoardCount--;
				// repaint() and revalidate() to display the items on previous canvas
				frame.boards.get(frame.currBoardCount).revalidate();
				frame.boards.get(frame.currBoardCount).repaint();	
				//total boards decreases
				frame.totalBoardCount --;
				// if only one canvas remaining disable the delete menu item
				if(frame.totalBoardCount == 1){
					deleteMenuItem.setEnabled(false);
				}
			} else { // else display the next canvas
				frame.boards.remove(frame.currBoardCount);
				frame.outerTextPanel.removeAll();
				frame.outerTextPanel.add(frame.boards.get(frame.currBoardCount), BorderLayout.CENTER);
				frame.boards.get(frame.currBoardCount).revalidate();
				frame.boards.get(frame.currBoardCount).repaint();
				// don't change the current board count since the index of next board will be now equal to the last board
				frame.totalBoardCount --;
				// if only one canvas remaining disable the delete menu item
				if(frame.totalBoardCount == 1){
					deleteMenuItem.setEnabled(false);
				}
			}
			// conditions for checking whether to disable/enable the next previous menu items
			if(frame.currBoardCount == frame.totalBoardCount - 1 ){
				nextMenuItem.setEnabled(false);
			} else {
				nextMenuItem.setEnabled(true);
			}
			if(frame.currBoardCount == 0){
				prevMenuItem.setEnabled(false);
			} else {
				prevMenuItem.setEnabled(true);
			}
		} else if (item == newMenuItem) {
			listener.emitString("New Menu Item Clicked");
			System.out.println("for creating new panel");
			//update the total number of boards and the current board
			frame.totalBoardCount = frame.totalBoardCount + 1;
			frame.currBoardCount = frame.totalBoardCount - 1;
			// conditions for checking whether to disable/enable the next previous menu items
			if(frame.currBoardCount == frame.totalBoardCount - 1 ){
				nextMenuItem.setEnabled(false);
			} else {
				nextMenuItem.setEnabled(true);
			}
			if(frame.currBoardCount == 0){
				prevMenuItem.setEnabled(false);
			} else {
				prevMenuItem.setEnabled(true);
			}
			//since adding a new board(canvas), enable the delete option
			deleteMenuItem.setEnabled(true);
			//
			//creating a new canvas object (drawing board)
			Canvas newCanvas = new Canvas(frame.currBoardCount);
			frame.boards.add(newCanvas);
			frame.outerTextPanel.removeAll();
			// add the current canvas to the display panel
			frame.outerTextPanel.add(newCanvas,BorderLayout.CENTER);
			revalidate();
			repaint();
		} else if (item == nextMenuItem) {
			listener.emitString("Next Menu Item Clicked");
			if(frame.currBoardCount == frame.totalBoardCount -1) {
				if(frame.currBoardCount == frame.totalBoardCount - 1 ){
					nextMenuItem.setEnabled(false);
				} else {
					nextMenuItem.setEnabled(true);
				}
				if(frame.currBoardCount == 0){
					prevMenuItem.setEnabled(false);
				} else {
					prevMenuItem.setEnabled(true);
				}
				System.out.println("no next");
				
			} else {
				// the current canvas now is the next one, hence increasing the index
				frame.currBoardCount ++;
				frame.outerTextPanel.removeAll();
				
				// conditions for checking whether to disable/enable the next previous menu items
				if(frame.currBoardCount == frame.totalBoardCount - 1 ){
					nextMenuItem.setEnabled(false);
				} else {
					nextMenuItem.setEnabled(true);
				}
				if(frame.currBoardCount == 0){
					prevMenuItem.setEnabled(false);
				} else {
					prevMenuItem.setEnabled(true);
				}
				
				//setting new canvas on display panel
				frame.outerTextPanel.add(frame.boards.get(frame.currBoardCount), BorderLayout.CENTER);
				frame.boards.get(frame.currBoardCount).revalidate();
				frame.boards.get(frame.currBoardCount).repaint();	
			}
		}else if (item == prevMenuItem) {
			
			listener.emitString("Previous Menu Item Clicked");
			if(frame.currBoardCount == 0){
				System.out.println("no previous");
				if(frame.currBoardCount == frame.totalBoardCount - 1 ){
					nextMenuItem.setEnabled(false);
				} else {
					nextMenuItem.setEnabled(true);
				}
				if(frame.currBoardCount == 0){
					prevMenuItem.setEnabled(false);
				} else {
					prevMenuItem.setEnabled(true);
				}
			} else {
				// since want to display the previous canvas or board decrease the index by one
				frame.currBoardCount --;
				frame.outerTextPanel.removeAll();
				// conditions for checking whether to disable/enable the next previous menu items
				if(frame.currBoardCount == frame.totalBoardCount - 1 ){
					nextMenuItem.setEnabled(false);
				} else {
					nextMenuItem.setEnabled(true);
				}
				if(frame.currBoardCount == 0){
					prevMenuItem.setEnabled(false);
				} else {
					prevMenuItem.setEnabled(true);
				}
				
				frame.outerTextPanel.add(frame.boards.get(frame.currBoardCount), BorderLayout.CENTER);
				frame.boards.get(frame.currBoardCount).revalidate();
				frame.boards.get(frame.currBoardCount).repaint();
			}
		} 
	}
	
}
