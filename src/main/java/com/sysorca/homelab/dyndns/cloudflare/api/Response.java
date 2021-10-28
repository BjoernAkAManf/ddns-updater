package com.sysorca.homelab.dyndns.cloudflare.api;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Data
// TODO: Immutable Data Structure
@Setter(AccessLevel.PACKAGE)
public final class Response<T> {
    private boolean success;
    private Object[] messages;
    private Error[] errors;
    private T result;

    private final Map<String, Object> unknownFields = new HashMap<>();

    // Capture all other fields that Jackson do not match other members
    @JsonAnyGetter
    public Map<String, Object> otherFields() {
        return unknownFields;
    }

    @JsonAnySetter
    public void setOtherField(final String name, final Object value) {
        unknownFields.put(name, value);
    }

    @JsonAlias("result_info")
    private PaginationInfo pagination;

    @Data
    @Setter(AccessLevel.PACKAGE)
    public static final class PaginationInfo {
        private int page = -1;
        @JsonAlias("per_page")
        private int perPage = -1;
        private int count = -1;
        @JsonAlias("total_count")
        private int totalCount = -1;
        @JsonAlias("total_pages")
        private int totalPages = -1;
    }

    @Data
    @Setter(AccessLevel.PACKAGE)
    public static final class Error {
        private String code;
        private String message;
        @JsonAlias("error_chain")
        private Error[] parents;
    }
}
