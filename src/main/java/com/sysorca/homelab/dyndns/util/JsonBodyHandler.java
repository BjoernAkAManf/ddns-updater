package com.sysorca.homelab.dyndns.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.http.HttpResponse;

/**
 * Implements parsing a JSON Object from the {@link HttpResponse}.
 *
 * @param <T> Type of the Value
 */
@RequiredArgsConstructor
public final class JsonBodyHandler<T> implements HttpResponse.BodyHandler<T> {
    private final ObjectMapper mapper;
    private final TypeReference<T> type;

    @Override
    public HttpResponse.BodySubscriber<T> apply(final HttpResponse.ResponseInfo responseInfo) {
        return HttpResponse.BodySubscribers.mapping(
            HttpResponse.BodySubscribers.ofInputStream(),
            is -> {
                try (final var stream = is) {
                    return this.mapper.readValue(stream, type);
                } catch (final IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        );
    }
}
