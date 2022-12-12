package me.Mugi.FinerFlips;

import com.google.gson.JsonElement;
import me.Mugi.FinerFlips.utils.ApiHandler;
import me.Mugi.FinerFlips.utils.Utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Tasks {
    public static Thread updateBalance = new Thread(() -> {
        while (true) {
            if (Config.enabled) {
                try {
                    ApiHandler.updatePurse();
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    try {
                        Utils.sendMessageWithPrefix("&cFailed to update balance, please check if you set your API key correctly.");
                        Thread.sleep(60000); // Wait until the user sets the API key
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }, "FinerFlips Balance Updating Task");
    public static Thread updateBazaarItem = new Thread(() -> {
        while (true) {
            if (Config.enabled || Config.bestSellingMethod) {
                try {
                    ApiHandler.updateBazaar();
                    Thread.sleep(2500);
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(60000); // sleep 60s if the API is down or got blacklisted
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }, "FinerFlips Bazaar Updating Task");

    public static Thread updateFilters = new Thread(() -> {
        while (true) {
            if (Config.hideSpam) {
                List<String> filters = new LinkedList<>();
                try {
                    for (Map.Entry<String, JsonElement> f : Utils.getJson("https://nec.robothanzo.dev/filter").getAsJsonObject().getAsJsonObject("result").entrySet()) {
                        for (JsonElement filter : f.getValue().getAsJsonArray()) {
                            filters.add(filter.getAsString().toLowerCase(Locale.ROOT));
                        }
                    }
                    Main.chatFilters = filters;
                    Thread.sleep(60000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }, "FinerFlips Filters Updating Task");
}
