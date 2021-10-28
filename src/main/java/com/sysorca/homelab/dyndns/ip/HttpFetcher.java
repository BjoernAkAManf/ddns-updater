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

import com.sysorca.homelab.dyndns.IPFetcher;
import com.sysorca.homelab.dyndns.util.IP;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Retrieves the {@link IP} from an external Server that returns the Address as a text.
 * Whitespace returned will be trimmed (e.g. to support https://icanhazip.com/ ).
 */
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public final class HttpFetcher implements IPFetcher {
    private final HttpClient client;
    @ToString.Include
    private final URI uri;

    @Override
    public IP retrieveIP() throws IOException {
        try {
            final var resp = this.client.send(
                    HttpRequest.newBuilder().uri(this.uri).build(),
                    HttpResponse.BodyHandlers.ofString()
            );
            final var address = resp.body().trim();
            return IP.create(address);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IOException("Interrupted");
        }
    }
}
