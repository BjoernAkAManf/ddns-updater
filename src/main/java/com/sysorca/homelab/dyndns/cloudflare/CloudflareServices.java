package com.sysorca.homelab.dyndns.cloudflare;

import com.sysorca.homelab.dyndns.Service;
import com.sysorca.homelab.dyndns.ServiceProvider;
import com.sysorca.homelab.dyndns.ServiceStorage;
import com.sysorca.homelab.dyndns.cloudflare.api.Request;
import com.sysorca.homelab.dyndns.cloudflare.api.models.DNSRecord;
import com.sysorca.homelab.dyndns.cloudflare.api.models.DNSRecordCreateParameters;
import com.sysorca.homelab.dyndns.util.IP;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public final class CloudflareServices implements ServiceProvider, ServiceStorage {
    private final Request request;
    private final String zoneId;

    @Override
    public List<Service> collectServiceNames() throws IOException, InterruptedException {
        try {
            log.info("- Start - ");
            final var records = new ArrayList<DNSRecord>();
            Loader.load(
                    5,
                    this.request.zones(zoneId),
                    i -> records.addAll(Arrays.asList(i))
            );
            final var services = new ArrayList<Service>(records.size());
            for (final var record : records) {
                final var ip = IP.create(record.getContent());
                services.add(new CFService(record.getId(), record.getName(), ip));
            }
            log.info("- End - ");
            return services;
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw ex;
        }
    }

    @Override
    public void insert(final List<Service> services) throws IOException, InterruptedException {
        for (final var service : services) {
            final var ip = service.getAddress();
            final var ipv = ip.getVersion();
            final String type;
            switch (ipv) {
                case V4:
                    type = "A";
                    break;
                case V6:
                    type = "AAAA";
                    break;
                default:
                    throw new UnsupportedOperationException(String.format("Version %s not supported!", ipv));
            }
            final var host = service.getHost();
            final var address = ip.getAddress();
            log.info("Creating '{}' Record for '{}' with content '{}'", type, host, address);
            this.request.zones(this.zoneId).createDNSRecord(
                    DNSRecordCreateParameters
                            .builder()
                            .type(type)
                            .name(host)
                            .content(address)
                            .proxied(true)
                            .build()
            );
        }
    }

    @Override
    public void replace(final List<Service> source, final List<Service> target) throws IOException, InterruptedException {
        // TODO: We could be smarter here and use patch if applicable
        this.delete(source);
        this.insert(target);
    }

    @Override
    public void delete(final List<Service> services) throws IOException, InterruptedException {
        if (!services.stream().allMatch(CFService.class::isInstance)) {
            throw new IOException("Can only delete CFService implementations");
        }
        for (final var service : services) {
            final var s = (CFService) service;
            final var id = s.getId();
            final var host = s.getHost();
            log.info("Deleting Record '{}' for '{}'", id, host);
            this.request.zones(this.zoneId).deleteDNSRecord(id);
        }
    }
}
