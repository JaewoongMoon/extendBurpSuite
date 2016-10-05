/**
 * @ FuzzerType.java
 * 
 */
package parameterHack;

/**
 * <pre>
 * parameterHack
 * FuzzerType.java 
 * </pre>
 *
 * @brief	: 
 * @author	: 문재웅(mjw8585@gmail.com)
 * @Date	: 2016. 10. 4.
 */
public enum FuzzerType {

	NORMAL(1), // 기본
	XSS(2),  // Cross Site Script
	XSAS(3), // Cross Site Action Script
	CSRF(4), // Cross Site Request Forgery
	SQL_INJECTION(5)
	;
	
	private final int value;
	
	FuzzerType(int value){
		this.value = value;
	}
	
	public int intValue(){
		return value;
	}
	
	public static FuzzerType valueOf(int value){
		switch(value){
		case 1: return NORMAL;
		case 2: return XSS;
		case 3: return XSAS;
		case 4: return CSRF;
		case 5: return SQL_INJECTION;
		default : throw new AssertionError("Unknown value : " +value);
		}
	}
}
