package jesg;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class LinkTest {
	private static final Pattern COMMA = Pattern.compile(",");
	
    private WebDriver driver;
    private Wait<WebDriver> wait;
    
	@DataProvider(name="links")
	public Object[][] getLinks() throws IOException{
        List<String> lines = FileUtils.readLines(new File("links.csv"));
        Object[][] csvData = new Object[lines.size()][3];
        
        for(int i=0; i< lines.size(); i++){
        	csvData[i] = COMMA.split(lines.get(i));
        }
        return csvData;
	}
	
    @BeforeTest
    public void setUp() throws Exception {
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 10);
    }

    @AfterTest
    public void tearDown() throws Exception {
        driver.quit();
    }
    
	@Test(dataProvider="links")
	public void verifyLinks(String url, String link, String title){
        driver.get(url);
        driver.findElement(By.partialLinkText(link)).click();
        wait.until(ExpectedConditions.titleIs(title));
        assertEquals(title, driver.getTitle());
	}

}
