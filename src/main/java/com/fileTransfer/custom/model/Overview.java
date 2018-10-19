package com.fileTransfer.custom.model;

import java.util.HashMap;
import java.util.List;

/**
 * Created by nano on 18-4-10.
 */
public class Overview {

    private HashMap<String, Long> summary;
    private List<String> dateData;
    private List<Long> findData;
    private List<Long> finishedData;

    public Overview() {
        super();
    }
    public Overview(HashMap<String, Long> summary, List<String> dateData, List<Long> findData, List<Long> finishedData) {
        super();
        this.summary = summary;
        this.dateData = dateData;
        this.findData = findData;
        this.finishedData = finishedData;
    }

    public HashMap<String, Long> getSummary() {
        return summary;
    }

    public void setSummary(HashMap<String, Long> summary) {
        this.summary = summary;
    }

    public List<String> getDateData() {
        return dateData;
    }

    public void setDateData(List<String> dateData) {
        this.dateData = dateData;
    }

    public List<Long> getFindData() {
        return findData;
    }

    public void setFindData(List<Long> findData) {
        this.findData = findData;
    }

    public List<Long> getFinishedData() {
        return finishedData;
    }

    public void setFinishedData(List<Long> finishedData) {
        this.finishedData = finishedData;
    }
}
