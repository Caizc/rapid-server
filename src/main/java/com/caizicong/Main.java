package com.caizicong;

import com.caizicong.network.ServerThread;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("MainClass")
public class Main {

    private static final Log log = LogFactory.getLog(Main.class);

    @PostConstruct
    public void init() {

        log.info("Hello Kitty! - log.info");

        System.out.println("Hello Kitty!");

        ServerThread.startup();

        log.info(" ====== RapidServer is READY! ======");
    }

}
