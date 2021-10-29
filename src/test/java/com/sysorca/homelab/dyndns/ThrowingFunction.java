package com.sysorca.homelab.dyndns;

/**
 * Functional interface for a function throwing an Exception.
 *
 * @param <T>
 * @param <R>
 */
public interface ThrowingFunction<T, R> {
    R apply(T t) throws Exception;
}
