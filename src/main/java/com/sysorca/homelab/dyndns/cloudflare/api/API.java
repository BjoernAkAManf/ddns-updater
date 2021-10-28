package com.sysorca.homelab.dyndns.cloudflare.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sysorca.homelab.dyndns.util.JsonBodyHandler;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.UncheckedIOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Raw client to access the API with.
 */
@RequiredArgsConstructor
final class API {
    private static final String BASE_URL = "https://api.cloudflare.com/client/v4/";
    private final ObjectMapper mapper;
    private final HttpClient client;
    private final String token;

    HttpResponse.BodyHandler<Response<Object>> createBodyHandler() {
        return this.createBodyHandler(new TypeReference<>() {
        });
    }

    HttpRequest.BodyPublisher createBodyPublisher() {
        return HttpRequest.BodyPublishers.noBody();
    }

    <T> HttpRequest.BodyPublisher createBodyPublisher(final T obj) {
        return HttpRequest.BodyPublishers.ofInputStream(() -> {
            final var in = new PipedInputStream();
            try (final var out = new PipedOutputStream(in)) {
                this.mapper.writeValue(out, obj);
            } catch (final IOException ex) {
                throw new UncheckedIOException(ex);
            }
            return in;
        });
    }

    <T> HttpResponse.BodyHandler<Response<T>> createBodyHandler(final TypeReference<Response<T>> ref) {
        return new JsonBodyHandler<>(this.mapper, ref);
    }

    <T> Response<T> send(
            final String method, final URIBuilder uri,
            final HttpRequest.BodyPublisher body, final HttpResponse.BodyHandler<Response<T>> response
    ) throws IOException, InterruptedException {
        final var resp = this.sendRaw(method, uri, body, response);
        checkSuccess(resp);
        return resp;
    }

    <T> T sendRaw(
            final String method, final URIBuilder uri,
            final HttpRequest.BodyPublisher body, final HttpResponse.BodyHandler<T> response
    ) throws IOException, InterruptedException {
        final var target = uri.build(BASE_URL);
        final var m = this.client.send(
                HttpRequest.newBuilder()
                        .method(method, body)
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", "Bearer " + token)
                        .uri(target)
                        .build(),
                response
        );
        final var resp = m.body();
        checkStatusCode(m.statusCode(), resp);
        return resp;
    }

    private void checkSuccess(final Response<?> resp) throws CloudflareException {
        if (!resp.isSuccess()) {
            throw new CloudflareException("API Error", resp);
        }
    }

    // Messages taken from documentation
    // https://api.cloudflare.com/#getting-started-responses
    private void checkStatusCode(final int code, final Object response) throws CloudflareException {
        final var resp = response instanceof Response ? (Response<?>) response : null;
        switch (code) {
            case 200:
                // request successful
                return;
            case 304:
                // Not modified
                return;
            case 400:
                throw new CloudflareException("request was invalid", resp);
            case 401:
                throw new CloudflareException("User does not have permission", resp);
            case 403:
                throw new CloudflareException("request not authenticated", resp);
            case 429:
                throw new IllegalStateException("client is rate limited");
            case 405:
                throw new CloudflareException("incorrect HTTP method provided", resp);
            case 415:
                throw new CloudflareException("Response is not valid json", resp);
        }
    }
}
