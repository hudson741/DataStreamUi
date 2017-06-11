package com.yss.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.base.Splitter;

public class UptimeUtil {
    private static final Splitter SPLITTER = Splitter.on(Pattern.compile("[d|h|m|s]")).omitEmptyStrings().trimResults();

    public static int compareUptime(String uptime1, String uptime2) {
        if ((uptime1 == null) && (uptime2 == null)) {
            return 0;
        }

        if (uptime1 == null) {
            return -1;
        }

        if (uptime2 == null) {
            return 1;
        }

        String[] parts1 = getUptimeStrings(uptime1);
        String[] parts2 = getUptimeStrings(uptime2);

        if (parts1.length != parts2.length) {
            return parts1.length - parts2.length;
        }

        for (int i = 0; i < parts1.length; i++) {
            int ret = Integer.parseInt(parts1[i]) - Integer.parseInt(parts2[i]);

            if (ret != 0) {
                return ret;
            }
        }

        return 0;
    }

    public static String[] getUptimeStrings(String uptime) {
        Iterable<String> uptimeParts = SPLITTER.split(uptime);
        List<String>     parts       = new ArrayList<String>(4);

        for (String part : uptimeParts) {
            parts.add(part);
        }

        return parts.toArray(new String[parts.size()]);
    }
}
