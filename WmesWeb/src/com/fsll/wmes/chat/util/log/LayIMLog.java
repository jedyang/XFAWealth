package com.fsll.wmes.chat.util.log;

import org.apache.log4j.Logger;

/**
 * Created by mjhuang
 */
public  class LayIMLog {
    static Logger logger = Logger.getLogger(LayIMLog.class);
    public static void error(Object msg){
        logger.error(msg);

    }
    public static void info(Object msg){
        logger.info(msg);
    }
}
