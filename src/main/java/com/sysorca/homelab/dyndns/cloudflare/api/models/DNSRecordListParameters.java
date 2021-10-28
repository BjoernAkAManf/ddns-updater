package com.sysorca.homelab.dyndns.cloudflare.api.models;

import lombok.Builder;
import lombok.Data;

import java.util.function.BiConsumer;

@Data
@Builder
public final class DNSRecordListParameters {
    @Builder.Default
    private final Integer page = null;

    @Builder.Default
    private final Integer perPage = null;

    public void buildQueryParams(final BiConsumer<String, String> append) {
        if (page != null) {
            append.accept("page", this.page.toString());
        }
        if (perPage != null) {
            append.accept("per_page", this.perPage.toString());
        }
    }

    public boolean hasPageSet() {
        return this.page != null;
    }

    public boolean hasPerPageSet() {
        return this.page != null;
    }
}
