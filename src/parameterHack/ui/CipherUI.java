/**
 * @ CipherUI.java 
 */
package parameterHack.ui;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * <pre>
 * parameterHack.ui
 * CipherUI.java 
 * </pre>
 *
 * @brief	: 
 * @author	: 문재웅(mjw8585@gmail.com)
 * @Date	: 2016. 10. 11.
 */
public class CipherUI extends JPanel{

	private JButton button1;
	
	public CipherUI(){
		setLayout(null);
		
		button1 = new JButton("버튼");
		button1.setBounds(10, 10, 200, 20);
		
		add(button1);
	}
}
