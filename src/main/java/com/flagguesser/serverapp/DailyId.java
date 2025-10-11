package com.flagguesser.serverapp;

import com.flagguesser.serverapp.dao.FlagDao;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DailyId implements Runnable {
    private FlagDao flagDao;
    private List<Integer> possibleFlagIds = new ArrayList();
    private int dailyId;
    private int dbLength;
    private long timestamp;

    public DailyId(FlagDao flagDao) {
        this.flagDao = flagDao;
        this.dbLength = flagDao.dbLength();
        this.getDailyId();
    }

    public void run() {
        this.getDailyId();
    }

    public int getId() {
        return this.dailyId;
    }

    private void getDailyId() {
        if (this.possibleFlagIds.isEmpty()) {
            this.resetFlagList();
        }

        this.dailyId = (Integer)this.possibleFlagIds.removeFirst();
        this.timestamp = System.currentTimeMillis() / 1000L;
    }

    private void resetFlagList() {
        for(int i = 1; i <= this.dbLength; ++i) {
            this.possibleFlagIds.add(i);
        }

        Collections.shuffle(this.possibleFlagIds);
    }

    public int getDbLength() {
        return this.dbLength;
    }

    public long getTimestamp() {
        return timestamp;
    }
}