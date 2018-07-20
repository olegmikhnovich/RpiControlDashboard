package com.mikhnovich.oleg.utils;

import java.util.regex.Pattern;

public class IPAddressValidator {
    private final Pattern patternv4, patternv6;

    public IPAddressValidator() {
        String ipv4Pattern = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
        String ipv6Pattern = "^([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4}$";
        patternv4 = Pattern.compile(ipv4Pattern);
        patternv6 = Pattern.compile(ipv6Pattern);
    }

    public boolean validateV4(String ipAddress) {
        return patternv4.matcher(ipAddress).matches();
    }

    public boolean validateV6(String ipAddress) {
        return patternv6.matcher(ipAddress).matches();
    }
}

