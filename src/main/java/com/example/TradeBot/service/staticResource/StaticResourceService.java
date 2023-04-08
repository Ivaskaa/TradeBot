package com.example.TradeBot.service.staticResource;

import org.springframework.core.io.Resource;
/**
 * created by Illya 02.2023
 */
public interface StaticResourceService {

    /** Get static resource from "resource/static"
     * @param filename name of file
     * @return resource file
     */
    Resource load(String filename);
}
