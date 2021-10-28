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
