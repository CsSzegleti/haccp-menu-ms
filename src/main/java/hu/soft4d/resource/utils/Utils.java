package hu.soft4d.resource.utils;

import hu.soft4d.model.MenuItem;

public class Utils {
    public static void copyMenuItemProperties(MenuItem dest, MenuItem src) {
        dest.category = src.category;
        dest.allergens = src.allergens;
        dest.description = src.description;
        dest.name = src.name;
        dest.currency = src.currency;
        dest.price = src.price;
        dest.isPreparable = src.isPreparable;
        dest.imgUrls = src.imgUrls;
        dest.storingCondition.maxStoringHours = src.storingCondition.maxStoringHours;
        dest.storingCondition.humidity = src.storingCondition.humidity;
        dest.storingCondition.temperature = src.storingCondition.temperature;
    }
}
