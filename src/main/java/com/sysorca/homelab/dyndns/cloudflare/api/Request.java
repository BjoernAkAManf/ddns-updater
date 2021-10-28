package com.sysorca.homelab.dyndns.cloudflare.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;

public final class Request {
    private final API api;

    public Request(final HttpClient client, final String token) {
        this.api = new API(new ObjectMapper(), client, token);
    }

    public Zones zones(final String zoneId) {
        return new Zones(this.api, zoneId);
    }
}
