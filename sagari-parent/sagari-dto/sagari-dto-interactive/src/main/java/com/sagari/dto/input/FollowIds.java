package com.sagari.dto.input;

import java.util.List;

/**
 * @author biubiupiu~
 */
public class FollowIds {

    private List<Integer> followIds;

    public List<Integer> getFollowIds() {
        return followIds;
    }

    public void setFollowIds(List<Integer> followIds) {
        this.followIds = followIds;
    }

    @Override
    public String toString() {
        return "FollowIds{" +
                "followIds=" + followIds +
                '}';
    }
}
