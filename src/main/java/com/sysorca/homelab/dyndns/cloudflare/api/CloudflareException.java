package com.sysorca.homelab.dyndns.cloudflare.api;

import lombok.Getter;
import lombok.ToString;

import java.io.IOException;

@Getter
@ToString
public final class CloudflareException extends IOException {
    private final Response<?> response;

    public CloudflareException(final String message, final Response<?> response) {
        super(message);
        this.response = response;
    }

    @Override
    @ToString.Include
    public String getMessage() {
        return super.getMessage();
    }
}
