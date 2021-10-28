package com.sysorca.homelab.dyndns.cloudflare.api.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class DNSRecordCreateParameters {
    private final String type;
    private final String name;
    private final String content;

    @Builder.Default
    private final int ttl = 1;

    @Builder.Default
    private final boolean proxied = false;
}
