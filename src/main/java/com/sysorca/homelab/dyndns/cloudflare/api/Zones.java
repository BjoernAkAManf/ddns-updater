package com.sysorca.homelab.dyndns.cloudflare.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sysorca.homelab.dyndns.cloudflare.api.models.DNSRecord;
import com.sysorca.homelab.dyndns.cloudflare.api.models.DNSRecordCreateParameters;
import com.sysorca.homelab.dyndns.cloudflare.api.models.DNSRecordListParameters;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.http.HttpResponse;

@RequiredArgsConstructor
public final class Zones {
    private static final TypeReference<Response<DNSRecord>> TF_DNS_RECORD = new TypeReference<>() {
    };
    private static final TypeReference<Response<DNSRecord[]>> TF_DNS_RECORD_ARRAY = new TypeReference<>() {
    };

    private final API request;
    private final String zoneId;

    public Response<DNSRecord[]> getDNSRecords(final DNSRecordListParameters params) throws IOException, InterruptedException {
        final var b = new URIBuilder("zones", this.zoneId, "dns_records");
        params.buildQueryParams(b::withQueryParam);
        return this.request.send(
                "GET",
                b,
                this.request.createBodyPublisher(),
                this.request.createBodyHandler(TF_DNS_RECORD_ARRAY)
        );
    }

    public Response<DNSRecord> createDNSRecord(final DNSRecordCreateParameters params) throws IOException, InterruptedException {
        return this.request.send(
                "POST",
                new URIBuilder("zones", this.zoneId, "dns_records"),
                this.request.createBodyPublisher(params),
                this.request.createBodyHandler(TF_DNS_RECORD)
        );
    }

    public void deleteDNSRecord(final String identifier) throws IOException, InterruptedException {
        this.request.sendRaw(
                "DELETE",
                new URIBuilder("zones", this.zoneId, "dns_records", identifier),
                this.request.createBodyPublisher(),
                HttpResponse.BodyHandlers.ofString()
        );
    }
}
