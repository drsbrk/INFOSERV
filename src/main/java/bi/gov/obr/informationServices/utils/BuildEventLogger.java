package bi.gov.obr.informationServices.utils;

import java.time.LocalDateTime;

import bi.gov.obr.informationServices.entity.AppEventLogger;
import bi.gov.obr.informationServices.enums.UserAction;

public class BuildEventLogger {

    public static AppEventLogger buildEventLogger(String username, String ipAdress, UserAction userAction, LocalDateTime date) {
        AppEventLogger appEventLogger = new AppEventLogger();
        appEventLogger.setUsername(username);
        appEventLogger.setIpAdress(ipAdress);
        appEventLogger.setUSER_ACTION(userAction);
        appEventLogger.setDateAction(date);
        return appEventLogger;
    }

    public static AppEventLogger buildEventLogger(String username, String ipAdress, UserAction userAction, String entityJson) {
        AppEventLogger appEventLogger = new AppEventLogger();
        appEventLogger.setUsername(username);
        appEventLogger.setIpAdress(ipAdress);
        appEventLogger.setUSER_ACTION(userAction);
        appEventLogger.setDateAction(LocalDateTime.now());
        appEventLogger.setEntityJson(entityJson);
        return appEventLogger;
    }

}
