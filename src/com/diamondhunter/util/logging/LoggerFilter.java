package com.diamondhunter.util.logging;

public interface LoggerFilter {
    boolean accept(final String format, final Logger.Level level, final String log);
}
