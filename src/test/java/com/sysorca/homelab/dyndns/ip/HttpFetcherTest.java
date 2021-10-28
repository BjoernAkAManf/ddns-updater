package com.sysorca.homelab.dyndns.ip;

import com.sysorca.homelab.dyndns.util.IP;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class HttpFetcherTest {
    // TODO: Mock example responses without requiring internet

    @Test
    public void testProductionIP() {
        final var urls = new String[]{
            "https://icanhazip.com/",
            "https://api64.ipify.org",
        };
        final var ips = new ArrayList<IP>();
        Assertions.assertAll(
            Stream.of(urls)
                .map(URI::create)
                .map(uri -> (Executable) () -> {
                    final var client = HttpClient.newBuilder()
                        .build();

                    final var fetcher = new HttpFetcher(client, uri);
                    final var ip = fetcher.retrieveIP();
                    final var version = ip.getVersion();
                    final var address = ip.getAddress();

                    Assertions.assertNotNull(ip);
                    Assertions.assertNotNull(address);
                    Assertions.assertNotNull(version);

                    ips.add(ip);
                })
                .collect(Collectors.toSet())
        );

        IP lastIP = null;
        for (final var ip : ips) {
            if (lastIP != null) {
                Assertions.assertEquals(lastIP, ip);
            }
            lastIP = ip;
        }
    }
}
