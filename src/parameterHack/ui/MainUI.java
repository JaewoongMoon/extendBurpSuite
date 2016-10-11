/**
 * @ java 
 */
package parameterHack.ui;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

/**
 * <pre>
 * parameterHack.ui
 * java 
 * </pre>
 *
 * @brief	: 
 * @author	: 문재웅(mjw8585@gmail.com)
 * @Date	: 2016. 10. 11.
 */
public class MainUI extends JFrame{

	public FuzzerUI fuzzerUI = null;
	public CipherUI cipherUI = null;
	public JTabbedPane tabs = null;
	
	public MainUI(){
		setTitle("Funky Ver 1.3");
		setLayout(null);
		cipherUI = new CipherUI();
		fuzzerUI = new FuzzerUI();
		
		tabs = new JTabbedPane();
		tabs.addTab("Fuzzer String Maker", fuzzerUI);
		tabs.addTab("Encrypt / Decrypt", cipherUI);
		tabs.setBounds(0, 0, 950, 700);
		add(tabs);
		
		/* 탭 단축키 설정? */
		// 메뉴바 
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu();
		JMenuItem menuItem1 = new JMenuItem("퍼저 탭 이동");
		JMenuItem menuItem2 = new JMenuItem("암호 탭 이동");
		menuItem1.setAccelerator(KeyStroke.getKeyStroke('1', Event.CTRL_MASK));
		menuItem1.addActionListener(new TabSelectionHandler(0));
		menuItem2.setAccelerator(KeyStroke.getKeyStroke('2', Event.CTRL_MASK));
		menuItem2.addActionListener(new TabSelectionHandler(1));
		
		
		menu.add(menuItem1);
		menu.add(menuItem2);
		menuBar.add(menu);
		add(menuBar);
		 
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(950, 700);
		setVisible(true);
		setResizable(false);
	}
	
	public static void main(String[] args) {
		
		new MainUI();
	}
	
	class TabSelectionHandler implements ActionListener{

		private int selectedTab;
		
		public TabSelectionHandler(int selectedTab) {
			this.selectedTab = selectedTab;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			tabs.setSelectedIndex(selectedTab);
		}
	}
}
