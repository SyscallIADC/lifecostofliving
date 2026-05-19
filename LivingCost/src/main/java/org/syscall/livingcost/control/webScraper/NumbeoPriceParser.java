package org.syscall.livingcost.control.webScraper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumbeoPriceParser {
    public static double parsePrice(String rawPrice) {
        if (rawPrice == null || rawPrice.isEmpty()) {
            return 0.0;
        }

        Pattern pattern = Pattern.compile("\\d+(,\\d+)*(\\.\\d+)?");
        Matcher matcher = pattern.matcher(rawPrice);

        if (matcher.find()) {
            String cleanNumber = matcher.group().replace(",", "");
            try {
                return Double.parseDouble(cleanNumber);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }

        return 0.0;
    }
}
