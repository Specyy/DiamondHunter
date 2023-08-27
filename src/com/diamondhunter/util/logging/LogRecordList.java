package com.diamondhunter.util.logging;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LogRecordList {

    private Logger logger;
    private List<LogRecord> records;

    LogRecordList(Logger logger) {
        this.logger = logger;
        records = new LinkedList<>();
    }

    LogRecord removeRecord(int index){
        return records.remove(index);
    }

    boolean removeRecord(LogRecord record){
        return records.remove(record);
    }

    void addRecord(LogRecord record, int index){
        records.add(index, record);
    }

    void addRecord(LogRecord record){
        records.add(record);
    }

    public LogRecord[] getRecord(String log) {
        List<LogRecord> recs = new LinkedList<>();

        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getLogMessage().equals(log))
                recs.add(records.get(i));
        }

        return recs.toArray(new LogRecord[0]);
    }

    public LogRecord[] getRecord(LoggerFilter filter){
        List<LogRecord> accepted = new LinkedList<>();

        for (int i = 0; i < records.size(); i++) {
            LogRecord record = records.get(i);

            if(filter.accept(record.getFormat(), record.getLevel(), record.getLogMessage()))
                accepted.add(record);

        }

        return accepted.toArray(new LogRecord[0]);
    }

    public List<LogRecord> getRecords(){
        return Collections.unmodifiableList(records);
    }

    public LogRecord getRecord(int index) {
        return records.get(index);
    }

    public Logger getLogger() {
        return logger;
    }
}
