/*
 * Copyright 2021 sysorca.com and contributors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sysorca.homelab.dyndns.ip;

import com.sysorca.homelab.dyndns.util.IP;
import com.sysorca.homelab.dyndns.util.IPV;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
        "https://10.10.10.10;V4",
        "https://192.168.1.1;V4",
        "https://[b1be:d83f:4212:ec1e:e1ac:a7d8:2ed9:fe82];V6",
        "https://[c0b:157a:12b9:f5:d683:e433:3dff:a997];V6",
    })
    public void testMockedIP(final String address, final String version) throws IOException, InterruptedException {
        final var uri = URI.create(address);
        final var host = uri.getHost().replace("[", "").replace("]", "");
        final var client = Mockito.mock(HttpClient.class);
        final var body = Mockito.mock(HttpResponse.class);
        Mockito
            .when(client.send(
                Mockito.eq(HttpRequest.newBuilder().uri(uri).build()),
                Mockito.eq(HttpResponse.BodyHandlers.ofString())
            ))
            .then(inv -> body);
        Mockito.when(body.body()).thenReturn(host);

        final var fetcher = new HttpFetcher(client, uri);
        final var ip = fetcher.retrieveIP();
        Assertions.assertEquals(host, ip.getAddress());
        Assertions.assertEquals(IPV.valueOf(version), ip.getVersion());

        Mockito.verify(client, Mockito.times(1))
            .send(Mockito.eq(HttpRequest.newBuilder().uri(uri).build()), Mockito.eq(HttpResponse.BodyHandlers.ofString()));
        Mockito.verify(body, Mockito.times(1)).body();
        Mockito.verifyNoMoreInteractions(client, body);
    }
}
