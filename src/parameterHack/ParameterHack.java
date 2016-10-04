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
	
	// 일회용. 아주 길게 연결된 view 파일 경로를 보기좋게 정렬한다. 
	private static void pathHack(){
		String src = "/WEB-INF/grails-app/views/systemAccount/_addTemplateField.gsp[gsp_webapp_systemAccount_addTemplateField_gsp]/WEB-INF/grails-app/views/adminUser/create.gsp=gsp_webapp_adminUsercreate_gsp_/WEB-INF/grails-app/views/adminReserve/list.gsp?gsp_webapp_adminReservelist_gspi/WEB-INF/grails-app/views/adminStore/rakutenEdit.gspIgsp_webapp_adminStorerakutenEdit_gspq/WEB-INF/grails-app/views/adminMasterCsvPattern/list.gspQgsp_webapp_adminMasterCsvPatternlist_gsp{/WEB-INF/grails-app/views/systemRecruitMallGenre/_options.gsp[gsp_webapp_systemRecruitMallGenre_options_gspu/WEB-INF/grails-app/views/systemYahooaucCategory/index.gspUgsp_webapp_systemYahooaucCategoryindex_gsp�/WEB-INF/plugins/mail-0.7.1/grails-app/views/_testemails/testhtml.gspAgsp_mail__testemailstesthtml_gspY/WEB-INF/grails-app/views/adminUser/edit.gsp9gsp_webapp_adminUseredit_gspo/WEB-INF/grails-app/views/adminAccountCategory/list.gspOgsp_webapp_adminAccountCategorylist_gsp{/WEB-INF/grails-app/views/systemYahooCategory/_categories.gsp[gsp_webapp_systemYahooCategory_categories_gspu/WEB-INF/grails-app/views/systemRecruitMallGenre/index.gspUgsp_webapp_systemRecruitMallGenreindex_gspo/WEB-INF/grails-app/views/adminTemplate/ecbeingEdit.gspOgsp_webapp_adminTemplateecbeingEdit_gspY/WEB-INF/grails-app/views/adminUser/list.gsp9gsp_webapp_adminUserlist_gsp{/WEB-INF/grails-app/views/systemRakutenDirectory/_options.gsp[gsp_webapp_systemRakutenDirectory_options_gsps/WEB-INF/grails-app/views/systemBiddersCategory/index.gspSgsp_webapp_systemBiddersCategoryindex_gsp_/WEB-INF/grails-app/views/adminItem/_upload.gsp?gsp_webapp_adminItem_upload_gspe/WEB-INF/grails-app/views/adminMasterCsv/index.gspEgsp_webapp_adminMasterCsvindex_gspw/WEB-INF/grails-app/views/adminAccountCategoryCsv/index.gspWgsp_webapp_adminAccountCategoryCsvindex_gspU/WEB-INF/grails-app/views/layouts/main.gsp5gsp_webapp_layoutsmain_gspW/WEB-INF/grails-app/views/layouts/admin.gsp7gsp_webapp_layoutsadmin_gsp]/WEB-INF/grails-app/views/systemStore/edit.gsp=gsp_webapp_systemStoreedit_gsp�	/WEB-INF/plugins/mail-0.7.1/grails-app/views/_testemails/tagtest.gsp?gsp_mail__testemailstagtest_gsp]/WEB-INF/grails-app/views/systemStore/show.gsp=gsp_webapp_systemStoreshow_gspi/WEB-INF/grails-app/views/systemJobServer/create.gspIgsp_webapp_systemJobServercreate_gspw/WEB-INF/grails-app/views/adminAccountCategory/bulkEdit.gspWgsp_webapp_adminAccountCategorybulkEdit_gspQ/WEB-INF/grails-app/views/admin/navi.gsp1gsp_webapp_adminnavi_gsp{/WEB-INF/grails-app/views/adminMasterCsv/setOutputColumns.gsp[gsp_webapp_adminMasterCsvsetOutputColumns_gsps/WEB-INF/grails-app/views/adminAccountCategory/stores.gspSgsp_webapp_adminAccountCategorystores_gsp�/WEB-INF/plugins/mail-0.7.1/grails-app/views/_testemails/test.gsp9gsp_mail__testemailstest_gspc/WEB-INF/grails-app/views/adminReserve/create.gspCgsp_webapp_adminReservecreate_gsp{/WEB-INF/grails-app/views/systemSavawayMallCategory/index.gsp[gsp_webapp_systemSavawayMallCategoryindex_gspa/WEB-INF/grails-app/views/systemAccount/list.gspAgsp_webapp_systemAccountlist_gspc/WEB-INF/grails-app/views/adminActivity/index.gspCgsp_webapp_adminActivityindex_gspG/WEB-INF/grails-app/views/index.gsp'gsp_webappindex_gspu/WEB-INF/grails-app/views/systemRakutenDirectory/index.gspUgsp_webapp_systemRakutenDirectoryindex_gsp�-/WEB-INF/plugins/database-migration-1.3.3/grails-app/views/dbdoc/_overview-summary.gspagsp_databaseMigration_dbdoc_overview_summary_gsp�/WEB-INF/grails-app/views/systemWmMaster/_giftCategoryOptions.gspcgsp_webapp_systemWmMaster_giftCategoryOptions_gsps/WEB-INF/grails-app/views/adminDefaultFieldValue/edit.gspSgsp_webapp_adminDefaultFieldValueedit_gsp{/WEB-INF/grails-app/views/adminItemImage/_unsetImageCount.gsp[gsp_webapp_adminItemImage_unsetImageCount_gspe/WEB-INF/grails-app/views/systemAccount/create.gspEgsp_webapp_systemAccountcreate_gspU/WEB-INF/grails-app/views/system/index.gsp5gsp_webapp_systemindex_gspo/WEB-INF/grails-app/views/systemUploadJobQueue/show.gspOgsp_webapp_systemUploadJobQueueshow_gsp_/WEB-INF/grails-app/views/adminStore/wmEdit.gsp?gsp_webapp_adminStorewmEdit_gspa/WEB-INF/grails-app/views/systemStore/create.gspAgsp_webapp_systemStorecreate_gspk/WEB-INF/grails-app/views/systemAccount/_addField.gspKgsp_webapp_systemAccount_addField_gspa/WEB-INF/grails-app/views/adminTemplate/edit.gspAgsp_webapp_adminTemplateedit_gspc/WEB-INF/grails-app/views/systemJobQueue/list.gspCgsp_webapp_systemJobQueuelist_gsps/WEB-INF/grails-app/views/systemWmMaster/_sizeOptions.gspSgsp_webapp_systemWmMaster_sizeOptions_gspY/WEB-INF/grails-app/views/adminUser/show.gsp9gsp_webapp_adminUsershow_gspk/WEB-INF/grails-app/views/systemWmMaster/_options.gspKgsp_webapp_systemWmMaster_options_gsp�/WEB-INF/plugins/database-migration-1.3.3/grails-app/views/dbdoc/_index.gspKgsp_databaseMigration_dbdoc_index_gspe/WEB-INF/grails-app/views/systemJobServer/edit.gspEgsp_webapp_systemJobServeredit_gsp/WEB-INF/grails-app/views/systemBiddersCategory/_categories.gsp_gsp_webapp_systemBiddersCategory_categories_gspe/WEB-INF/grails-app/views/systemJobServer/list.gspEgsp_webapp_systemJobServerlist_gsp[/WEB-INF/grails-app/views/systemLogs/list.gsp;gsp_webapp_systemLogslist_gspY/WEB-INF/grails-app/views/layouts/admin5.gsp9gsp_webapp_layoutsadmin5_gspa/WEB-INF/grails-app/views/adminStore/mdcEdit.gspAgsp_webapp_adminStoremdcEdit_gspG/WEB-INF/grails-app/views/error.gsp'gsp_webapperror_gspi/WEB-INF/grails-app/views/adminActivityCsv/index.gspIgsp_webapp_adminActivityCsvindex_gspk/WEB-INF/grails-app/views/systemWmMaster/category.gspKgsp_webapp_systemWmMastercategory_gsp�/WEB-INF/plugins/database-migration-1.3.3/grails-app/views/dbdoc/_globalnav.gspSgsp_databaseMigration_dbdoc_globalnav_gsp]/WEB-INF/grails-app/views/systemShard/list.gsp=gsp_webapp_systemShardlist_gsp]/WEB-INF/grails-app/views/systemStore/list.gsp=gsp_webapp_systemStorelist_gspm/WEB-INF/grails-app/views/systemAccount/editFields.gspMgsp_webapp_systemAccounteditFields_gspe/WEB-INF/grails-app/views/systemJobServer/show.gspEgsp_webapp_systemJobServershow_gspc/WEB-INF/grails-app/views/adminItemImage/edit.gspCgsp_webapp_adminItemImageedit_gspW/WEB-INF/grails-app/views/adminItem/csv.gsp7gsp_webapp_adminItemcsv_gspm/WEB-INF/grails-app/views/systemAccount/_editField.gspMgsp_webapp_systemAccount_editField_gspo/WEB-INF/grails-app/views/systemUploadJobQueue/edit.gspOgsp_webapp_systemUploadJobQueueedit_gsp�!/WEB-INF/plugins/database-migration-1.3.3/grails-app/views/dbdoc/_stylesheet.gspUgsp_databaseMigration_dbdoc_stylesheet_gspo/WEB-INF/grails-app/views/adminAccountCategory/edit.gspOgsp_webapp_adminAccountCategoryedit_gsp}/WEB-INF/grails-app/views/systemAccount/_editTemplateField.gsp]gsp_webapp_systemAccount_editTemplateField_gsps/WEB-INF/grails-app/views/systemWmMaster/giftCategory.gspSgsp_webapp_systemWmMastergiftCategory_gspa/WEB-INF/grails-app/views/systemAccount/show.gspAgsp_webapp_systemAccountshow_gspS/WEB-INF/grails-app/views/admin/login.gsp3gsp_webapp_adminlogin_gspk/WEB-INF/grails-app/views/adminStore/corekagoEdit.gspKgsp_webapp_adminStorecorekagoEdit_gspq/WEB-INF/grails-app/views/adminDistributionCsv/index.gspQgsp_webapp_adminDistributionCsvindex_gspq/WEB-INF/grails-app/views/adminMasterCsvPattern/edit.gspQgsp_webapp_adminMasterCsvPatternedit_gspq/WEB-INF/grails-app/views/systemCroozDirectory/index.gspQgsp_webapp_systemCroozDirectoryindex_gspi/WEB-INF/grails-app/views/adminStore/recruitEdit.gspIgsp_webapp_adminStorerecruitEdit_gspg/WEB-INF/grails-app/views/adminStore/yamadaEdit.gspGgsp_webapp_adminStoreyamadaEdit_gspu/WEB-INF/grails-app/views/adminMasterCsvPattern/create.gspUgsp_webapp_adminMasterCsvPatterncreate_gspc/WEB-INF/grails-app/views/systemWmMaster/size.gspCgsp_webapp_systemWmMastersize_gspk/WEB-INF/grails-app/views/adminStore/yahooaucEdit.gspKgsp_webapp_adminStoreyahooaucEdit_gspc/WEB-INF/grails-app/views/systemJobQueue/edit.gspCgsp_webapp_systemJobQueueedit_gsps/WEB-INF/grails-app/views/adminAccountCategory/create.gspSgsp_webapp_adminAccountCategorycreate_gspy/WEB-INF/grails-app/views/systemAccount/confirmDeleteAll.gspYgsp_webapp_systemAccountconfirmDeleteAll_gspw/WEB-INF/grails-app/views/systemCroozDirectory/_options.gspWgsp_webapp_systemCroozDirectory_options_gspc/WEB-INF/grails-app/views/systemJobQueue/show.gspCgsp_webapp_systemJobQueueshow_gsp�/WEB-INF/grails-app/views/systemSavawayMallCategory/_options.gspagsp_webapp_systemSavawayMallCategory_options_gspe/WEB-INF/grails-app/views/systemWmMaster/brand.gspEgsp_webapp_systemWmMasterbrand_gspk/WEB-INF/grails-app/views/adminCustom/officeDepot.gspKgsp_webapp_adminCustomofficeDepot_gspi/WEB-INF/grails-app/views/adminStore/biddersEdit.gspIgsp_webapp_adminStorebiddersEdit_gspm/WEB-INF/grails-app/views/systemWmMaster/character.gspMgsp_webapp_systemWmMastercharacter_gspg/WEB-INF/grails-app/views/adminStore/amazonEdit.gspGgsp_webapp_adminStoreamazonEdit_gspe/WEB-INF/grails-app/views/adminStore/croozEdit.gspEgsp_webapp_adminStorecroozEdit_gspo/WEB-INF/grails-app/views/systemYahooCategory/index.gspOgsp_webapp_systemYahooCategoryindex_gspe/WEB-INF/grails-app/views/adminStore/yahooEdit.gspEgsp_webapp_adminStoreyahooEdit_gspk/WEB-INF/grails-app/views/adminTemplate/parcoEdit.gspKgsp_webapp_adminTemplateparcoEdit_gspi/WEB-INF/grails-app/views/systemLogs/invalidList.gspIgsp_webapp_systemLogsinvalidList_gspo/WEB-INF/grails-app/views/systemUploadJobQueue/list.gspOgsp_webapp_systemUploadJobQueuelist_gsp_/WEB-INF/grails-app/views/error/serverError.gsp?gsp_webapp_errorserverError_gspW/WEB-INF/grails-app/views/welcome/index.gsp7gsp_webapp_welcomeindex_gspq/WEB-INF/grails-app/views/systemYahooCategory/_specs.gspQgsp_webapp_systemYahooCategory_specs_gsp_/WEB-INF/grails-app/views/error/clientError.gsp?gsp_webapp_errorclientError_gspa/WEB-INF/grails-app/views/systemAccount/edit.gspAgsp_webapp_systemAccountedit_gsp{/WEB-INF/grails-app/views/systemYahooaucCategory/_options.gsp[gsp_webapp_systemYahooaucCategory_options_gspS/WEB-INF/grails-app/views/admin/index.gsp3gsp_webapp_adminindex_gspQ/WEB-INF/grails-app/views/user/login.gsp1gsp_webapp_userlogin_gspk/WEB-INF/grails-app/views/systemShard/editDefault.gspKgsp_webapp_systemShardeditDefault_gspreloadEnabled�";
		String[] list = src.split("");
		//System.out.println(list.length);
		List<String> result = new ArrayList<String>();
		
		for(int i=0; i < list.length; i++){
			String item = list[i];
			int idx = item.indexOf("/");
			//System.out.println(item + "," + idx);
			if( idx > -1){
				//System.out.println(item.substring(idx, item.length()));
				result.add(item.substring(idx, item.length()));
			}
		}
		Collections.sort(result);
		//result.
		for(String item : result){
			System.out.println(item);
		}
		//System.out.println(result.size());
		//result.
	}
	
}
