package com.sysorca.homelab.dyndns.util;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Represents an IP Address a machine can connect to.
 */
@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class IP {
    /**
     * From https://stackoverflow.com/a/36760050/2716053
     */
    private static final Pattern IPV4 = Pattern.compile("^(?:(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])(\\.(?!$)|$)){4}$");

    /**
     * Based on https://stackoverflow.com/a/37355379/2716053.
     */
    private static final Pattern IPV6 = Pattern.compile("^([a-f0-9:]+:+)+[a-f0-9]+$");
    private final IPV version;
    private final String address;

    /**
     * Creates an instance of this.
     *
     * @param address Address of an IP Address
     * @return New Instance
     * @throws IOException If the IP Address could not be resolved.
     */
    public static IP create(final String address) throws IOException {
        final var v4 = IPV4.matcher(address);
        if (v4.matches()) {
            return new IP(IPV.V4, address);
        }
        final var v6 = IPV6.matcher(address);
        if (v6.matches()) {
            return new IP(IPV.V6, address);
        }
        throw new IOException(String.format("Could not detect IP Type for Input: '%s'", address));
    }
}

