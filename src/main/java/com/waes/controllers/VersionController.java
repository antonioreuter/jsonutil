package com.waes.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by aandra1 on 30/09/16.
 */
@Slf4j
@RestController("versionController")
@RequestMapping("/version")
public class VersionController {

    @Value("${application.version}")
    private String version;

    @RequestMapping(method = RequestMethod.GET)
    public String healthcheck() {
        log.info("Getting the version...");
        return version;
    }
}