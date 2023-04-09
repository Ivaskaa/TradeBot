package com.example.TradeBot.service.selenium;


import com.example.TradeBot.dto.MyChromeProfile;
import com.example.TradeBot.dto.MySteamProfile;
import com.example.TradeBot.dto.inventory.InventoryFromParser;
import com.example.TradeBot.dto.item.ItemFromParser;
import com.example.TradeBot.dto.request.RequestListOfItems;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SeleniumServiceImpl implements SeleniumService{
    private final ObjectMapper objectMapper;
    private WebDriver driver;

    public SeleniumServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void startDriver() throws IOException {
        File fileChromeProfile = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("my_chrome_profile.json")).getFile());
        MyChromeProfile myChromeProfile = objectMapper.readValue(fileChromeProfile, MyChromeProfile.class);

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        options.addArguments("--disabble-infoars");
        options.addArguments("user-data-dir=" + myChromeProfile.getPath());
        options.addArguments("profile-directory=" + myChromeProfile.getProfile());

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    public void login() throws InterruptedException, IOException {

        driver.get("https://cs.money/uk/");

        try {
            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(By.cssSelector("#layout-page-header > div.MediaQueries_desktop__TwhBE > div > div.Personal_personal__1v9GT > a > button"))).click();
            try {
                driver.findElement(By.cssSelector("#imageLogin")).click();
                log.info("success login in profile");
            } catch (Exception e){
                File fileSteamProfile = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("my_steam_profile.json")).getFile());
                MySteamProfile mySteamProfile = objectMapper.readValue(fileSteamProfile, MySteamProfile.class);
                driver.findElement(By.cssSelector("#responsive_page_template_content > div.page_content > div:nth-child(1) > div > div > div > div.newlogindialog_FormContainer_3jLIH > div > form > div:nth-child(1) > input")).sendKeys(mySteamProfile.getUsername());
                driver.findElement(By.cssSelector("#responsive_page_template_content > div.page_content > div:nth-child(1) > div > div > div > div.newlogindialog_FormContainer_3jLIH > div > form > div:nth-child(2) > input")).sendKeys(mySteamProfile.getPassword());
                driver.findElement(By.cssSelector("#responsive_page_template_content > div.page_content > div:nth-child(1) > div > div > div > div.newlogindialog_FormContainer_3jLIH > div > form > div.newlogindialog_SignInButtonContainer_14fsn > button")).click();
                log.info("need new steam login with authenticator");
            }
        } catch (Exception e){
            log.info("we now in profile");
        }
    }

    public float getBalance() {
        driver.get("https://cs.money/uk/");
        String balanseString = driver.findElement(By.cssSelector("#layout-page-header > div.MediaQueries_desktop__TwhBE > div > div.Personal_personal__1v9GT > div.Balances_balance_container__1RZzs > div.Balances_foreground_balance__1fVyv > div > div.USDCurrencyView_info__2Zc3D > span.csm_ui__text__6542e.csm_ui__body_14_medium__6542e.USDCurrencyView_balance__2Hihw")).getText();
        balanseString = balanseString.replace("$ ", "");
        return Float.parseFloat(balanseString);
    }

    public List<ItemFromParser> getInventoryInNewTab() throws JsonProcessingException {
        String uri = "https://cs.money/3.0/load_user_inventory/730?isPrime=false&limit=60&noCache=true&offset=0&order=desc&sort=price&withStack=true";

        // Execute JavaScript to open a new tab
        ((JavascriptExecutor) driver).executeScript("window.open()");

        // Switch to the new tab
        driver.switchTo().window((String) driver.getWindowHandles().toArray()[1]);

        // Navigate to the URL in the new tab
        driver.get(uri);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        // getting not formatted jsonfile as jsonText
        String jsonText = (String) ((JavascriptExecutor) driver).executeScript("return document.body.textContent");

        // formatting jsonText
        jsonText = jsonText.substring(9);
        jsonText = jsonText.replaceAll("[^\\x20-\\x7e]", "");

        // mapping jsonText to InventoryFromParser
        ObjectMapper objectMapper = new ObjectMapper();
        InventoryFromParser inventory = objectMapper.readValue(jsonText, InventoryFromParser.class);
        List<ItemFromParser> items = inventory.getItems();

        // Close the current tab
        driver.close();

        // Switch to the first window handle
        driver.switchTo().window(driver.getWindowHandles().iterator().next());
        return items;
    }

    public CookieStore getCookies() {
        CookieStore cookieStore = new BasicCookieStore();
        // Get all cookies
        Set<Cookie> cookies = driver.manage().getCookies();

        // Print each cookie
        for (Cookie cookie : cookies) {
            cookieStore.addCookie(new BasicClientCookie(cookie.getName(), cookie.getValue()));
            System.out.println(cookie.getName() + ": " + cookie.getValue());
        }
        return cookieStore;
    }

    public List<ItemFromParser> getInventoryByRequest() throws URISyntaxException, IOException {
        CookieStore cookieStore = getCookies();
        HttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();;

        URIBuilder uriBuilder = new URIBuilder("https://cs.money/3.0/load_user_inventory/730?isPrime=false&limit=60&noCache=true&offset=0&order=desc&sort=price&withStack=true");
//        uriBuilder.addParameter("limit", "60");
//        uriBuilder.addParameter("offset", String.valueOf(offset));
//        uriBuilder.addParameter("order", "asc");
//        uriBuilder.addParameter("priceWithBonus", "30");
//        uriBuilder.addParameter("sort", "price");
//        uriBuilder.addParameter("type", String.valueOf(type));
//        uriBuilder.addParameter("withStack", "true");
        URI uri = uriBuilder.build();

        System.out.println(uri);
        HttpGet httpGet = new HttpGet(uri);
        HttpResponse response = httpClient.execute(httpGet);
        System.out.println("response\n" + response);
        System.out.println("entity\n" + response.getEntity());
        InventoryFromParser inventory = objectMapper.readValue(EntityUtils.toString(response.getEntity()), InventoryFromParser.class);
        System.out.println(inventory);

        List<ItemFromParser> items = inventory.getItems();
        System.out.println(items);
        return items;
    }
}
