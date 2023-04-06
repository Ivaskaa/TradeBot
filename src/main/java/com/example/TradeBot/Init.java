package com.example.TradeBot;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@Slf4j
@AllArgsConstructor
public class Init implements CommandLineRunner {


    @Override
    public void run(String... args) throws Exception {
        log.info("################## START OF INITIALIZATION ##################");

        String apiKey = "349B231D049DC4DCA51D085EE9A8008B";
        String steamId = "39041850239856029736598342";
        String appId = "730"; // ідентифікатор додатку CS:GO
        String contextId = "2"; // ідентифікатор контексту інвентаря
        String language = "en"; // мова запиту

        HttpClient httpClient = HttpClientBuilder.create().build();
        URIBuilder uriBuilder = new URIBuilder("https://steamcommunity.com/id/398246592304682534098234/inventory/json/730/2");
//        uriBuilder.setParameter("appid", appId);
//        uriBuilder.setParameter("key", apiKey);
//        uriBuilder.setParameter("steamid", steamId);

//        HttpClient httpClient = HttpClientBuilder.create().build();
//        URIBuilder uriBuilder = new URIBuilder("http://api.steampowered.com/ISteamEconomy/GetAssetPrices/v0001/?appid=440&key=XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX&format=xml");
//        uriBuilder.setParameter("appid", appId);
//        uriBuilder.setParameter("key", apiKey);
//        uriBuilder.setParameter("steamid", steamId);

        URI uri = uriBuilder.build();
        System.out.println(uriBuilder);
        HttpGet httpGet = new HttpGet(uri);
        HttpResponse response = httpClient.execute(httpGet);
        String responseBody = EntityUtils.toString(response.getEntity());

        System.out.println(responseBody);


//        SteamWebApiClient.SteamWebApiClientBuilder steamWebApiClientBuilder = new SteamWebApiClient.SteamWebApiClientBuilder(apiKey);
//        SteamWebApiClient client = new SteamWebApiClient(steamWebApiClientBuilder);
//        SteamWebApiInterfaceMethod method = SteamWebApiInterfaceMethod.GET_GAME_ITEMS;
//
//        SteamWebApiRequest request = new SteamWebApiRequest.Builder()
//                .withInterface(SteamWebApiInterface.I_STEAM_USER_INVENTORY)
//                .withMethod(method)
//                .addParameter("steamid", steamId)
//                .addParameter("appid", appId)
//                .addParameter("contextid", contextId)
//                .build();
//
//        SteamWebApiResponse response = client.processRequest(request);
//        String jsonString = response.getBody();

////        SteamWeb steamWeb = new SteamWeb("yourSteamId", "yourSteamWebApiKey");
//
//        String apiKey = "YOUR_API_KEY";
//        String steamId = "YOUR_API_KEY";
//        SteamWebApiInterface steamWebApiInterface = new SteamWebApiInterface(apiKey);
//
//        String appId = "730"; // appId для CS:GO
//        String contextId = "2"; // contextId для інвентаря
//        int count = 5000; // кількість предметів, яку потрібно отримати
//
//        String jsonResponse = steamWebApiInterface.getPlayerInventory(
//                steamId,
//                appId,
//                contextId,
//                count
//        );
//
//// Парсинг JSON
//        JSONObject jsonObject = new JSONObject(jsonResponse);
//        JSONArray itemsArray = jsonObject.getJSONObject("rgInventory").toJSONArray();
//
//        for (int i = 0; i < itemsArray.length(); i++) {
//            JSONObject itemObject = itemsArray.getJSONObject(i);
//            String itemId = itemObject.getString("id");
//            String itemName = itemObject.getString("name");
//            // додаткові поля можна отримати з об'єкту itemObject
//        }
//
//
////        SteamWebApiClient.SteamWebApiClientBuilder clientBuilder = new SteamWebApiClient.SteamWebApiClientBuilder("398246592304682534098234");
////        SteamWebApiClient steamWebAPI = clientBuilder.build();

        log.info("################## END OF INITIALIZATION ##################");
    }


}
