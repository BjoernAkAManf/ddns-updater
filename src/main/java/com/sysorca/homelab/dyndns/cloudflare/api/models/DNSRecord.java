package com.sysorca.homelab.dyndns.cloudflare.api.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PACKAGE)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class DNSRecord {
    private String id;
    private String type;
    private String name;
    private String content;
    private boolean proxiable;
    private boolean proxied;
    @JsonAlias("created_on")
    private String created;
    @JsonAlias("modified_on")
    private String modified;
}

