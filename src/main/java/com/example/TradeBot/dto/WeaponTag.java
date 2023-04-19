package com.example.TradeBot.dto;

import java.util.List;

public class WeaponTag {
    public static final List<String> allTags = List.of("aug","awp","cz75a","deagle","elite", // beretas
            "famas","fiveseven","g3sg1","galilar","glock","m4a1_silencer", "m4a1",
            "mac10","mag7","mp5sd","mp7","mp9","negev","nova","hkp2000", // p2000
            "p250","p90","bizon","revolver","sawedoff","scar20","sg556","ssg08","tec9",
            "ump45","usp_silencer","xm1014"
    );
    public static final List<String> tags7percent = List.of("aug","awp","cz75a","deagle",
            "famas","galilar","glock","m4a1_silencer", "m4a1",
            "mac10","mp5sd","mp7","mp9","nova","hkp2000", // p2000
            "p250","p90","bizon","revolver","sawedoff","scar20","sg556","ssg08","tec9",
            "ump45","usp_silencer","xm1014"
    );
    public static final List<String> tags4_5percent = List.of("aug","awp","deagle",
            "famas","galilar","m4a1_silencer","m4a1","sg556","ssg08","ak47"
    );
}
