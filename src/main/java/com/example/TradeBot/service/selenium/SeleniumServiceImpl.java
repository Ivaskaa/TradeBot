package com.example.TradeBot.service.selenium;


import com.example.TradeBot.dto.MyChromeProfile;
import com.example.TradeBot.dto.MySteamProfile;
import com.example.TradeBot.model.PurchaseRequest;
import com.example.TradeBot.service.purchase_request.PurchaseRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SeleniumServiceImpl implements SeleniumService{
    private final ObjectMapper objectMapper;
    private WebDriver driver;

    private final PurchaseRequestService purchaseRequestService;

    public SeleniumServiceImpl(ObjectMapper objectMapper, PurchaseRequestService purchaseRequestService) {
        this.objectMapper = objectMapper;
        this.purchaseRequestService = purchaseRequestService;
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


    @Override
    public void getElementsToBuy() throws InterruptedException {

        float startPrice = 100f;
        float endPrice = 200f;
        float profitPercent = 4.5f;

        for(int i = 1; i < 10; i++){
            driver.get("https://steamcommunity.com/market/search?q=&category_730_ItemSet%5B%5D=any&category_730_ProPlayer%5B%5D=any&category_730_StickerCapsule%5B%5D=any&category_730_TournamentTeam%5B%5D=any&category_730_Weapon%5B%5D=tag_weapon_ak47&appid=730#p" + i + "_price_asc");
            Thread.sleep(500);
            for (int j = 0; j < 10; j++){
                String string;
                try {
                    string = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(By.cssSelector("#result_" + j + " > div.market_listing_price_listings_block > div.market_listing_right_cell.market_listing_their_price > span.market_table_value.normal_price > span.normal_price"))).getText();
                } catch (Exception e){
                    continue;
                }
                System.out.println(string);
                string = string.replace("₴", "");
                string = string.replace(",", ".");
                float price = Float.parseFloat(string);
                if(price > startPrice && price < endPrice){
                    try {
                        driver.findElement(By.xpath("//*[@id=\"result_" + j + "\"]")).click();
                        string = driver.findElement(By.xpath("//*[@id=\"searchResultsRows\"]/div[" + 2 + "]/div[2]/div[2]/span/span[1]")).getText();
                        string = string.replace("₴", "");
                        string = string.replace(",", ".");
                        float lastPrice = Float.parseFloat(string);
                        System.out.println("last sell price:" + lastPrice);
                        float buyPrice = lastPrice * 100 / (113 + profitPercent);
                        System.out.println("buy price:" + buyPrice);
                        string = driver.findElement(By.cssSelector("#market_commodity_buyrequests > span:nth-child(2)")).getText();
                        string = string.replace("₴", "");
                        string = string.replace(",", ".");
                        float requestsToBuyAt = Float.parseFloat(string);
                        System.out.println("requests to buy at:" + requestsToBuyAt);
                        if(buyPrice > requestsToBuyAt){
                            String fullName = driver.findElement(By.xpath("//*[@id=\"largeiteminfo_item_name\"]")).getText();
                            System.out.println("-------------------------------- fullName" + fullName);
                            String exterior = driver.findElement(By.xpath("//*[@id=\"largeiteminfo_item_descriptors\"]/div[1]")).getText();
                            exterior = exterior.replace("Exterior: ", "");
                            System.out.println("-------------------------------- exterior" + exterior);
                            if(!purchaseRequestService.isExistByfFullNameAndExterior(fullName, exterior)){
                                buyPrice = (buyPrice + requestsToBuyAt) / 2;
                                // buy logic
                                System.out.println("buy logic for" + buyPrice);
                                // after success buy
                                PurchaseRequest purchaseRequest = new PurchaseRequest();
                                purchaseRequest.setPurchasePrice(buyPrice);
                                purchaseRequest.setSellPrice(lastPrice);
                                purchaseRequest.setFullName(fullName);
                                purchaseRequest.setExterior(exterior);
                                purchaseRequestService.savePurchaseRequest(purchaseRequest);
                            }
                        }
                        driver.get("https://steamcommunity.com/market/search?q=&category_730_ItemSet%5B%5D=any&category_730_ProPlayer%5B%5D=any&category_730_StickerCapsule%5B%5D=any&category_730_TournamentTeam%5B%5D=any&category_730_Weapon%5B%5D=tag_weapon_ak47&appid=730#p" + i + "_price_asc");
                    } catch (Exception e){
                        continue;
                    }
                }
            }

        }
    }

    public void login() throws InterruptedException, IOException {

        driver.get("https://cs.money/uk/");

        try {
            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"global_action_menu\"]/a"))).click();
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
        String balanseString = driver.findElement(By.cssSelector("#layout-page-header > div.MediaQueries_desktop__TwhBE > div > div.Personal_personal__1v9GT > div.Balances_balance_container__1RZzs > div.Balances_foreground_balance__1fVyv > div > div.USDCurrencyView_info__2Zc3D > span.csm_ui__text__6542e.csm_ui__body_14_medium__6542e.USDCurrencyView_balance__2Hihw")).getText();
        balanseString = balanseString.replace("$ ", "");
        return Float.parseFloat(balanseString);
    }

    public void changeSelectToDiscount() throws InterruptedException {
        driver.findElement(By.cssSelector("#layout-page-content-area > div > div > div.MediaQueries_desktop__TwhBE.TradePage_full_height__3Vv16 > div > div.TradePage_content__22N7o > div:nth-child(3) > div.bot-listing_container__1LBz1 > div.MediaQueries_desktop__TwhBE.bot-listing_sorting_and_search_wrapper__3Rn_x > div > button")).click();
        Actions actions = new Actions(driver);
        Thread.sleep(1000);
        actions.moveByOffset(1600, 305).perform();
        actions.click().perform();
        Thread.sleep(2000);
    }

}
