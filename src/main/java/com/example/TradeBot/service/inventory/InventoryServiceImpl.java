package com.example.TradeBot.service.inventory;

import com.example.TradeBot.dto.request.RequestListOfItems;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Service
@Slf4j
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final ObjectMapper objectMapper;

    public void getInventoryFromCSMoney() throws URISyntaxException, IOException {
        CookieStore cookieStore = new BasicCookieStore();
        cookieStore.addCookie(new BasicClientCookie("region", "Khmelnytskyi Oblast"));
        cookieStore.addCookie(new BasicClientCookie("_gcl_au", "1.1.1894378622.1680620649"));
        cookieStore.addCookie(new BasicClientCookie("sc", "2D70E1D8-ED76-DFCE-A76D-A8F02E4C180E"));
        cookieStore.addCookie(new BasicClientCookie("_scid", "86c30f61-614d-4696-afda-770aca7ab0c2"));
        cookieStore.addCookie(new BasicClientCookie("_tt_enable_cookie", "1"));
        cookieStore.addCookie(new BasicClientCookie("_ttp", "JhoheAx04W8b82ehXUrzZTBaqN4"));
        cookieStore.addCookie(new BasicClientCookie("_sctr", "1|1680555600000"));
        cookieStore.addCookie(new BasicClientCookie("_gcl_aw", "GCL.1680620662.Cj0KCQjwla-hBhD7ARIsAM9tQKuZQnMjfDZjJshhAN5jskrYmTMUo-HBe_dwgBKA4y-qDOF8iZVpYKoaAkFCEALw_wcB"));
        cookieStore.addCookie(new BasicClientCookie("_hjSessionUser_2848248", "eyJpZCI6IjJhYTlhMDM3LTZjYmEtNTJmOS1iNzUzLTdlOGI0MzBjYTk2YyIsImNyZWF0ZWQiOjE2ODA2MjA2NDkyNzEsImV4aXN0aW5nIjp0cnVlfQ=="));
        cookieStore.addCookie(new BasicClientCookie("_gac_UA-77178353-1", "1.1680620663.Cj0KCQjwla-hBhD7ARIsAM9tQKuZQnMjfDZjJshhAN5jskrYmTMUo-HBe_dwgBKA4y-qDOF8iZVpYKoaAkFCEALw_wcB"));
        cookieStore.addCookie(new BasicClientCookie("registered_user", "true"));
        cookieStore.addCookie(new BasicClientCookie("FPID", "FPID1.2.wCDV5JsBKr1cuAPLz%2FBv9LPtKG6C4HL2GiKRlhhJiDA%3D.1680620649"));
        cookieStore.addCookie(new BasicClientCookie("language", "en"));
        cookieStore.addCookie(new BasicClientCookie("sellerid", "error"));
        cookieStore.addCookie(new BasicClientCookie("theme", "darkTheme"));
        cookieStore.addCookie(new BasicClientCookie("pro_version", "false"));
        cookieStore.addCookie(new BasicClientCookie("_gid", "GA1.2.1439846146.1680767168"));
        cookieStore.addCookie(new BasicClientCookie("amp_c14fa5", "DlnYxmap6EUTHI7n1ITG7N.NzY1NjExOTg4ODgzODg2MDc=..1gtbqgpvc.1gtbqgpvk.2.2.4"));
        cookieStore.addCookie(new BasicClientCookie("amp_9e76ea", "cjkp2NDWC15V41dsn1voD5.NzY1NjExOTg4ODgzODg2MDc=..1gtbqgpuv.1gtbqgqi9.d.2.f"));
        cookieStore.addCookie(new BasicClientCookie("settings_visual_high_demand", "false"));
        cookieStore.addCookie(new BasicClientCookie("isAnalyticEventsLimit", "true"));
        cookieStore.addCookie(new BasicClientCookie("settings_visual_card_size", "small"));
        cookieStore.addCookie(new BasicClientCookie("AB_TEST_CONCRETE_SKIN_1_ITERATION", "a"));
        cookieStore.addCookie(new BasicClientCookie("AB_TEST_MARKET_NEW_API_KEY_MODAL", "a"));
        cookieStore.addCookie(new BasicClientCookie("new_language", "uk"));
        cookieStore.addCookie(new BasicClientCookie("_hjSession_2848248", "eyJpZCI6Ijc5ZDQ3MjAwLWRlMzUtNDYyYS1hMzVmLWZkNWUzYmNlYjI0YiIsImNyZWF0ZWQiOjE2ODA5NDIxNjExOTAsImluU2FtcGxlIjp0cnVlfQ=="));
        cookieStore.addCookie(new BasicClientCookie("_hjAbsoluteSessionInProgress", "0"));
        cookieStore.addCookie(new BasicClientCookie("_hjHasCachedUserAttributes", "true"));
        cookieStore.addCookie(new BasicClientCookie("steamid", "76561198888388607"));
        cookieStore.addCookie(new BasicClientCookie("avatar", "https://avatars.akamai.steamstatic.com/c1ab3ba6e7ee7fb3267c989927e1e76b7486bb4f_medium.jpg"));
        cookieStore.addCookie(new BasicClientCookie("username", "C%2B%2B"));
        cookieStore.addCookie(new BasicClientCookie("FPLC", "GJeghDUI%2FMku8NCMVcKgjR%2Fyte5PeI3onew0uZPbXZzsxBJObGn30L%2F8PwmQgRptngoWVZBN0Fh2TDQBU5skWSRK80eVI66U1szh%2F00CgRAgpP%2BSCeOJLkXxyK0LLA%3D%3D"));
        cookieStore.addCookie(new BasicClientCookie("GleamId", "nKZep6AjBoiecHtN8"));
        cookieStore.addCookie(new BasicClientCookie("GleamA", "%7B%22nKZep%22%3A%22login%22%7D"));
        cookieStore.addCookie(new BasicClientCookie("amp_d77dd0", "o-FDpxvq29NxzSt7VlBYx_.NzY1NjExOTg4ODgzODg2MDc=..1gtg01cqj.1gtg01d1i.1.1.2"));
        cookieStore.addCookie(new BasicClientCookie("amp_d77dd0_cs.money", "o-FDpxvq29NxzSt7VlBYx_.NzY1NjExOTg4ODgzODg2MDc=..1gtg01cqj.1gtg01d4d.1.1.2"));
        cookieStore.addCookie(new BasicClientCookie("trade_carts_open", "true"));
        cookieStore.addCookie(new BasicClientCookie("auction__faq_banner", "true"));
        cookieStore.addCookie(new BasicClientCookie("_scid_r", "86c30f61-614d-4696-afda-770aca7ab0c2"));
        cookieStore.addCookie(new BasicClientCookie("_uetsid", "17a15270d44f11ed873393f285820117"));
        cookieStore.addCookie(new BasicClientCookie("_uetvid", "f3ca4a70d2f911ed8d0c5d106424e03a"));
        cookieStore.addCookie(new BasicClientCookie("thirdparty_token", "2916404f6035ec0d1c083b7925431a6a782db724c66b5c62157d3483926843e5"));
        cookieStore.addCookie(new BasicClientCookie("amplitude_id_222f15bd4f15cdfaee99c07bcc641e5fcs.money", "eyJkZXZpY2VJZCI6ImM1NzFmZDkxLWEwNWItNDkwOC1iM2ExLTI1NGRkZDkyNGNhZFIiLCJ1c2VySWQiOiI3NjU2MTE5ODg4ODM4ODYwNyIsIm9wdE91dCI6ZmFsc2UsInNlc3Npb25JZCI6MTY4MDk0MzcxODg2OSwibGFzdEV2ZW50VGltZSI6MTY4MDk0Mzg4Mjk1NywiZXZlbnRJZCI6NCwiaWRlbnRpZnlJZCI6Mywic2VxdWVuY2VOdW1iZXIiOjd9"));

        cookieStore.addCookie(new BasicClientCookie("amplitude_id_c14fa5162b6e034d1c3b12854f3a26f5cs.money", "eyJkZXZpY2VJZCI6IjJhMWU4YTNjLWM1NDAtNGI0NC05NDgwLWMwNWNlNWU3MTFjOFIiLCJ1c2VySWQiOiI3NjU2MTE5ODg4ODM4ODYwNyIsIm9wdE91dCI6ZmFsc2UsInNlc3Npb25JZCI6MTY4MDk0MjE2MDk3NSwibGFzdEV2ZW50VGltZSI6MTY4MDk0MzY1NDk1NywiZXZlbnRJZCI6NDY2LCJpZGVudGlmeUlkIjoyNjUsInNlcXVlbmNlTnVtYmVyIjo3MzF9")); // changing
        cookieStore.addCookie(new BasicClientCookie("csgo_ses", "357eb794732619836175dedab6541d4951b9d83526ea01cd51e8624a391bd453"));  // changing
        cookieStore.addCookie(new BasicClientCookie("support_token", "dac0a433c8214089ae0d432a3de3894b1078e7409d30a45fc4142734f9e4e819"));  // changing
        cookieStore.addCookie(new BasicClientCookie("page_before_registration", "/uk/")); // changing
        cookieStore.addCookie(new BasicClientCookie("_ga_HY7CCPCD7H", "GS1.1.1680942161.15.1.1680943881.39.0.0")); //changing
        cookieStore.addCookie(new BasicClientCookie("_ga", "GA1.1.611460168.1680620649")); // changing

        cookieStore.addCookie(new BasicClientCookie("_dc_gtm_UA-77178353-1", "1")); //не завжди є

        HttpClient httpClient = HttpClientBuilder.create().build();

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
        RequestListOfItems requestListOfItems = objectMapper.readValue(EntityUtils.toString(response.getEntity()), RequestListOfItems.class);
        System.out.println(requestListOfItems);
    }
}
