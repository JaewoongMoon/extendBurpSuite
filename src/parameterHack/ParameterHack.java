package parameterHack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @ ParameterHack.java
 * 
 * Copyright 2016 NHN Techorus Corp. All rights Reserved. 
 * NHN Techorus PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/**
 * <pre>
 * 
 * ParameterHack.java 
 * </pre>
 *
 * @brief	: 
 * @author	: 문재웅(jwmoon@nhn-techorus.com)
 * @Date	: 2016. 9. 16.
 */
public class ParameterHack {

	private FuzzerType fuzzer = FuzzerType.NORMAL; // default
	
	public FuzzerType getFuzzer() {
		return fuzzer;
	}

	public void setFuzzer(FuzzerType fuzzer) {
		this.fuzzer = fuzzer;
	}

	public String makeFuzzerString(String src, String action, String value){
		
		// validation 체크 
		if (src == null || fuzzer == null){
			throw new IllegalArgumentException("please insert argument!");
		}
		String[] params = src.split("&");
		
		
		if(fuzzer.equals(FuzzerType.XSS)){
			return xssScript(params);
		}else if (fuzzer.equals(FuzzerType.XSAS)){
			return xsasScript(params);
		}else if (fuzzer.equals(FuzzerType.CSRF)){
			return postForm(params, action, value);
		}else if (fuzzer.equals(FuzzerType.SQL_INJECTION)){
			return normalType(params, sqlInjectionString());
		}else{
			return normalType(params, value);
		}
	}
	
	private String sqlInjectionString(){
		return "' union select 1, 2 from dual --";
	}
	
	/**
	 * @Method 	: normalType
	 * @brief	: 모든 파라메터의 값을 value로 세팅한다.
	 * @author	: 문재웅(jwmoon@nhn-techorus.com)
	 * @Date	: 2016. 10. 4.
	 * @param params
	 * @param value
	 * @return
	 */
	private String normalType(String[] params, String value){
		String result = "";
		for(int i=0; i < params.length; i++){
			String param = params[i];
			String[] params2 = param.split("=");
			String newParam = params2[0] + "=" + value;
			result = getResult(result, i, "&", newParam);
		}
		return result;
	}
	
	/**
	 * @Method 	: xsasScript
	 * @brief	: 액션스크립트 XSS 테스트 스크립트를 생성한다.
	 * @author	: 문재웅(jwmoon@nhn-techorus.com)
	 * @Date	: 2016. 10. 4.
	 * @param params
	 * @return
	 */
	private String xsasScript(String[] params){
		String xss1 = "<mx:Script><![CDATA[trace('***";
		String xss2 = "***');]]></mx:Script>";
		String result = "";
		for(int i=0; i < params.length; i++){
			String param = params[i];
			String[] params2 = param.split("=");
			String newParam = params2[0] + "=" + xss1 + (i+1) + xss2;
			result = getResult(result, i, "&", newParam);
		}
		return result;
	}
	
	/**
	 * @Method 	: xssScript
	 * @brief	: XSS 테스트 스크립트를 생성한다.
	 * @author	: 문재웅(jwmoon@nhn-techorus.com)
	 * @Date	: 2016. 10. 4.
	 * @param params
	 * @return
	 */
	private String xssScript(String[] params){
		String xss1 = "\"/><script>alert(";
		String xss2 = ");</script>";
		String result = "";
		
		for(int i=0; i < params.length; i++){
			String param = params[i];
			String[] params2 = param.split("=");
			String newParam = params2[0] + "=" + xss1 + (i+1) + xss2;
			result = getResult(result, i, "&", newParam);
		}
		return result;
	}
	
	
	
	/**
	 * @Method 	: postForm
	 * @brief	: 파라메터 배열로부터 HTML 폼을 생성한다. 
	 * @author	: 문재웅(jwmoon@nhn-techorus.com)
	 * @Date	: 2016. 10. 4.
	 * @param params : 파라메터 배열
	 * @param action : 포워딩할 액션 경로
	 * @param value : 폼의 파라메터 값으로 일괄 적용할 값
	 * @return 만들어진 HTML 폼 
	 */
	private String postForm(String[] params, String action, String value){
		String form = "<form name='csrfForm' action='" + action + "'>\r\n";
		String str1 = "<input type='hidden' name='";
		String str2 = "' value='";
		String str3 = "' />";
		String result = form;
		for(int i=0; i < params.length; i++){
			String param = params[i];
			String[] params2 = param.split("=");
			String newParam = str1 + params2[0] + "=" + str2 + value + str3; 
			result = getResult(result, i, "\r\n", newParam);
		}
		result += "\r\n</form>";
		return result;
	}
	
	
	/**
	 * @Method 	: getResult
	 * @brief	: 
	 * @author	: 문재웅(jwmoon@nhn-techorus.com)
	 * @Date	: 2016. 10. 4.
	 * @param result
	 * @param idx
	 * @param chain : 스트링을 연결할 연결자 값 
	 * @param param
	 * @return
	 */
	private String getResult(String result, int idx, String chain, String param){
		if(idx == 0){
			result = result + param;
		}else{
			result = result + chain + param;
		}
		return result;
	}
	
}
