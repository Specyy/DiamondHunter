package com.diamondhunter.util.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A Logger is used to log or prompt messages to this application. Loggers
 * are typically named using a hierarchical dot-separated namespace although,
 * doing so is not needed. We actually encourage the use of custom-named loggers
 * for this use because it is only needed in this application.
 * <p>
 * A Logger instance can be obtained by calling the {@link #createLogger(String)}
 * which either retrieves a existing logger or creates a new logger.
 */
public class Logger {

    private static final Map<String, Logger> loggers = new LinkedHashMap<>();

    private LogRecordList logRecords;

    private String identifier;
    private LoggerOptions options;

    private OutputStream out;
    private OutputStream err;

    private Logger(String identifier) {
        this(identifier, System.out, System.err);
    }

    private Logger(String identifier, OutputStream out, OutputStream err) {
        this.identifier = identifier;
        options = new LoggerOptions();
        logRecords = new LogRecordList(this);

        this.out = out;
        this.err = err;
        loggers.put(identifier, this);
    }

    public static boolean containsLogger(String identifier) {
        return getLogger(identifier) != null;
    }

    public static Logger getLogger(String identifier) {
        return loggers.get(identifier);
    }

    public static Logger createLogger(String identifier) {
        if (loggers.containsKey(identifier))
            return loggers.get(identifier);

        return new Logger(identifier);
    }

    public static Logger createLogger(String identifier, OutputStream out, OutputStream err) {
        if (loggers.containsKey(identifier))
            return loggers.get(identifier);

        return new Logger(identifier, out, err);
    }

    public void log(Level level, String prompt) {
        String log;

        if (options.willTranslateLog())
            log = options.formatPrompt(prompt, level, true);
        else log = prompt;

        String format = options.formatPrompt(options.getLogFormat(), level, false);
        String finalLog = format.replace(LoggerOptions.Format.PROMPT.getFormat(), log);

        if (level == Level.ERROR) {
            if (!logError(finalLog)) {
                finalLog = format.replace(LoggerOptions.Format.PROMPT.getFormat(), "An error occurred while trying to log a message!");

                if (!logOutput(finalLog)) {
                    System.out.println(finalLog);
                }
            }
        } else {
            if (!logOutput(finalLog)) {
                finalLog = format.replace(LoggerOptions.Format.PROMPT.getFormat(), "An error occurred while trying to log a message!");
                System.out.println(finalLog);
            }
        }

        LogRecord record = new LogRecord(this, level, options.getLogFormat(), finalLog);
        logRecords.addRecord(record);
    }

    public void logLine(Level level, String prompt) {
        log(level, prompt + System.lineSeparator());
    }

    private boolean logOutput(String prompt) {
        try {
            out.write(prompt.getBytes());
            out.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean logError(String prompt) {
        try {
            err.write(prompt.getBytes());
            err.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public LoggerOptions getOptions() {
        return options;
    }

    public void setOptions(LoggerOptions options) {
        this.options = options;
    }

    public void setOutputStream(OutputStream out) {
        try {
            this.out.close();
        } catch (IOException | NullPointerException e) {
            logLine(Level.WARNING, "Could not close current output stream (" + this.out.toString() + ")");
        }

        this.out = out;
    }

    public OutputStream getOutputStream() {
        return out;
    }

    public OutputStream getErrorStream() {
        return err;
    }

    public void setErrorStream(OutputStream err) {
        try {
            this.err.close();
        } catch (IOException | NullPointerException e) {
            logLine(Level.WARNING, "Could not close current error stream (" + this.err.toString() + ")");
        }

        this.err = err;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (obj instanceof Logger) {
            return this.options.equals(((Logger) obj).options) && this.logRecords.equals(((Logger) obj).logRecords);
        }

        return false;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        if (loggers.containsKey(identifier))
            throw new IllegalArgumentException("Cannot set Logger \"" + this.identifier + "\"'s name to \"" + identifier + "\" because a Logger with that name already exists!");

        this.identifier = identifier;
    }

    public LogRecordList getLogRecords() {
        return logRecords;
    }

    public enum Level {
        ERROR(-1, "ERROR"), INFO(0, "INFO"), WARNING(1, "WARNING"), CLIENT(2, "CLIENT"), SERVER(3, "SERVER");

        final int id;
        String prompt;

        Level(final int id, String prompt) {
            this.id = id;
            this.prompt = prompt;
        }

        public String getPrompt() {
            return prompt;
        }

        public void setPrompt(String prompt) {
            this.prompt = prompt;
        }

        @Override
        public String toString() {
            return getPrompt();
        }
    }

}
