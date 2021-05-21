import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;

public class InstaUnfollowersList 
{
	public List<WebElement> followerList;
	public List<WebElement> followingList;
	public ArrayList<String> follower;
	public ArrayList<String> following;
	
	public static ChromeDriver driver = new ChromeDriver();
	
	public InstaUnfollowersList()
	{
		follower = new ArrayList<String>();
		following = new ArrayList<String>();
	}
	
	public void openBrowser()
	{
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.get("https://www.instagram.com/");
	}
	
	public void Login() throws InterruptedException
	{
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys("your_username");
		driver.findElement(By.xpath("//input[@name='password']")).sendKeys("your_password");
		driver.findElement(By.xpath("//button/div[text()='Log In']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//img[@data-testid='user-avatar']")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[text()='Profile']")).click();
		Thread.sleep(2000);
	}
	
	public int getFollowers() throws InterruptedException
	{
		driver.findElement(By.xpath("(//span[@class='g47SY '])[2]")).click();
		WebElement m=driver.findElement(By.xpath("//div[@class='isgrP']"));
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		
		for(int i=0;i<65;i++)
		{
				jse.executeScript("arguments[0].scrollTop += arguments[0].offsetHeight;",m);
				Thread.sleep(500);
		}
		//Thread.sleep(500);
		followerList = driver.findElements(By.xpath("//*[@class='isgrP']/ul/div/li/div/div/div[2]/div/span/a"));
		
		for(int i=0;i<followerList.size();i++)
		{
			follower.add(followerList.get(i).getText());
		}
		driver.findElement(By.xpath("(//*[@class='wpO6b  '])[2]")).click();
		return followerList.size();
	}
	
	public int getFollowing() throws InterruptedException
	{
		driver.findElement(By.xpath("(//span[@class='g47SY '])[3]")).click();
		WebElement m=driver.findElement(By.xpath("//div[@class='isgrP']"));
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		
		for(int i=0;i<90;i++)
		{
			jse.executeScript("arguments[0].scrollTop += arguments[0].offsetHeight;",m);
			Thread.sleep(500);
		}
		followingList = driver.findElements(By.xpath("//*[@class='isgrP']/ul/div/li/div/div/div[2]/div/span/a"));
		for(int i=0;i<followingList.size();i++)
		{
			following.add(followerList.get(i).getText());
		}
		driver.findElement(By.xpath("(//*[@class='wpO6b  '])[2]")).click();
		return followingList.size();
	}
	
	public void writeExcel() throws IOException
	{
		FileInputStream fin=new FileInputStream("path_to_excel_file");
		XSSFWorkbook wb=new XSSFWorkbook(fin);
		XSSFSheet ws=wb.getSheet("Sheet1");
		
		for(int i=0;i<followingList.size();i++)
		{
			
			Row row = ws.createRow(i+1);
			row.createCell(0).setCellValue(following.get(i));
			
			if(follower.contains(following.get(i)))
			{
				row.createCell(1).setCellValue("Following you");
			}
			else
			{
				row.createCell(1).setCellValue("Not Following you");
			}
		}
		
		FileOutputStream fout=new FileOutputStream("path_to_excel_file");
		wb.write(fout);
		
		fin.close();
		fout.close();
	}
	
	public void closeBrowser()
	{
		driver.close();
	}
	
	
	public static void main(String args[]) throws InterruptedException, IOException
	{
		InstaUnfollowersList u1 = new InstaUnfollowersList();
		u1.openBrowser();
		u1.Login();
		System.out.println("No of followers "+u1.getFollowers());
		System.out.println("No of following "+u1.getFollowing());
		u1.writeExcel();
		u1.closeBrowser();
	}
}
