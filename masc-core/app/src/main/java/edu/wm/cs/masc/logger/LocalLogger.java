package edu.wm.cs.masc.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LocalLogger {
    public static Logger logger = null;

    public static Logger getLocalLogger(){
        if(logger==null){
            try {
                FileHandler handler = new FileHandler("MainScope.log",false);
                logger = Logger.getLogger("MainScopeLogger");
                logger.addHandler(handler);
                logger.setUseParentHandlers(false);
                SimpleFormatter formatter = new SimpleFormatter();
                handler.setFormatter(formatter);
                return logger;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return logger;
    }
}
