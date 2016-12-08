package edu.sjsu.pm.partyonretailer.config;

import java.util.List;

import edu.sjsu.pm.partyonretailer.Merchandise;

/**
 * Created by Ming on 12/7/16.
 */

public class AppData {
    public static String backendServerURL = "https://partyonbackend.herokuapp.com/parse/";//http://10.50.0.21:1337/parse/";
    public static String backendServerAppID = "PartyOn";
    public static List<Merchandise> itemList;
    public static boolean isParseAdapterInitiated;
    public static float getAmout(){
        float amount = 0;
        for(Merchandise m : itemList){
            amount += m.getPrice();
        }
        return amount;
    }
}
