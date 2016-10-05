/**
 * @ FuzzerUI.java
 * 
 */
package parameterHack;

import java.awt.Choice;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
/**
 * <pre>
 * parameterHack
 * FuzzerUI.java 
 * </pre>
 *
 * @brief	: 
 * @author	: 문재웅(mjw8585@gmail.com)
 * @Date	: 2016. 10. 4.
 */
public class FuzzerUI extends JFrame implements ActionListener{

	   
   // Choice fuzzerBox = new Choice();
    JComboBox fuzzerBox;
    Vector<String> fuzzers;
    JTextArea input1;
    JTextArea output;
    JLabel label1;
    JLabel label2;
    JLabel label3;
    JLabel label4;
    JTextField userParamField;
    Vector dbmses;
    JComboBox dbmsBox;
    
    /** 
     *  생성자 
     */ 
    public FuzzerUI() {
    	super("Fuzzer String Maker Ver 1.1"); // 타이틀
    	
    	/************************************************************************/
    	/************************  컴포넌트 세팅  *****************************/
    	
    	// fuzzer 콤보 박스
    	fuzzers = new Vector<String>();
    	fuzzers.add("--SELECT--");
    	fuzzers.add(FuzzerType.NORMAL.toString());
    	fuzzers.add(FuzzerType.XSS.toString());
    	fuzzers.add("Actions Script XSS");
    	fuzzers.add(FuzzerType.CSRF.toString());
    	fuzzers.add(FuzzerType.SQL_INJECTION.toString());
    	
    	fuzzerBox = new JComboBox<>(fuzzers);
    	fuzzerBox.addActionListener(this);
    	
    	
    	// 파라메터 입력 텍스트 Input
    	input1 = new JTextArea(5, 10);
    	input1.setLineWrap(true);
    	JScrollPane inputPane1 = new JScrollPane(input1);
    	
    	
    	// 파라메터 출력 텍스트 Output
    	output = new JTextArea(5, 10);
    	output.setLineWrap(true);
    	JScrollPane outputPane = new JScrollPane(output);
    	
    	label1 = new JLabel("변환할 파라메터 문자열 (Input Parameter String)");
    	label2 = new JLabel("퍼저 타입 선택 (Fuzzer Type Select)");
    	label3 = new JLabel("출력 값 (Output)");
    	label4 = new JLabel("세팅할 파라메터 값(User Parameter Value)");
    	
    	// 유저가 입력하는 Param Value
    	userParamField = new JTextField();
    	
    	dbmses = new Vector<String>();
    	dbmses.add("--SELECT--");
    	for(DbmsType dbmsType : DbmsType.values()){
    		dbmses.add(dbmsType);
    	}
    	dbmsBox = new JComboBox<>(dbmses);
    	
    	
    	
    	/************************************************************************/
    	/**********************  컨테이너 view 세팅  **************************/
    	// 레이아웃 세팅
    	super.setLayout(null); 
    	setResizable(false);
    	
    	// 컴포넌트 위치 조정
    	label1.setBounds( 30, 30, 300, 20);
    	inputPane1.setBounds( 30, 60, 500, 200);
    	
    	label2.setBounds( 550, 30, 300, 20);
    	fuzzerBox.setBounds(550, 60, 150, 20);
    	dbmsBox.setBounds(720, 60, 150, 20);
    	
    	label3.setBounds( 30, 300, 300, 20);
    	outputPane.setBounds( 30, 330, 500, 200);
    	
    	label4.setBounds(550, 210, 300, 20);
    	userParamField.setBounds(550, 240, 250, 20);
    	userParamField.setEnabled(false);
    	userParamField.addActionListener(this);
    	
    	
    	// 컴포넌트 추가 
    	add(label1);
    	add(fuzzerBox);
    	add(dbmsBox);
    	add(label2);
    	add(inputPane1);
    	add(label3);
    	add(outputPane);
    	add(label4);
    	add(userParamField);
    	
    	// 크기 지정
    	setSize(950, 700);
    	
    	setVisible(true);
    	
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//System.out.println("**** 선택한 값: " + fuzzerBox.getSelectedItem());
	
		// STEP 0. 유저 파라메터 입력 부분 Visible 처리 
		if (fuzzerBox.getSelectedIndex() == 1){ // Normal 이면 보이게  
			userParamField.setEnabled(true);
		}else{
			userParamField.setEnabled(false);
			userParamField.setText("");
		}
		
		// STEP 1. 입력된 파라메터 값 얻어오기 
		// 값 체크 
		String inputParam = input1.getText();
		if(inputParam == null || inputParam.equals("")){
			JOptionPane.showMessageDialog(null, "입력 파라메터 값이 없습니다.");
			return ;
		}
		// (보완) 파라메터 형식에 맞는 지도 체크를 하면 좋을 듯
		//System.out.println(input1.getText());
		
		// STEP 2. 선택 된 Fuzzer Type 변환
		if (fuzzerBox.getSelectedIndex() == 0){
			JOptionPane.showMessageDialog(null, "퍼저 타입을 선택해 주십시오.");
			return ;
		}	
		FuzzerType fuzzer = FuzzerType.valueOf(fuzzerBox.getSelectedIndex());
		
		// STEP 3. ParameterHack 에게 변환 작업 요청
		ParameterHack paramHack = new ParameterHack();
		paramHack.setFuzzer(fuzzer);
		String paramValue = userParamField.getText() == null? "" : userParamField.getText();
		String result = paramHack.makeFuzzerString(inputParam, "", paramValue);
		
		// STEP 4. 결과 값 출력 
		output.setText(result);
		
	}

    public static void main(String[] args){
    	new FuzzerUI();
    }


}
