package com.example.TradeBot.service.selenium;


import com.example.TradeBot.dto.MyChromeProfile;
import com.example.TradeBot.dto.MySteamProfile;
import com.example.TradeBot.dto.WeaponTag;
import com.example.TradeBot.model.Item;
import com.example.TradeBot.model.ItemStatus;
import com.example.TradeBot.service.item.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    private final ItemService itemService;

    public SeleniumServiceImpl(ObjectMapper objectMapper, ItemService itemService) {
        this.objectMapper = objectMapper;
        this.itemService = itemService;
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
            log.info("previous login is saved");
        }
        return true;
    }

    @Override
    public void  sendBuyRequest(float startPrice, float endPrice, float profitPercent) throws InterruptedException {
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
                    float price = getFloat(itemPrice);
                    if (price > endPrice) {
                       isBreak = true;
                       break;
                    }
                    if (price < startPrice) {
                        continue;
                    }
                    try {
                        driver.findElement(By.xpath("//*[@id=\"result_" + j + "\"]")).click();
                        // get users last sale price
                        String usersLastSalePriceString = driver.findElement(By.xpath("//*[@id=\"searchResultsRows\"]/div[" + 2 + "]/div[2]/div[2]/span/span[1]")).getText();
                        if(!usersLastSalePriceString.contains("₴")) break;
                        float usersLastSalePrice = getFloat(usersLastSalePriceString);
                        System.out.println("users last sale price:" + usersLastSalePrice);
                        float buyPrice = usersLastSalePrice - (usersLastSalePrice * ((15 + profitPercent) / 100.0f));
                        System.out.println("-15 + profit percent price: " + buyPrice);
                        // click on button view more details
                        driver.findElement(By.xpath("//*[@id=\"market_buyorder_info_show_details\"]/span")).click();
                        float averageCheapestPriceOfPurchase = 0f;
                        for(int k = 2; k <= 6; k++){
                            // get cheapest price request
                            String cheapestPriceOfPurchase = driver.findElement(By.xpath("//*[@id=\"market_commodity_buyreqeusts_table\"]/table/tbody/tr[" + k + "]/td[1]")).getText();
                            averageCheapestPriceOfPurchase = averageCheapestPriceOfPurchase + getFloat(cheapestPriceOfPurchase);
                        }
                        averageCheapestPriceOfPurchase = averageCheapestPriceOfPurchase / 5;
                        System.out.println("requests to buy at:" + averageCheapestPriceOfPurchase);
                        // get count buy requests of skin
                        String buyRequestsCountString = driver.findElement(By.xpath("//*[@id=\"market_commodity_buyrequests\"]/span[1]")).getText();
                        int buyRequestsCount = Integer.parseInt(buyRequestsCountString);
                        System.out.println("buy requests count: " + buyRequestsCount);
                        System.out.println("------------------------------------------------");
                        if (buyPrice > averageCheapestPriceOfPurchase && buyRequestsCount > 4000) {
                            buyPrice = (buyPrice + averageCheapestPriceOfPurchase) / 2; // average price
                            buyPrice = buyPrice * 0.97f; // price -3%
                            String fullName = driver.findElement(By.xpath("//*[@id=\"largeiteminfo_item_name\"]")).getText();
                            String exterior = driver.findElement(By.xpath("//*[@id=\"largeiteminfo_item_descriptors\"]/div[1]")).getText();
                            exterior = exterior.replace("Exterior: ", "");
                            if(fullName.contains("Souvenir ")) continue;
                            if(!itemService.isExistByFullNameAndExteriorAndStatusBuyRequest(fullName, exterior)){
                                // buy logic
                                // click place buy order button
                                driver.findElement(By.xpath("//*[@id=\"market_buyorder_info\"]/div[1]/div[1]/a/span")).click();
                                // clear price in input
                                driver.findElement(By.xpath("//*[@id=\"market_buy_commodity_input_price\"]")).clear();
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
                                                Item item = new Item();
                                                item.setBuyPrice(buyPrice);
                                                item.setSellPrice(usersLastSalePrice);
                                                item.setFullName(fullName);
                                                item.setExterior(exterior);
                                                item.setStatus(ItemStatus.BUY_REQUEST);
                                                itemService.saveItem(item);
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
            if(isBreak) break;
            }
        }
    }

    public float getBalance() {
        String balanseString = driver.findElement(By.xpath("//*[@id=\"header_wallet_balance\"]")).getText();
        return getFloat(balanseString);
    }

    public void updateInventory() throws IOException {
        File fileSteamProfile = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("my_steam_profile.json")).getFile());
        MySteamProfile mySteamProfile = objectMapper.readValue(fileSteamProfile, MySteamProfile.class);
        driver.get("https://steamcommunity.com/id/" + mySteamProfile.getProfileId() + "/inventory/#730");
        try {
            driver.findElement(By.xpath("//*[@id=\"inventory_load_error_ctn\"]/div/div/div/div[2]/span")).click();
            log.info("This inventory is not available at this time. Please try again later.");
        } catch (Exception e){
            Set<Item> items = new HashSet<>();
            for(int i = 1; i <= 25; i++){
                try {
                    if(i != 1) driver.findElement(By.xpath("//*[@id=\"inventory_76561198888388607_730_2\"]/div/div[" + i + "]")).click();
                    Thread.sleep(2000);
                } catch (Exception ex){
                    break;
                }
                for(int j = 0; j <= 1; j++){
                    if(i == 1){ j = 1;}
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
                    Item item = new Item();
                    item.setFullName(name);
                    item.setExterior(exterior);
                    item.setCanSellAt(tradableAfter);
                    items.add(item);
                    System.out.println(item);
                }
            }

            System.out.println("------------------------------------------------------------------------");
            List<Item> databaseItems = itemService.getAllItemsByStatus(ItemStatus.BUY_REQUEST);
            for(Item item : items){
                for(Item purchaseRequest: databaseItems){
                    if(item.getFullName().equals(purchaseRequest.getFullName()) && item.getExterior().equals(purchaseRequest.getExterior())){
                        itemService.changeStatusToInventoryByFullNameAndExteriorAndStatus(purchaseRequest.getFullName(), purchaseRequest.getExterior(), item.getCanSellAt(), ItemStatus.BUY_REQUEST);
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
            boolean agreeToTermsSteam = false;
            for(int i = 1; i <= 25; i++){
                try {
                    if(i != 1) driver.findElement(By.xpath("//*[@id=\"inventory_76561198888388607_730_2\"]/div/div[" + i + "]")).click();
                    Thread.sleep(2000);
                } catch (Exception ex){
                    break;
                }
                for(int j = 0; j <= 1; j++){
                    if(i == 1){ j = 1;}
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
                        Item inventoryItem = itemService.findItemByFullNameAndExteriorAndStatus(fullName, exterior, ItemStatus.INVENTORY);
                        if(Objects.nonNull(inventoryItem)){
                            // get starting at price
                            String textWithStartingAtPrice = driver.findElement(By.xpath("//*[@id=\"iteminfo" + j + "_item_market_actions\"]/div/div[2]")).getText();
                            int startIndex = textWithStartingAtPrice.indexOf(": ") + 2;
                            int endIndex = textWithStartingAtPrice.indexOf("₴");
                            String startingAtPriceString = textWithStartingAtPrice.substring(startIndex, endIndex).replace(",", ".");
                            float startingAtPrice = Float.parseFloat(startingAtPriceString);
                            if(startingAtPrice > inventoryItem.getSellPrice()) {
                                inventoryItem.setSellPrice(startingAtPrice - 0.01f);
                            }
                            // sell logic
                            // click on button sell
                            driver.findElement(By.xpath("//*[@id=\"iteminfo" + j + "_item_market_actions\"]/a/span[2]")).click();
                            // send sell price
                            driver.findElement(By.xpath("//*[@id=\"market_sell_buyercurrency_input\"]")).sendKeys(String.format("%,2f", inventoryItem.getSellPrice()));
                            // get received price
                            String receivedPriceString = driver.findElement(By.xpath("//*[@id=\"market_sell_currency_input\"]")).getAttribute("value");
                            float receivedPrice = getFloat(receivedPriceString);
                            if(!agreeToTermsSteam){
                                // click on checkbox I agree to the terms of the Steam Subscriber Agreement
                                driver.findElement(By.xpath("//*[@id=\"market_sell_dialog_accept_ssa\"]")).click();
                                agreeToTermsSteam = true;
                            }
                            // click on button OK, put it up for sale
                            driver.findElement(By.xpath("//*[@id=\"market_sell_dialog_accept\"]/span")).click();
                            // click on button OK
                            driver.findElement(By.xpath("//*[@id=\"market_sell_dialog_ok\"]/span")).click();
                            // get response message
                            for (int k = 0; k <= 3;){
                                try {
                                    // get text from modal window
                                    String successMessage = driver.findElement(By.xpath("/html/body/div[5]/div[2]/div/div[2]")).getText();
                                    if(StringUtils.equalsIgnoreCase(successMessage, "ADDITIONAL CONFIRMATION NEEDED")){
                                        // click on button ok
                                        driver.findElement(By.xpath("/html/body/div[5]/div[3]/div/div[2]/div/span")).click();
                                        // success request
                                        itemService.changeStatusToSellRequest(inventoryItem, receivedPrice);
                                        // send email by success
                                        break;
                                    }
                                } catch (Exception ignored){}

                                String responseMessage = driver.findElement(By.xpath("//*[@id=\"market_sell_dialog_error\"]")).getText();
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
                                    } else if(responseMessage.contains("Please confirm or cancel the existing listing.")){
                                        // click on crosshair
                                        driver.findElement(By.xpath("//*[@id=\"market_sell_dialog\"]/div[2]/div/div")).click();
                                        break;
                                    } else {
                                        // click on crosshair
                                        driver.findElement(By.xpath("//*[@id=\"market_sell_dialog\"]/div[2]/div/div")).click();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void updateBuyRequests() throws InterruptedException {
        driver.get("https://steamcommunity.com/market/");
        Thread.sleep(500);
        List<Item> databaseItems = itemService.getAllItemsByStatus(ItemStatus.BUY_REQUEST);
        for (int i = 2; true; i++) {
            String fullItemText;
            try {
                fullItemText = driver.findElement(By.xpath("//*[@id=\"tabContentsMyListings\"]/div[2]/div[" + i + "]/div[4]/span[1]/a")).getText();
            } catch (Exception e) {
                break;
            }
            String[] splitByParenthesis = fullItemText.split("\\s*\\(\\s*");
            String fullName = splitByParenthesis[0].trim();
            String exterior = splitByParenthesis[1].replaceAll("\\)", "");
            databaseItems.removeIf(item -> item.getFullName().equals(fullName) && item.getExterior().equals(exterior));
        }
        for (Item item : databaseItems){
            itemService.changeStatusToNotEnoughMoneyDuringBuyByFullNameAndExteriorAndStatus(item.getFullName(), item.getExterior(), ItemStatus.BUY_REQUEST);
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

    public void getWeaponsPopularity() throws InterruptedException {
        for (String weaponTag: WeaponTag.tags4_5percent) {
            System.out.println(weaponTag);
            List<Float> coefs = new ArrayList<>();
            for (int i = 1; true; i++) {
                boolean isBreak = false;
                driver.get("https://steamcommunity.com/market/search?q=&category_730_ItemSet%5B%5D=any&category_730_ProPlayer%5B%5D=any&category_730_StickerCapsule%5B%5D=any&category_730_TournamentTeam%5B%5D=any&category_730_Weapon%5B%5D=tag_weapon_" + weaponTag + "&appid=730#p" + i + "_price_asc");
                Thread.sleep(500);
                for (int j = 0; j < 10; j++) {
                    try {
                        driver.findElement(By.xpath("//*[@id=\"result_" + j + "\"]")).click();
                        // get users request to buy price
                        String usersRequestToBuyPriceString = driver.findElement(By.cssSelector("#market_commodity_buyrequests > span:nth-child(2)")).getText();
                        float requestsToBuyAt = getFloat(usersRequestToBuyPriceString);
                        if (requestsToBuyAt > 500) {
                            isBreak = true;
                        }
                        System.out.println("requests to buy at:" + requestsToBuyAt);
                        // get count buy requests of skin
                        String buyRequestsCountString = driver.findElement(By.xpath("//*[@id=\"market_commodity_buyrequests\"]/span[1]")).getText();
                        int buyRequestsCount = Integer.parseInt(buyRequestsCountString);
                        System.out.println("buy requests count: " + buyRequestsCount);



                        if (isWeaponPopular(requestsToBuyAt, buyRequestsCount, weaponTag)) {
                            System.out.println("\n\nenough popular\n\n");
                        }
                        var price = (requestsToBuyAt * buyRequestsCount)/10000;
                        System.out.println(price);
                        coefs.add(price);

                        driver.get("https://steamcommunity.com/market/search?q=&category_730_ItemSet%5B%5D=any&category_730_ProPlayer%5B%5D=any&category_730_StickerCapsule%5B%5D=any&category_730_TournamentTeam%5B%5D=any&category_730_Weapon%5B%5D=tag_weapon_" + weaponTag + "&appid=730#p" + i + "_price_asc");
                    } catch (Exception ignored){}
                }
                if(isBreak) break;
            }
            float sum = 0;
            for (var item: coefs) {
                sum += item;
            }
            System.out.println("\n\nAverage "+ sum/coefs.size() + weaponTag+"\n\n");
        }
    }

    private float getFloat(String string) {
        string = string.replace("₴", "");
        string = string.replace(",", ".");
        return Float.parseFloat(string);
    }

    private boolean isWeaponPopular(float requestsToBuyAt, int buyRequestsCount, String weaponTag) {
        float popularIndex = Float.MAX_VALUE;
        boolean result = false;
        var price = (requestsToBuyAt * buyRequestsCount)/10000;
        switch (weaponTag) {
            case "aug" -> popularIndex = 144;
            case "awp" -> popularIndex = 748;
            case "deagle" -> popularIndex = 342;
            case "famas", "galilar" -> popularIndex = 150;
            case "m4a1_silencer" -> popularIndex = 714;
            case "m4a1" -> popularIndex = 350;
            case "sg556" -> popularIndex = 152;
            case "ssg08" -> popularIndex = 192;
            case "ak47" -> popularIndex = 868;
        }

        if (price > popularIndex) {
            result = true;
        }


        return result;
    }
}
