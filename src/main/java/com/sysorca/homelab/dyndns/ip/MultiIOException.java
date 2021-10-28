package com.sysorca.homelab.dyndns.ip;

import lombok.Getter;
import lombok.ToString;

import java.io.IOException;
import java.util.Collection;

@Getter
@ToString
public final class MultiIOException extends IOException {
    private static final Throwable[] EMPTY_ARRAY = new Throwable[0];
    private final Throwable[] causes;

    public MultiIOException(final String msg, final Collection<? extends Throwable> causes) {
        this(msg, causes.size() > 0 ? causes.toArray(EMPTY_ARRAY) : EMPTY_ARRAY);
    }

    public MultiIOException(final String msg, final Throwable[] causes) {
        super(msg, causes.length > 0 ? causes[0] : null);
        this.causes = causes;
    }
}
