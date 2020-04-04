package com.sagari.dto.input;

import java.util.List;

/**
 * @author biubiupiu~
 */
public class FansIds {

    private List<Integer> fansIds;

    public List<Integer> getFansIds() {
        return fansIds;
    }

    public void setFansIds(List<Integer> fansIds) {
        this.fansIds = fansIds;
    }

    @Override
    public String toString() {
        return "FansIds{" +
                "fansIds=" + fansIds +
                '}';
    }
}
