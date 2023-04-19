package com.example.TradeBot.service.selenium;


import com.example.TradeBot.dto.MyChromeProfile;
import com.example.TradeBot.dto.MySteamProfile;
import com.example.TradeBot.dto.WeaponTag;
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
    public void sendPurchaseRequests(float startPrice, float endPrice, float profitPercent) throws InterruptedException {

        for (String weaponTag: WeaponTag.tags4_5percent) {
            for (int i = 1; true; i++) {
                boolean isBreak = false;
                driver.get("https://steamcommunity.com/market/search?q=&category_730_ItemSet%5B%5D=any&category_730_ProPlayer%5B%5D=any&category_730_StickerCapsule%5B%5D=any&category_730_TournamentTeam%5B%5D=any&category_730_Weapon%5B%5D=tag_weapon_" + weaponTag + "&appid=730#p" + i + "_price_asc");
                Thread.sleep(500);
                for (int j = 0; j < 10; j++) {
                    String itemPrice;
                    try {
                        itemPrice = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(By.cssSelector("#result_" + j + " > div.market_listing_price_listings_block > div.market_listing_right_cell.market_listing_their_price > span.market_table_value.normal_price > span.normal_price"))).getText();
                    } catch (Exception e) {
                        continue;
                    }
                    itemPrice = itemPrice.replace("₴", "");
                    itemPrice = itemPrice.replace(",", ".");
                    float price = Float.parseFloat(itemPrice);
                    if (price > endPrice) {
                       isBreak = true;
                       break;
                    }
                    if (price > startPrice && price < endPrice) {
                        try {
                            driver.findElement(By.xpath("//*[@id=\"result_" + j + "\"]")).click();
                            // get users last sale price
                            String usersLastSalePriceString = driver.findElement(By.xpath("//*[@id=\"searchResultsRows\"]/div[" + 2 + "]/div[2]/div[2]/span/span[1]")).getText();
                            if(!usersLastSalePriceString.contains("₴")) break;
                            usersLastSalePriceString = usersLastSalePriceString.replace("₴", "");
                            usersLastSalePriceString = usersLastSalePriceString.replace(",", ".");
                            float usersLastSalePrice = Float.parseFloat(usersLastSalePriceString);
                            System.out.println("users last sale price:" + usersLastSalePrice);
                            float buyPrice = usersLastSalePrice * 100 / (113 + profitPercent);
                            System.out.println("-13 + profit percent price: " + buyPrice);
                            // get users request to buy price
                            String usersRequestToBuyPriceString = driver.findElement(By.cssSelector("#market_commodity_buyrequests > span:nth-child(2)")).getText();
                            usersRequestToBuyPriceString = usersRequestToBuyPriceString.replace("₴", "");
                            usersRequestToBuyPriceString = usersRequestToBuyPriceString.replace(",", ".");
                            float requestsToBuyAt = Float.parseFloat(usersRequestToBuyPriceString);
                            System.out.println("requests to buy at:" + requestsToBuyAt);
                            // get count buy requests of skin
                            String buyRequestsCountString = driver.findElement(By.xpath("//*[@id=\"market_commodity_buyrequests\"]/span[1]")).getText();
                            int buyRequestsCount = Integer.parseInt(buyRequestsCountString);
                            System.out.println("buy requests count: " + buyRequestsCount);
                            if (buyPrice > requestsToBuyAt && buyRequestsCount > 5000) {
                                String fullName = driver.findElement(By.xpath("//*[@id=\"largeiteminfo_item_name\"]")).getText();
                                String exterior = driver.findElement(By.xpath("//*[@id=\"largeiteminfo_item_descriptors\"]/div[1]")).getText();
                                exterior = exterior.replace("Exterior: ", "");
                                if(fullName.contains("Souvenir ")) continue;
                                // buy logic
                                if (!purchaseRequestService.isExistByfFullNameAndExterior(fullName, exterior)) {
                                    buyPrice = (buyPrice + requestsToBuyAt) / 2;
                                    // click place buy order button
                                    driver.findElement(By.xpath("//*[@id=\"market_buyorder_info\"]/div[1]/div[1]/a/span")).click();
                                    // clear price in input
                                    driver.findElement(By.xpath("//*[@id=\"market_buy_commodity_input_price\"]")).clear();
                                    System.out.println("------------------------ buy price " + buyPrice);
                                    // write price in input
                                    driver.findElement(By.xpath("//*[@id=\"market_buy_commodity_input_price\"]")).sendKeys(String.format("%,2f", buyPrice));
                                    try {
                                        // click on checkbox I agree to the terms of the Steam Subscriber Agreement
                                        driver.findElement(By.xpath("//*[@id=\"market_buyorder_dialog_accept_ssa\"]")).click();
                                        // click on place order
                                        driver.findElement(By.xpath("//*[@id=\"market_buyorder_dialog_purchase\"]/span")).click();
                                        while (true){
                                            String responseAboutOrder = driver.findElement(By.xpath("//*[@id=\"market_buy_commodity_status\"]")).getText();
                                            if(!responseAboutOrder.equals("Placing buy order...") && !responseAboutOrder.equals("Finding matching item listings at your desired price...")){
                                                if(responseAboutOrder.contains("Success! Your buy order has been placed.")){
                                                    // after success buy
                                                    PurchaseRequest purchaseRequest = new PurchaseRequest();
                                                    purchaseRequest.setPurchasePrice(buyPrice);
                                                    purchaseRequest.setSellPrice(usersLastSalePrice);
                                                    purchaseRequest.setFullName(fullName);
                                                    purchaseRequest.setExterior(exterior);
                                                    purchaseRequestService.savePurchaseRequest(purchaseRequest);
                                                }
                                                break;
                                            }
                                        }
                                        // after success place order click on crosshair
                                        driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div[1]")).click();
                                    } catch (Exception ignored) {}
                                }
                            }
                            driver.get("https://steamcommunity.com/market/search?q=&category_730_ItemSet%5B%5D=any&category_730_ProPlayer%5B%5D=any&category_730_StickerCapsule%5B%5D=any&category_730_TournamentTeam%5B%5D=any&category_730_Weapon%5B%5D=tag_weapon_" + weaponTag + "&appid=730#p" + i + "_price_asc");
                        } catch (Exception ignored){}
                    }
                }
            if(isBreak) break;
            }
        }
    }

    public float getBalance() {
        String balanseString = driver.findElement(By.xpath("//*[@id=\"header_wallet_balance\"]")).getText();
        balanseString = balanseString.replace("₴", "");
        balanseString = balanseString.replace(",", ".");
        return Float.parseFloat(balanseString);
    }

    public void updateInventory() throws IOException {
        File fileSteamProfile = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("my_steam_profile.json")).getFile());
        MySteamProfile mySteamProfile = objectMapper.readValue(fileSteamProfile, MySteamProfile.class);
        driver.get("https://steamcommunity.com/id/"+mySteamProfile.getProfileId()+"/inventory/#730");
        try {
            driver.findElement(By.xpath("//*[@id=\"inventory_load_error_ctn\"]/div/div/div/div[2]/span")).click();
            log.info("This inventory is not available at this time. Please try again later.");
        } catch (Exception e){
            Set<InventoryItem> items = new HashSet<>();
            for(int count = 0; count < 2; count++){
                for(int i = 1; i <= 25; i++){
                    try {
                        driver.findElement(By.xpath("//*[@id=\"inventory_76561198888388607_730_2\"]/div/div[" + i + "]")).click();
                        Thread.sleep(2000);
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
    public void sendSellRequests() throws InterruptedException, IOException {
        File fileSteamProfile = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("my_steam_profile.json")).getFile());
        MySteamProfile mySteamProfile = objectMapper.readValue(fileSteamProfile, MySteamProfile.class);
        driver.get("https://steamcommunity.com/id/" + mySteamProfile.getProfileId() + "/inventory/#730");
        try {
            driver.findElement(By.xpath("//*[@id=\"inventory_load_error_ctn\"]/div/div/div/div[2]/span")).click();
            log.info("This inventory is not available at this time. Please try again later.");
        } catch (Exception e){
            for(int i = 1; i <= 25; i++){
                try {
                    driver.findElement(By.xpath("//*[@id=\"inventory_76561198888388607_730_2\"]/div/div[" + i + "]")).click();
                    Thread.sleep(2000);
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
                    String fullName = driver.findElement(By.cssSelector("#iteminfo" + j + "_item_name")).getText();
                    if(Objects.isNull(fullName) || fullName.equals("")){
                        continue;
                    }
                    String tradableAfterString;
                    try {
                        tradableAfterString = driver.findElement(By.xpath("//*[@id=\"iteminfo" + j + "_item_owner_descriptors\"]/div[2]")).getText();
                    } catch (Exception exe){
                        tradableAfterString = null;
                    }
                    if(tradableAfterString == null){
                        InventoryItem inventoryItem = inventoryItemService.findInventoryItemByFullNameAndExterior(fullName, exterior);
                        if(Objects.nonNull(inventoryItem)){
                            // get starting at price
                            String textWithStartingAtPrice = driver.findElement(By.xpath("//*[@id=\"iteminfo" + j + "_item_market_actions\"]/div/div[2]")).getText();
                            int startIndex = textWithStartingAtPrice.indexOf(": ") + 2;
                            int endIndex = textWithStartingAtPrice.indexOf("₴");
                            String startingAtPriceString = textWithStartingAtPrice.substring(startIndex, endIndex).replace(",", ".");
                            float startingAtPrice = Float.parseFloat(startingAtPriceString);
                            if(startingAtPrice > inventoryItem.getSellPrice()){
                                // sell logic
                                // click on button sell
                                Thread.sleep(2000);
                                driver.findElement(By.xpath("//*[@id=\"iteminfo" + j + "_item_market_actions\"]/a/span[2]")).click();
                                // send sell price
                                Thread.sleep(2000);
                                driver.findElement(By.xpath("//*[@id=\"market_sell_buyercurrency_input\"]")).sendKeys(String.format("%,2f", startingAtPrice));
                                // click on checkbox I agree to the terms of the Steam Subscriber Agreement
                                Thread.sleep(2000);
                                driver.findElement(By.xpath("//*[@id=\"market_sell_dialog_accept_ssa\"]")).click();
                                // click on button OK, put it up for sale
                                Thread.sleep(2000);
                                driver.findElement(By.xpath("//*[@id=\"market_sell_dialog_accept\"]/span")).click();
                                // click on button OK
                                Thread.sleep(2000);
                                driver.findElement(By.xpath("//*[@id=\"market_sell_dialog_ok\"]/span")).click();
                                // get response message
                                for (int k = 0; k <= 3;){
                                    String responseMessage = driver.findElement(By.xpath("//*[@id=\"market_sell_dialog_error\"]")).getText();
                                    System.out.println(responseMessage);
                                    if(!responseMessage.equals("")){
                                        if(responseMessage.contains("Your listing has not been created. Refresh the page and try again.")){
                                            // click on button OK
                                            driver.findElement(By.xpath("//*[@id=\"market_sell_dialog_ok\"]/span")).click();
                                            Thread.sleep(2000);
                                            k++;
                                            if(k == 3){
                                                // click on crosshair
                                                driver.findElement(By.xpath("//*[@id=\"market_sell_dialog\"]/div[2]/div/div")).click();
                                            }
                                        } else {

                                            System.out.println("success");
                                        }
                                    }
                                }

                            } else {
                                // if the price is less than the profitable one, then send an email, if it is profitable (get at least half of the profitPercent), then sell
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void endDriver() {
        driver.close();
        driver.quit();
    }

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
