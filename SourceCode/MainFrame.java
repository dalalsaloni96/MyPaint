import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

//this class is for the main JFrame in which other components are added
public class MainFrame extends JFrame{
	
	private Canvas textPanel;       // the drawing board
	public JPanel outerTextPanel;	// display panel to which drawing board/canvas gets added to
	private JLabel statusBar;		// status bar for displaying the messages
	private MenuBar menuBar;		// file menu bar
	private StringListener listener;
	private ToolBar toolBar;		// toolbar 
	private JScrollPane scroll; 	//scroll pane for the display list
	List<Canvas> boards = new ArrayList<Canvas>();  // list of drawing boards/canvases maintained
	int currBoardCount = 0;
	int totalBoardCount = 1;
	public MainFrame() {
		super ("Paint");
		setLayout(new BorderLayout());
		textPanel = new Canvas(currBoardCount);
		boards.add(textPanel);
		statusBar = new JLabel();
		menuBar = new MenuBar();
		toolBar = new ToolBar();
		outerTextPanel = new JPanel();
		outerTextPanel.setLayout(new BorderLayout());
		outerTextPanel.setPreferredSize(new Dimension(1200,1200));
		outerTextPanel.add(textPanel, BorderLayout.CENTER);
		menuBar.setStringListener(new StringListener() {
			public void emitString(String text) {
				statusBar.setText(text);
			}
		});
		menuBar.setFrameObject(this);
		toolBar.setStringListener(new StringListener() {
			public void emitString(String text) {
				statusBar.setText(text);
			}
		});
		
		toolBar.setFrameObject(this);
		scroll = new JScrollPane(outerTextPanel);
		scroll.setBorder(new EmptyBorder( 0, 0, 10, 10 ));
		toolBar.setBorder(new EmptyBorder(5,5,5,5));
		this.setJMenuBar(menuBar.createMenuBar());
		
		// defining position for each component
		add(scroll,BorderLayout.CENTER);
		add(menuBar, BorderLayout.NORTH);
		add(toolBar.createToolBar(), BorderLayout.WEST);
		add(statusBar, BorderLayout.SOUTH);
		
		this.pack();
		setMinimumSize(new Dimension(100, 250));
		setSize(600,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
