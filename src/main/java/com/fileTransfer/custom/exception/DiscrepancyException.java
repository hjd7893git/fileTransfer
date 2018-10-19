package com.fileTransfer.custom.exception;

/**
 * Created by nano on 18-4-19.
 */
public class DiscrepancyException extends Exception {
    public DiscrepancyException(String src, String dst) {
        super("字段[ " + src + " ]与[ " + dst + " ]不一致");
    }
}
