package com.diamondhunter.util.logging;

public class LogRecord {

    private Logger.Level level;
    private Logger logger;
    private String log;
    private String format;

    LogRecord(Logger logger, Logger.Level level, String format, String log){
        this.level = level;
        this.log = log;
        this.logger = logger;
        this.format = format;
    }

    public String getFormat(){
        return format;
    }

    public Logger getLogger() {
        return logger;
    }

    public Logger.Level getLevel() {
        return level;
    }

    public String getLogMessage() {
        return log;
    }
}
