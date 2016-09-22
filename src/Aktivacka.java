import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;

import static junit.framework.Assert.fail;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.JavascriptExecutor;

/** 
 * Class Aktivacka automatically goes through activation process based on 
 * criteria specified in external excel sheet.
 */       
public class Aktivacka {
    
    private static Map<String, String> testParameters;
    
    public static void main(String[] args) throws InterruptedException, ClassNotFoundException, SQLException, IOException {
    
        int testResult = 0;
        //Map<String, String> testParameters = null;
        
        testParameters = loadParameters(); 
    	System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        WebElement pageTemplate = null;

        
        //String baseUrl = "http://rztvnode029.cz.tmo:7777";
        //driver.get(baseUrl + "/ecommunications_csy/start.swe?SWECmd=ExecuteLogin&SWEUserName=extsedam&SWEPassword=init1234");
        
        String baseUrl = "C:\\Users\\matej.seda\\OneDrive - Accenture\\Selenium\\javascriptloader.html";
        //String baseUrl = "src\\main\\resources\\javascriptloader.html";
        String dbHash = createURL();
        System.out.println(baseUrl+"?dbhash=s002"+dbHash);
        driver.get(baseUrl+"?dbhash=s002"+dbHash);
        waitForElementById(driver, "symbUrlIFrame1");
        driver.switchTo().frame(driver.findElement(By.xpath("//*[@id=\"symbUrlIFrame1\"]")));
        pageTemplate = driver.findElement(By.id("pageTemplate"));
        
        while(true){
            try {pageTemplate = driver.findElement(By.id("pageTemplate"));} catch (NoSuchElementException e) {} 
  
            analyzePage(driver, testParameters);

            try {pageTemplate = driver.findElement(By.id("pageTemplate")); 
            System.out.println("Template = "+pageTemplate.getAttribute("value"));
            System.out.println("Template = "+executeButton(pageTemplate.getAttribute("value"), testParameters));
            } catch (NoSuchElementException e) {}

            try {pageTemplate = driver.findElement(By.id("pageTemplate"));} catch (NoSuchElementException e) {continue;}
            if(pageTemplate.getAttribute("value").equalsIgnoreCase("order_confirmation")){break;};   
            
            try {driver.findElement(By.partialLinkText(executeButton(pageTemplate.getAttribute("value"), testParameters))).click(); } catch (NoSuchElementException e) {break;} 
            System.out.println("ORDER: "+testParameters.get("orderId"));
            Thread.sleep(500);       
        }

        /*
        testResult += ocvCheckVas( "SUB2000", "subscriber_id", null , testParameters);
        testResult += comCheckVas( "SUB2000", "subscriber_id", null , testParameters);
        testResult += prodCheckVas("SUB2000", "subscriber_id", null , testParameters);
        System.out.println("Vysledek = "+testResult);
        */
        
    }
    
