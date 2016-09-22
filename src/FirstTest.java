
import java.sql.CallableStatement;
import java.sql.Connection;
//import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
//import java.util.List;
import java.util.Properties;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

public class FirstTest {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        initDbClass();
        System.setProperty("webdriver.chrome.driver", "C:/Users/martin.sivok/workspace/chromedriver.exe");
    }

    @Test(expected = org.openqa.selenium.NoSuchElementException.class)
    public void test1() throws InterruptedException, ClassNotFoundException, SQLException {
        initDbClass();
        WebElement element = null;
        Actions actions = null;
        System.setProperty("webdriver.chrome.driver", "C:/Users/martin.sivok/workspace/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("file:///C:/Users/martin.sivok/Desktop/javascriptloader.html?dbhash=s002696F85A2BBD6C7E872B5D9B8B3B741AC7AA1E400DE62E85DE17FE8D4DBD85AF4A2398D7447EF161D8FC9C6F9B46568AE11A528F0791BFA8501DAC6214E25D94AD42C15B3B228AB8617FBE953E7F94ADDD18100068D81088E2F20C1AB61A2CFB20031CABBD5DCE0D115F6EC0B97BB6F100724F38D1F4B8C3DDBCFACCDD68DC0354BDCA2FB88F6D02227737B1D485BEDBAAC334638DC44E9DE99D1967F7E0CC18BD69DA0D7601C58A74C693C8A69CC1A5A1309B219A38CFE89822FB34A726ACB8411A623AC9DB59D06F4FAE119C19F0D9DDC749EEA90106254D2B7B6A04E84D35C55E887149FFE5AA9E103A9EE67D13CC7597BC35144EC7D54A22C68DF0EFA603C35835837670AB6322E213CEDAFD53868C7982CA0BD5216209FF161E3AD156F36E747493875BC813177E58C79E21EC1ED669AE3533E7050D4EE0A65172CF7C8C4ABCBAA7DF0FD9845C9F9973493B390033EFA9B345E07EB981EF702BDD4A49F73BA5222BEFA9AFD33ECA604840F15A3849CC09D3B52998CA6F22890F0972AE57D8B6CEBBCC480FC33050D0CE48361114885EC8081F20D48C3CD4C7106650C0D5259AE6F2800D79BE347407C848FF8DB2AC03038DF5D0FE8EF1F236CA5F773A1F7A2510945D7C57B7E7F7E4CA695B38E199CDB317E9AE79CF0A0910CEAB6BFFB65056247685F050029950FD9921B79B89BB6DE6782D2A0F45453280C8B308E7590203C617916EB115EF101274557ED385474FD3E7BEC1671EF10EB6557024833017152ED064E8F1143637F674116CA86F72899AA3AD44D517043E6A95EA4101ED5E93F850EE1DB33D65353EA35B13941AF331F3FF069DD614E4317B459A07CBCBBE0D3858EE9A8E55D2B62CB5EEC4F9A5CA453BFC1D7B9F2C9FD5FAE140FBBBA26DC27ED12BD6B1B9B8924EB7A9D1B8EE07FD29E05CDDC31C9A3ECF74F6A7D1740E2DB5996192EEB74192940A08F730113B0C8A0EC0090D33AF11CDEACEEBE373B3A11AA8FCFAC2A689E8F1FFA29E0BDAD670C4E5B67B6A996AF62C49F140CE1D3D6937C78C21B7A17976136358E5A1E9A3CA2DEAA4EEA3409C52E65E68C3984CAAC3413E248117118364AF4A75A24C6AEEF4D78767C46F829EC7772B5BE0A082D26B0E135981EF4E6481399B3E50E5A1F5A567A95C254AF8DB97660971D4D5A843259BFB9E1AAF7B2759F6C143533264016E80C05D0479DD2BADEF2700315640A675BA364E9D66742E0A894448BFFAE7366AAFAE6428055D07121A9697D3F4155E7F74029CD617821FAD59C5B024AE34E548079AA8BAF92F825CEA06BB1D1AD2DA48DE7E7FC791AA0A64A961CBD5F8AF42DE788BDC11087FA39C1A7281D1B5E6EB1D6F9676CB4016984A7EF105223B375FBC18226C4413382ABFDCD067ABE212CD2DF04E3753E755C77113227F0F047D3DE08D3758BF2507A3971E45602EF357C79C1E4DEE7BCEFB602C5EE2C81466628AADD415B6193708AA108B92CBC400783458CDD3D0D0764722EB18F92C2778BAAEB663BD67619718C8AD19EC70C269F353A8DF05CA3B025A02C025DCE2A0B5C5C07B07DCEB6534A0EE67C72CDA7A254697A5E02C189FF8C20877D0F0959EB1D25252225AFFFE36C940C60CC5457345918176A4AEF24D2817C");
        driver.switchTo().frame("symbUrlIFrame1");

        WebElement radio = driver.findElement(By.xpath("//input[@id = 'radio']/.."));
        radio.click();
        element = driver.findElement(By.partialLinkText("Pokračovat"));
        element.click();
        String imei = getICCID("QAPP01", "15444");
        element = driver.findElement(By.xpath("//span[contains(text(), 'ICCID')]"));
        actions = new Actions(driver);
        actions.moveToElement(element);
        actions.click();
        actions.sendKeys(imei);
        actions.build().perform();

        element = driver.findElement(By.xpath("//label[contains(text(), 'SAP k')]"));
        actions = new Actions(driver);
        actions.moveToElement(element);
        actions.click();
        actions.sendKeys("1216");
        actions.build().perform();

        element = driver.findElement(By.partialLinkText("Pokračovat"));
        element.click();

    }

    @Test()
    public void createURL() throws ClassNotFoundException, SQLException {
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
                + "'EXTKRIVANEKT',"
                + "'ocvOrdering',"
                + "'CZ',"
                + "'',"
                + "'orderingContext=>^^processName=>^^productInstanceOperation=>^^operatorRole=>SBL_TR_Comp_Order_1000|SBL_TR_Comp_Order_10000|SBL_TR_Comp_Order_2000|SBL_TR_Comp_Order_5000|SBL_TR_Comp_Order_50000|SBL_TR_Edit_Card_Id|SBL_TR_Suspend_Resume_Collection|SBL_TR_Suspend_Resume_Fraud|SBL_UPC_Brand Shop|SBL_UPC_Complaints|SBL_UPC_Contract Relationship Admin - AT Advanced|SBL_UPC_Contract Relationship Admin - AT Standard|SBL_UPC_Retention|SBL_UPC_Terminations|SBL_UPC_Brand Shop Advanced|SBL_UPC_Complaints and Termination|SBL_UPC_Contractual Relationship Admin|SBL_UPC_Fraud|SBL_TR_B2B_Testing|SBL_UPC_Credit^^sblPositionId=>0-5220^^customerId=>9999999999^^segmentId=>^^contactHistoryId=>^^employeeId=>5050^^basketId=>^^outletId=>15445^^outletCode=>PGSA7.001.001^^outletSAPCode=>S-PGSA7^^billingProfileId=>^^productOfferingId=>PO0661^^hierarchyLevel=>^^hierarchyId=>^^campaignIds=>^^rtdId=>^^productInstanceId=>^^dateTo=>^^svcRequestId=>^^relevantDate=>^^offeringCatalogueRel=>CAT_BSHAxPO0661xcompensation0^^voucherId=>^^customerType=>B2C - Prepaid^^customerNumber=>9999999999',"
                + "abc, das);"
                + "end;");

        stmt.close();

        //dbmsOutput.show();
        hash = dbmsOutput.getHash();
        System.out.println(hash);
        dbmsOutput.close();
        conn.close();

    }

    public static void initDbClass() throws ClassNotFoundException, SQLException {

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: unable to load driver class!");
            System.exit(1);
        } catch (IllegalAccessException ex) {
            System.out.println("Error: access problem while loading!");
            System.exit(2);
        } catch (InstantiationException ex) {
            System.out.println("Error: unable to instantiate driver!");
            System.exit(3);
        }
    }

    public static String getICCID(String enviroment, String outletId) throws ClassNotFoundException, SQLException {
        System.out.println("Outlet: " + outletId);
        String iccid = null;
        Connection conn = connectToDb(enviroment);
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

        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            iccid = rs.getString("iccid");
            //System.out.println("IMEI: "+iccid);
        }
        stmt.close();
        conn.close();
        return iccid;
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

            default:
                break;
        }

        return conn;
    }

}
