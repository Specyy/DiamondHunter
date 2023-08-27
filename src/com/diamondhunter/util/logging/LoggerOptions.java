package com.diamondhunter.util.logging;

import java.time.ZonedDateTime;
import java.time.temporal.IsoFields;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

public class LoggerOptions {
    public static final int DEFAULT_INDENT = 0;
    public static final String DEFAULT_INDENT_STRING = "\t";
    public static final String DEFAULT_LOG_FORMAT = "[" + Format.YEAR + ":" + Format.MONTH + ":" + Format.WEEK + ":" + Format.HOUR12 + ":" + Format.MINUTE + ":" + Format.SECOND + " " + Format.HOUR_AM_PM + " - " + Format.LEVEL + "] " + Format.PROMPT;

    public static final String DEFAULT_AM_HOUR_TEXT = "AM";
    public static final String DEFAULT_PM_HOUR_TEXT = "PM";

    private int indent;
    private String indentString;

    private String logPrefix = "";
    private String logSuffix = "";

    private String amHourText;
    private String pmHourText;

    private String logFormat;

    private String title = "";

    private List<LoggerBreak> customBreaks;

    private boolean translateLog;

    public LoggerOptions() {
        indent = DEFAULT_INDENT;
        indentString = DEFAULT_INDENT_STRING;
        logFormat = DEFAULT_LOG_FORMAT;

        amHourText = DEFAULT_AM_HOUR_TEXT;
        pmHourText = DEFAULT_PM_HOUR_TEXT;

        customBreaks = new LinkedList<>();
        translateLog = false;
    }