    private static void waitForElementById(WebDriver driver, String elementId) throws InterruptedException {
        for (int second = 0;; second++) {
            if (second >= 60) fail("timeout");
            try { if (driver.findElement(By.id(elementId)).isDisplayed()) break; }
            catch (Exception e) {}
            Thread.sleep(500);
        }
    }
    
    
    public static String getICCID(String environment, String outletId) throws ClassNotFoundException, SQLException {
        System.out.println("Outlet: " + outletId);
        String iccid = null;
        Connection conn = connectToDb(environment);
        Statement stmt = conn.createStatement();
        String query = "SELECT iccid "
                + "FROM sy_rst.rst_sim_storage_media a,sy_rst.rst_sim_electrical_profiles b,sy_rst.rst_sim_types c "
                + "WHERE a.rstep_rstep_id = b.rstep_id "
                + "AND a.status = 'r' "
                + "AND a.req_id IS NULL "
                + "AND a.f_logasl_id IS  NULL "
                + "AND a.lock_req_id IS  NULL "
                + "AND b.rstty_sim_type = c.sim_type "
                + "AND a.DCDSEH_DCDSEH_ID='" + outletId + "' "
                + "AND rownum < 2 ";
        System.out.println("QUERY: " + query);
        
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            iccid = rs.getString("iccid");
            //System.out.println("IMEI: "+iccid);
        }
        stmt.close();
        conn.close();
        return iccid;
    }
    
    public static String createURL() throws ClassNotFoundException, SQLException, IOException {
        Map <String, String> parameters = loadParameters();
        System.out.println(parameters);

        String hash = "";
        Connection conn = connectToDb("DSBL01");
        Statement stmt = conn.createStatement();;
        DbmsOutput dbmsOutput = new DbmsOutput(conn);

        dbmsOutput.enable(10000000);
        stmt.execute("declare abc VARCHAR2(32767); das VARCHAR2(32767); rwe INT; BEGIN "
                + "rwe := siebel.TST_VCCNG_CONVERT_SBLR3('Brand Shop',"
                + "'7',"
                + "'6005000263',"
                + "'2',"
                + "'9999999999',"
                + "'EXTSEDAM',"
                + "'init1234',"
                + "'CZ',"
                + "'',"
                + "'orderingContext=>^^processName=>^^productInstanceOperation=>^^operatorRole=>"+parameters.get("roles")+"^^sblPositionId=>0-5220^^customerId=>"+parameters.get("customerId")+"^^segmentId=>^^contactHistoryId=>^^employeeId=>5050^^basketId=>^^outletId=>"+parameters.get("outletId")+"^^outletCode=>"+parameters.get("outletCode")+"^^outletSAPCode=>"+parameters.get("outletSAPCode")+"^^billingProfileId=>"+parameters.get("billingProfileId")+"^^"
                +"productOfferingId=>"+parameters.get("productOfferingId")+"^^"
                +"hierarchyLevel=>^^hierarchyId=>^^campaignIds=>^^rtdId=>^^productInstanceId=>^^dateTo=>^^svcRequestId=>^^relevantDate=>^^"
                +"offeringCatalogueRel=>"+parameters.get("offeringCatalogueRel")+"^^"
                +"voucherId=>^^customerType=>B2C - Prepaid^^customerNumber=>9999999999',"
                + "abc, das);"
                + "end;");

        stmt.close();

        //dbmsOutput.show();
        hash = dbmsOutput.getHash();
        System.out.println(hash);
        dbmsOutput.close();
        conn.close();
        return hash;
    }
    
    private static Map<String, String> loadParameters() throws FileNotFoundException, IOException{
        Map <String, String> parameters = new HashMap<String, String>(); 
        String excelPath = "src\\main\\resources\\input.xlsx";
        FileInputStream inputStream = new FileInputStream(new File(excelPath));
         
        Workbook workbook = new XSSFWorkbook(inputStream);
        org.apache.poi.ss.usermodel.Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = firstSheet.iterator();
        String[] field = new String [2];
        String roles = "";
        Boolean	roleStarted = false;
        Boolean	roleFirst = true;
        String s = "";

        /* Default values, will be upserted if inputed in excel */
    	parameters.put("productOfferingId", "PO0661");
    	parameters.put("offeringCatalogueRel", "CAT_BSHAxPO0661xcompensation0");
    	parameters.put("role", "SBL_UPC_Brand Shop|SBL_UPC_Contractual Relationship Admin");
    	parameters.put("customerId", "9999999999");
    	parameters.put("outletId", "15445");
    	parameters.put("outletCode", "PGSA7.001.001");
    	parameters.put("billingProfileId", "");
    	parameters.put("outletSAPCode", "S-PGSA7");
    	parameters.put("environment", "2");
    	
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            int i = 0;
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                if(cell.getCellType() == 0){
                	s=String.valueOf(Math.round(Math.floor(cell.getNumericCellValue())));
                }
                else{
                	s=cell.getStringCellValue();
                }
                	
                if (i==0){
                    field[0] = s;
                }
                
                if (i==1){
                    field[1] = s;

                }
        
                if(s.contentEquals("role")){
                	roleStarted = true;
                }
           
                if(i==1 && roleStarted==true && roleFirst==false){
                	roles += "|"+field[1];
                }
                
                if(i==1 && roleStarted==true && roleFirst==true){
                	roles = field[1];
                    roleFirst = false;
                }
                
                if(i==0 && roleStarted==true && !s.contentEquals("role")){
                	parameters.put("roles", roles);
                    roles = "";
                    roleStarted = false;
                    roleFirst = true;
                }

                i++;
            }
            if(roleStarted == false){
            	parameters.put(field[0], field[1]);
            }
            
        }
        
        if(roleStarted==true){
        	parameters.put("roles", roles);
            roles = "";
            roleStarted = false;
            roleFirst = true;
        }
        
        /*
         	while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            int i = 0;
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                if (i==0){
                    field[0] = cell.getStringCellValue();
                }
                else{
                    field[1] = cell.getStringCellValue();
                }
                i++;
            }
            parameters.put(field[0], field[1]);
        }
        */ 
            
         	for (Map.Entry<String, String> entry : parameters.entrySet())
         	{
         	    System.out.println(entry.getKey() + "/" + entry.getValue());
         	}
            
        workbook.close();
        inputStream.close();
        
        return parameters;
    }
    
    public static Connection connectToDb(String DbName) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        String URL = "";
        Properties info = null;
        switch (DbName) {
            case "DSBL01":
                URL = "jdbc:oracle:thin:@rztvnode071.cz.tmo:1523/DSBL01.WORLD";
                info = new Properties();
                info.put("user", "EXTKVASNICAT");
                info.put("password", "Zkou3en9");
                conn = DriverManager.getConnection(URL, info);
                break;

            case "Q2APP01":
                URL = "jdbc:oracle:thin:@hkpowm03.cz.tmo:1684/Q2AP01.WORLD";
                info = new Properties();
                info.put("user", "apptest");
                info.put("password", "ittc");
                conn = DriverManager.getConnection(URL, info);
                break;

            case "QAPP01":
                URL = "jdbc:oracle:thin:@rztpowb06.cz.tmo:1584/QAP01.world";
                info = new Properties();
                info.put("user", "apptest");
                info.put("password", "ittc");
                conn = DriverManager.getConnection(URL, info);
                break;

            case "Q2COM01":
                URL = "jdbc:oracle:thin:@rztvnode005.cz.tmo:1525/Q2COM01.WORLD";
                info = new Properties();
                info.put("user", "extvicarp");
                info.put("password", "inIT1234");
                conn = DriverManager.getConnection(URL, info);
                break;
                
            case "QCOM01":
                URL = "jdbc:oracle:thin:@rztvnode026.cz.tmo:1525/QCOM01.WORLD";
                info = new Properties();
                info.put("user", "COM_TEST");
                info.put("password", "dcfng2vr");
                conn = DriverManager.getConnection(URL, info);
                break;

            default:
                break;
        }

        return conn;
    }
    
    public static String getParameter(String paramName, Map<String, String> paramMap) throws ClassNotFoundException, SQLException {
    	String paramResult = "";
    	String SiebelDB = "Q2SBL01";
    	String AppDB = "Q2APP01";
    	String ComDB = "Q2COM01"; 
    	
    	if(paramMap.containsKey("environment")){
        	if(paramMap.get("environment").equals("1")){
            	SiebelDB = "QSBL01";
            	AppDB = "QAPP01";
            	ComDB = "QCOM01"; 
        	}
    	}
    	        	
        switch (paramName) {
            case "iccid":
            	String RSTOuletId = "15444";
            	if(paramMap.containsKey("RSTOuletId")){
            		RSTOuletId = paramMap.get("RSTOuletId");
            	}
            	paramResult = getICCID(AppDB, RSTOuletId);
                break;

            case "sapCode":
            	String sapCode = "1216";
            	if(paramMap.containsKey("sapCode")){
            		sapCode = paramMap.get("sapCode");
            	}
            	paramResult = sapCode;
                break;

            case "inputContactName":
            	String inputContactName = "automated";
            	if(paramMap.containsKey("inputContactName")){
            		inputContactName = paramMap.get("inputContactName");
            	}
            	paramResult = inputContactName;
                break;

            case "inputContactSurname":
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_mm");
                Calendar cal = Calendar.getInstance();
            	String inputContactSurname = dateFormat.format(cal.getTime());
            	if(paramMap.containsKey("inputContactSurname")){
            		inputContactSurname = paramMap.get("inputContactSurname");
            	}
            	paramResult = inputContactSurname;
                break;

            case "inputIdentificationNumber":
                String inputIdentificationNumber = RcGenerator.generateRcForAge(18, 40);
                inputIdentificationNumber = inputIdentificationNumber.replace("/","");
            	if(paramMap.containsKey("inputIdentificationNumber")){
            		inputIdentificationNumber = paramMap.get("inputIdentificationNumber");
            	}
            	paramResult = inputIdentificationNumber;
                break;

            case "inputDocumentNumber1":
                String inputDocumentNumber1 = RcGenerator.generateRcForAge(18, 40);
                inputDocumentNumber1 = inputDocumentNumber1.replace("/","");
                inputDocumentNumber1 = inputDocumentNumber1.substring(0, 8);
            	if(paramMap.containsKey("inputDocumentNumber1")){
            		inputDocumentNumber1 = paramMap.get("inputDocumentNumber1");
            	}
            	paramResult = inputDocumentNumber1;
                break;

            case "inputCity":
                String inputCity = "Praha (Hlavní­ město Praha)";
            	if(paramMap.containsKey("inputCity")){
            		inputCity = paramMap.get("inputCity");
            	}
            	paramResult = inputCity;
                break;

            case "inputStreet":
                String inputStreet = "Tomíčkova";
            	if(paramMap.containsKey("inputStreet")){
            		inputStreet = paramMap.get("inputStreet");
            	}
            	paramResult = inputStreet;
                break;

            case "inputHouseOrNumber":
                String inputHouseOrNumber = "2144/1";
            	if(paramMap.containsKey("inputHouseOrNumber")){
            		inputHouseOrNumber = paramMap.get("inputHouseOrNumber");
            	}
            	paramResult = inputHouseOrNumber;
                break;

            case "inputZip":
                String inputZip = "148 00";
            	if(paramMap.containsKey("inputZip")){
            		inputZip = paramMap.get("inputZip");
            	}
            	paramResult = inputZip;
                break;
                
                
                
            default:
                break;
        }


        
        

        return paramResult;
    }

    
    public static int ocvCheckVas(String vasName, String vasParam, String vasParamValue, Map<String, String> paramMap) throws ClassNotFoundException, SQLException {    	
    	int checkResult = 0; 
    	int dbResult = 0;
    	String ComDB = "Q2COM01";
    	if(paramMap.get("environment").equals("1")){
        	ComDB = "QCOM01"; 
    	}

        Connection conn = connectToDb(ComDB);
        Statement stmt = conn.createStatement();
        String query = "select COUNT(*) as value "+
		"from "+ 
		"OCV_REPO.BUSN_INTER_ITEM bi, "+
		"OCV_REPO.PROD p, "+
		"OCV_REPO.PROD_CHAR_VAL pcv "+
		"where 1=1 "+
		"and p.ident=bi.prod_ident "+ 
		"and p.ident=pcv.prod_ident "+ 
		"and p.PROD_NUM = '"+vasName+"' "+ 
		"and bi.BUSN_INTER_IDENT = '"+paramMap.get("orderId")+"' ";
        
		if(!vasParam.equalsIgnoreCase(null)){
			query+="and pcv.PROD_SPEC_CHAR_IDENT = '"+vasParam+"'";
		} 
		
		if(!vasParamValue.equalsIgnoreCase(null)){
			query+="and pcv.VAL = '"+vasParamValue+"'";
		}

        System.out.println("QUERY: " + query);

        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
        	dbResult = rs.getInt("value");
            System.out.println("IMEI: "+dbResult);
        }
        
        if(dbResult > 0){
        	checkResult = 0;
        } else {
        	checkResult = 1;
        	System.out.println("Specification "+vasName+" with parameter '"+vasParam+"' and value '"+vasParamValue+"' not found in OCV_REPO for order '"+paramMap.get("orderId")+"'");
        }
        
        stmt.close();
        conn.close();      
        
        return checkResult;
	}

    
    public static int comCheckVas(String vasName, String vasParam, String vasParamValue, Map<String, String> paramMap) throws ClassNotFoundException, SQLException {    	
    	int checkResult = 0; 
    	int dbResult = 0;
    	String ComDB = "Q2COM01";
    	if(paramMap.get("environment").equals("1")){
        	ComDB = "QCOM01"; 
    	}

        Connection conn = connectToDb(ComDB);
        Statement stmt = conn.createStatement();
        String query = "select COUNT(*) as value "+
		"from "+ 
		"COM_REPO.BUSN_INTER_ITEM bi, "+
		"COM_REPO.PROD p, "+
		"COM_REPO.PROD_CHAR_VAL pcv "+
		"where 1=1 "+
		"and p.ident=bi.prod_ident "+ 
		"and p.ident=pcv.prod_ident "+ 
		"and p.PROD_NUM = '"+vasName+"' "+ 
		"and bi.BUSN_INTER_IDENT = '"+paramMap.get("orderId")+"' ";
        
		if(!vasParam.equalsIgnoreCase(null)){
			query+="and pcv.PROD_SPEC_CHAR_IDENT = '"+vasParam+"'";
		} 
		
		if(!vasParamValue.equalsIgnoreCase(null)){
			query+="and pcv.VAL = '"+vasParamValue+"'";
		}

        System.out.println("QUERY: " + query);

        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
        	dbResult = rs.getInt("value");
            System.out.println("IMEI: "+dbResult);
        }
        
        if(dbResult > 0){
        	checkResult = 0;
        } else {
        	checkResult = 1;
        	System.out.println("Specification "+vasName+" with parameter '"+vasParam+"' and value '"+vasParamValue+"' not found in COM_REPO for order '"+paramMap.get("orderId")+"'");
        }
        
        stmt.close();
        conn.close();      
        
        return checkResult;
	}

    
    public static int prodCheckVas(String vasName, String vasParam, String vasParamValue, Map<String, String> paramMap) throws ClassNotFoundException, SQLException {    	
    	int checkResult = 0; 
    	int dbResult = 0;
    	String ComDB = "Q2COM01";
    	if(paramMap.get("environment").equals("1")){
        	ComDB = "QCOM01"; 
    	}

        Connection conn = connectToDb(ComDB);
        Statement stmt = conn.createStatement();

        
        String query = "SELECT COUNT(*) as value from prod_repo.prod_char_val al, prod_repo.prod od, upc_repo.prod_spec pse where al.prod_ident=od.ident and pse.prod_num=od.prod_num and pse.prod_num = '"+vasName+"' ";


		if(!vasParam.equalsIgnoreCase(null)){
			query+=" and al.prod_spec_char_ident = '"+vasParam+"' ";
		} 
		
		if(!vasParamValue.equalsIgnoreCase(null)){
			query+=" and al.val = '"+vasParamValue+"' ";
		}

        query+="and al.prod_ident in "
	        + "(select vas.ident from prod_repo.prod vas, prod_repo.prod_char_val vasch where vasch.prod_ident=vas.ident and vasch.prod_spec_char_ident='subscriber_id' and vasch.val in "
	        + "(select pchvs.val from prod_repo.prod_char_val pchv, prod_repo.prod_char_val pchvs where pchv.prod_ident=pchvs.prod_ident and od.status = 'active' and pchv.val IN "
	        + "(select distinct pcv.val  from COM_REPO.BUSN_INTER_ITEM bi, COM_REPO.PROD p, COM_REPO.PROD_CHAR_VAL pcv where 1=1 "
	        + "and p.ident=bi.prod_ident and p.ident=pcv.prod_ident and bi.BUSN_INTER_IDENT = "+paramMap.get("orderId")+"and pcv.PROD_SPEC_CHAR_IDENT = 'subscriber_id'))) ";

        System.out.println("QUERY: " + query);

        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
        	dbResult = rs.getInt("value");
            System.out.println("IMEI: "+dbResult);
        }
        
        if(dbResult > 0){
        	checkResult = 0;
        } else {
        	checkResult = 1;
        	System.out.println("Specification "+vasName+" with parameter '"+vasParam+"' and value '"+vasParamValue+"' not found in PROD_REPO for order '"+paramMap.get("orderId")+"'");
        }
        
        stmt.close();
        conn.close();      
        
        return checkResult;
	}
    
    
    public static String executeButton(String pageTemplate, Map<String, String> testParameters) throws ClassNotFoundException, SQLException {    	
    	String executeResult = ""; 
		executeResult = "Pokračovat";
    	try{
    		executeResult = testParameters.get("pageTemplate");
    	} catch (NullPointerException e) {
    		executeResult = "Pokračovat";
            return executeResult;
    	}    
    	
    	if(executeResult==null){executeResult = "Pokračovat";}
        
        return executeResult;
	}
    
    public static void analyzePage(WebDriver driver, Map<String, String> paramMap) throws ClassNotFoundException, SQLException, InterruptedException{
        /** Choose MSISDN **/
        try {driver.findElement(By.xpath("//input[@id = 'radio']/..")).click();} catch (NoSuchElementException e) {} 

        /** ICCID **/
        try {driver.findElement(By.name("iccid")).sendKeys(getParameter("iccid", paramMap));    } catch (NoSuchElementException e) {} 
        try {driver.findElement(By.name("sapCode")).sendKeys(getParameter("sapCode", paramMap));} catch (NoSuchElementException e) {} 
        //sim_input


        /** Order Checkout **/        
        try{
            List<WebElement> allElements = driver.findElements(By.xpath("//table[@class='table-low-lines']")); 
            String s = "";
            String orderId = "";
            for (WebElement el : allElements) {
                  s = el.getText();
                  if (s.indexOf("Přehled objednávky ") > -1) {
                          orderId = s.substring(19, s.indexOf("(") -1);
                          testParameters.put("orderId", orderId);
                  }
            }
        } catch (NoSuchElementException e) {} 
        try {driver.findElement(By.cssSelector("a[onclick*='sendContent();return false;']")).click();} catch (NoSuchElementException e) {} 

        /** Customer info **/
        try {driver.findElement(By.id("inputContactName")).sendKeys(getParameter("inputContactName", paramMap));} catch (NoSuchElementException e) {} 
        try {driver.findElement(By.id("inputContactSurname")).sendKeys(getParameter("inputContactSurname", paramMap));} catch (NoSuchElementException e) {} 

        /* MUSIME VOSALIT NEJAK*/
        try {
            /*
            driver.findElement(By.cssSelector("a[id='inputNationality-button']"));
            WebElement element = driver.findElement(By.xpath("//*[@id=\"inputNationality-button\"]/span[1]"));
            ((JavascriptExecutor)driver).executeScript("arguments[0].innerText ='Česká republika'", element);
            //* POTREBUJEM VYMYSLET JAK TRIGROVAT onchange na tomto elementu 
            ((JavascriptExecutor)driver).executeScript("document.getElementById('inputNationality-button').onchange();");
            */
            
            driver.findElement(By.cssSelector("a[id='inputNationality-button']")).click();
            driver.findElement(By.cssSelector("a[id='inputNationality-button']")).sendKeys(Keys.ARROW_DOWN);
            driver.findElement(By.cssSelector("a[id='inputNationality-button']")).sendKeys(Keys.ENTER);
           
        } catch (NoSuchElementException e) {} 

        try {driver.findElement(By.id("inputIdentificationNumber")).sendKeys(getParameter("inputIdentificationNumber", paramMap)); } catch (NoSuchElementException e) {} 
        try {driver.findElement(By.id("inputDocumentNumber1")).sendKeys(getParameter("inputDocumentNumber1", paramMap));           } catch (NoSuchElementException e) {} 


        try {driver.findElement(By.id("inputCity")).sendKeys(getParameter("inputCity", paramMap));                  } catch (NoSuchElementException e) {} 
        try {driver.findElement(By.id("inputStreet")).sendKeys(getParameter("inputStreet", paramMap));              } catch (NoSuchElementException e) {} 
        try {driver.findElement(By.id("inputHouseOrNumber")).sendKeys(getParameter("inputHouseOrNumber", paramMap));} catch (NoSuchElementException e) {} 
        try {driver.findElement(By.id("inputZip")).sendKeys(getParameter("inputZip", paramMap));                    } catch (NoSuchElementException e) {} 

        /* MUSIME VOSALIT NEJAK*/
        try {
            driver.findElement(By.cssSelector("a[id='selectNotificationMethodParticipant1-button']")).click();
            for (int i=0; i<3; i++){
                driver.findElement(By.cssSelector("a[id='selectNotificationMethodParticipant1-button']")).sendKeys(Keys.ARROW_DOWN);
            }
            driver.findElement(By.cssSelector("a[id='selectNotificationMethodParticipant1-button']")).sendKeys(Keys.ENTER);
        } catch (NoSuchElementException e) {} 

        try {
            driver.findElement(By.cssSelector("a[id='selectPreferredTimeParticipant1-button']")).click();
            driver.findElement(By.cssSelector("a[id='selectPreferredTimeParticipant1-button']")).sendKeys(Keys.ARROW_DOWN);
            driver.findElement(By.cssSelector("a[id='selectPreferredTimeParticipant1-button']")).sendKeys(Keys.ENTER);
        } catch (NoSuchElementException e) {} 


        /** Billing info **/
        try {
            driver.findElement(By.cssSelector("a[id='selectPaymentType1createNew-button']")).click();
            for (int i=0; i<3; i++){
                driver.findElement(By.cssSelector("a[id='selectPaymentType1createNew-button']")).sendKeys(Keys.ARROW_DOWN);
            }
            driver.findElement(By.cssSelector("a[id='selectPaymentType1createNew-button']")).sendKeys(Keys.ENTER);
        } catch (NoSuchElementException e) {} 

        /** Delivery method **/
        try {
            driver.findElement(By.cssSelector("a[id='deliveryType-button']")).click();
            driver.findElement(By.cssSelector("a[id='deliveryType-button']")).sendKeys(Keys.ARROW_DOWN);
            driver.findElement(By.cssSelector("a[id='deliveryType-button']")).sendKeys(Keys.ENTER);
        } catch (NoSuchElementException e) {} 

        /** Print **/

        try {
            driver.findElement(By.cssSelector("a[id*='selectmenu']")).click();
            for (int i=0; i<3; i++){
                driver.findElement(By.cssSelector("a[id*='selectmenu']")).sendKeys(Keys.ARROW_DOWN);
            }
            driver.findElement(By.cssSelector("a[id*='selectmenu']")).sendKeys(Keys.ENTER);
        } catch (NoSuchElementException e) {} catch (ElementNotVisibleException e) {} 
        try {
            driver.findElement(By.cssSelector("a[target='_DOWNLOAD']")).click(); 
            System.out.println(paramMap.get("environment")+"/"+paramMap.get("orderId"));
            while(!barcodePresent(paramMap.get("environment"),paramMap.get("orderId"))){
                Thread.sleep(200);
            };
        } catch (NoSuchElementException e) {} 
    }
    
    public static boolean barcodePresent(String environment, String orderId) throws SQLException, ClassNotFoundException {
        String barcode = "";
        String db = "QCOM01";
        if(environment.equals("2")){
            db = "Q2COM01";
        }
        Connection conn = connectToDb(db);
        Statement state = conn.createStatement();
        String query = "select val\n" +
        "from OCV_REPO.BUSN_INTER_ITEM bii\n" +
        "join OCV_REPO.PROD_CHAR_VAL pchv on bii.PROD_IDENT = pchv.PROD_IDENT\n" +
        "where bii.BUSN_INTER_IDENT = '"+orderId+"'\n" +
        "and pchv.PROD_SPEC_CHAR_IDENT = 'barcode'";
        System.out.println("QUERY: " + query);

        ResultSet rs = state.executeQuery(query);
        while (rs.next()) {
            barcode = rs.getString("val");
        //System.out.println("IMEI: "+iccid);
        }
        state.close();
        conn.close();
        if(barcode.equals("")){
            return false;
        }
        else{
            return true;
        }
    }
}
