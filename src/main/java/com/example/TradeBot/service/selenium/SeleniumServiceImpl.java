package com.example.TradeBot.service.selenium;


import com.example.TradeBot.dto.MyChromeProfile;
import com.example.TradeBot.dto.MySteamProfile;
import com.example.TradeBot.model.InventoryItem;
import com.example.TradeBot.model.PurchaseRequest;
import com.example.TradeBot.service.inventory_item.InventoryItemService;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SeleniumServiceImpl implements SeleniumService{
    private final ObjectMapper objectMapper;
    private WebDriver driver;
    private final PurchaseRequestService purchaseRequestService;
    private final InventoryItemService inventoryItemService;

    public SeleniumServiceImpl(ObjectMapper objectMapper, PurchaseRequestService purchaseRequestService, InventoryItemService inventoryItemService) {
        this.objectMapper = objectMapper;
        this.purchaseRequestService = purchaseRequestService;
        this.inventoryItemService = inventoryItemService;
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

    public boolean login() throws InterruptedException, IOException {

        driver.get("https://steamcommunity.com/");

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
                return false;
            }
        } catch (Exception e){
            log.info("we now in profile");
        }
        return true;
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
                        // get users last sale price
                        string = driver.findElement(By.xpath("//*[@id=\"searchResultsRows\"]/div[" + 2 + "]/div[2]/div[2]/span/span[1]")).getText();
                        string = string.replace("₴", "");
                        string = string.replace(",", ".");
                        float usersLastSalePrice = Float.parseFloat(string);
                        System.out.println("users last sale price:" + usersLastSalePrice);
                        float buyPrice = usersLastSalePrice * 100 / (113 + profitPercent);
                        System.out.println("buy price:" + buyPrice);
                        // get users request to buy price
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
                                driver.findElement(By.xpath("//*[@id=\"market_buyorder_info\"]/div[1]/div[1]/a/span")).click();
                                driver.findElement(By.xpath("//*[@id=\"market_buy_commodity_input_price\"]")).clear();
                                System.out.println("------------------------send " + buyPrice);
//                                driver.findElement(By.xpath("//*[@id=\"market_buy_commodity_input_price\"]")).sendKeys(String.format("%,2f", buyPrice));
                                driver.findElement(By.xpath("//*[@id=\"market_buy_commodity_input_price\"]")).sendKeys(String.format("%,2f", 10.4f));
                                try{
                                    // click on checkbox I agree to the terms of the Steam Subscriber Agreement
                                    driver.findElement(By.xpath("//*[@id=\"market_buyorder_dialog_accept_ssa\"]")).click();
                                    // click on place order
                                    driver.findElement(By.xpath("//*[@id=\"market_buyorder_dialog_purchase\"]/span")).click();
                                    // after success place order click on crosshair
                                    driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div[1]")).click();

                                    // after success buy
                                    PurchaseRequest purchaseRequest = new PurchaseRequest();
                                    purchaseRequest.setPurchasePrice(buyPrice);
                                    purchaseRequest.setSellPrice(usersLastSalePrice);
                                    purchaseRequest.setFullName(fullName);
                                    purchaseRequest.setExterior(exterior);
                                    purchaseRequestService.savePurchaseRequest(purchaseRequest);
                                } catch (Exception ignored) {

                                }
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

    public float getBalance() {
        String balanseString = driver.findElement(By.xpath("//*[@id=\"header_wallet_balance\"]")).getText();
        balanseString = balanseString.replace("₴", "");
        balanseString = balanseString.replace(",", ".");
        return Float.parseFloat(balanseString);
    }

    public void updateInventory() {
        driver.get("https://steamcommunity.com/id/398246592304682534098234/inventory/#730");
        try {
            driver.findElement(By.xpath("//*[@id=\"inventory_load_error_ctn\"]/div/div/div/div[2]/span")).click();
            log.info("This inventory is not available at this time. Please try again later.");
        } catch (Exception e){
            Set<InventoryItem> items = new HashSet<>();
            for(int count = 0; count < 2; count++){
                for(int i = 1; i <= 25; i++){
                    try {
                        driver.findElement(By.xpath("//*[@id=\"inventory_76561198888388607_730_2\"]/div/div[" + i + "]")).click();
                    } catch (Exception ex){
                        break;
                    }
                    for(int j = 0; j <= 1; j++){
                        String exterior = driver.findElement(By.xpath("//*[@id=\"iteminfo" + j + "_item_descriptors\"]/div[1]")).getText();
                        if(exterior.contains("Exterior:")){
                            exterior = exterior.replace("Exterior: ", "");
                        } else {
                            continue;
                        }
                        String name = driver.findElement(By.cssSelector("#iteminfo" + j + "_item_name")).getText();
                        if(Objects.isNull(name) || name.equals("")){
                            continue;
                        }
                        String tradableAfterString;
                        LocalDateTime tradableAfter;
                        try {
                            tradableAfterString = driver.findElement(By.xpath("//*[@id=\"iteminfo" + j + "_item_owner_descriptors\"]/div[2]")).getText();
                            tradableAfterString = tradableAfterString.replace("Tradable After ", "");
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy (H:mm:ss) z", Locale.ENGLISH);
                            tradableAfter = LocalDateTime.parse(tradableAfterString, formatter);
                            tradableAfter = tradableAfter.plusHours(3);
                        } catch (Exception exe){
                            tradableAfter = null;
                        }
                        InventoryItem item = new InventoryItem();
                        item.setFullName(name);
                        item.setExterior(exterior);
                        item.setCanSellAt(tradableAfter);
                        items.add(item);
                        System.out.println(item);
                    }
                }
            }
            System.out.println("------------------------------------------------------------------------");
            List<PurchaseRequest> purchaseRequestList = purchaseRequestService.getAllPurchaseRequest();
            for(InventoryItem item : items){
                for(PurchaseRequest purchaseRequest: purchaseRequestList){
                    if(item.getFullName().equals(purchaseRequest.getFullName()) && item.getExterior().equals(purchaseRequest.getExterior())){
                        purchaseRequestService.deleteByFullNameAndExterior(purchaseRequest.getFullName(), purchaseRequest.getExterior());
                        item.setBuyPrice(purchaseRequest.getPurchasePrice());
                        item.setSellPrice(purchaseRequest.getSellPrice());
                        inventoryItemService.saveInventoryItem(item);
                    }
                }
            }
        }
    }

    @Override
    public void getBuyOrders() {
        driver.get("https://steamcommunity.com/market/");
        for (int i = 2; true; i++){
            try {
                String fullBuyOrderName = driver.findElement(By.xpath("//*[@id=\"tabContentsMyListings\"]/div[3]/div[" + i + "]/div[4]/span[1]/a[1]")).getText();
            } catch (Exception e){
                break;
            }
        }
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