    String formatPrompt(String prompt, Logger.Level level, boolean log) {
        ZonedDateTime time = ZonedDateTime.now();

        String logFormat = prompt.replace(Format.LEVEL.format, level.prompt);
        logFormat = logFormat.replace(Format.TITLE.format, title);
        logFormat = logFormat.replace(Format.MONTH.format, time.getMonthValue() >= 10 ? String.valueOf(time.getMonthValue()) : "0" + time.getMonthValue());
        logFormat = logFormat.replace(Format.YEAR.format, String.valueOf(time.getYear() >= 10 ? time.getYear() : "0" + time.getYear()));
        logFormat = logFormat.replace(Format.WEEK.format, String.valueOf(time.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) >= 10 ? time.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) : "0" + time.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)));

        logFormat = logFormat.replace(Format.DAY_OF_MONTH.format, String.valueOf(time.getDayOfMonth() >= 10 ? time.getDayOfMonth() : "0" + time.getDayOfMonth()));
        logFormat = logFormat.replace(Format.DAY_OF_YEAR.format, String.valueOf(time.getDayOfYear() >= 10 ? time.getDayOfYear() : "0" + time.getDayOfYear()));
        logFormat = logFormat.replace(Format.DAY_OF_WEEK.format, "0" + time.getDayOfWeek().getValue());
        logFormat = logFormat.replace(Format.HOUR24.format, String.valueOf(time.getHour() >= 10 ? time.getHour() : "0" + time.getHour()));

        boolean amHour = time.getHour() < 12;

        if (amHour) {
            logFormat = logFormat.replace(Format.HOUR12.format, String.valueOf(time.getHour() >= 10 ? time.getHour() : "0" + time.getHour()));
            logFormat = logFormat.replace(Format.HOUR_AM_PM.format, amHourText);
        } else {
            logFormat = logFormat.replace(Format.HOUR12.format, String.valueOf(time.getHour() - 12 >= 10 ? time.getHour() - 12 : "0" + (time.getHour() - 12)));
            logFormat = logFormat.replace(Format.HOUR_AM_PM.format, pmHourText);
        }

        logFormat = logFormat.replace(Format.MILLISECOND.format, String.valueOf(Calendar.getInstance(TimeZone.getTimeZone(time.getZone())).getTimeInMillis() % 1000));
        logFormat = logFormat.replace(Format.MINUTE.format, String.valueOf(time.getMinute() >= 10 ? time.getMinute() : "0" + time.getMinute()));
        logFormat = logFormat.replace(Format.SECOND.format, String.valueOf(time.getSecond() >= 10 ? time.getSecond() : "0" + time.getSecond()));
        logFormat = logFormat.replace(Format.NANOSECOND.format, String.valueOf(time.getNano() % 1000000000));

        for (int i = 0; i < customBreaks.size(); i++) {
            LoggerBreak br = customBreaks.get(i);
            logFormat = logFormat.replace(br.getKey(), br.getBreak());
        }

        StringBuilder indentText = new StringBuilder();

        for (int i = 0; i < indent; i++) {
            indentText.append(indentString);
        }

        return log ? logPrefix + logFormat + logSuffix : indentText + logPrefix + logFormat + logSuffix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogSuffix() {
        return logSuffix;
    }

    public void setLogSuffix(String logSuffix) {
        this.logSuffix = logSuffix;
    }

    public String getLogPrefix() {
        return logPrefix;
    }

    public void setLogPrefix(String logPrefix) {
        this.logPrefix = logPrefix;
    }

    public String getIndentString() {
        return indentString;
    }

    public void setIndentString(String indentString) {
        this.indentString = indentString;
    }

    public String getAMHourText() {
        return amHourText;
    }

    public void setAMHourText(String amHourText) {
        this.amHourText = amHourText;
    }

    public String getPMHourText() {
        return pmHourText;
    }

    public void setPMHourText(String pmHourText) {
        this.pmHourText = pmHourText;
    }

    public String getLogFormat() {
        return logFormat;
    }

    public void setLogFormat(String logFormat) {
        this.logFormat = logFormat;
    }

    public void setTranslateLog(boolean translateLog) {
        this.translateLog = translateLog;
    }

    public boolean willTranslateLog() {
        return translateLog;
    }

    public void setCustomBreak(String key, String cbreak) {
        setCustomBreak(new LoggerBreak(key, cbreak));
    }

    public void setCustomBreak(LoggerBreak br) {
        customBreaks.remove(br);
        customBreaks.add(br);
    }

    public boolean removeCustomBreak(LoggerBreak br) {
        return customBreaks.remove(br);
    }

    public boolean removeCustomBreak(String key) {
        return removeCustomBreak(getCustomBreak(key));
    }

    public boolean removeCustomBreak(String key, String cbreak) {
        return removeCustomBreak(new LoggerBreak(key, cbreak));
    }

    public LoggerBreak getCustomBreak(String key) {
        for (LoggerBreak breaks : getCustomBreaks()) {
            if (breaks.getKey().equals(key))
                return breaks;
        }

        return null;
    }

    public List<LoggerBreak> getCustomBreaks() {
        return customBreaks;
    }

    public int getIndent() {
        return indent;
    }

    public void setIndent(int indent) {
        this.indent = indent;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (obj instanceof LoggerOptions) {
            LoggerOptions other = (LoggerOptions) obj;
            return other.indent == indent && other.customBreaks.equals(customBreaks) && other.pmHourText.equals(pmHourText) && other.amHourText.equals(amHourText)
                    && other.logFormat.equals(logFormat) && other.indentString.equals(indentString) && other.logPrefix.equals(logPrefix) && other.logSuffix.equals(logSuffix)
                    && other.title.equals(title);
        }

        return false;
    }

    public enum Format {
        YEAR("%year"), MONTH("%month"), WEEK("%week"), DAY_OF_YEAR("%doy"), DAY_OF_MONTH("%dom"), DAY_OF_WEEK("%dow"), HOUR12("%hour12"), HOUR_AM_PM("%ampm"), HOUR24("%hour24"), MINUTE("%minute"), SECOND("%second"), MILLISECOND("%millis"), NANOSECOND("%nano"), LEVEL("%level"), TITLE("%title"), PROMPT("%prompt");

        private String format;

        Format(String format) {
            this.format = format;
        }

        public static Format parse(String identifier) {
            return parse(identifier, false);
        }

        public static Format parse(String identifier, boolean ignoreCase) {
            for (int i = 0; i < values().length; i++) {
                if (ignoreCase) {
                    if (values()[i].format.equalsIgnoreCase(identifier))
                        return values()[i];
                } else {
                    if (values()[i].format.equals(identifier))
                        return values()[i];
                }
            }

            return null;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        @Override
        public String toString() {
            return getFormat();
        }
    }
}
