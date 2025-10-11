package com.flagguesser.serverapp.dao;

import com.flagguesser.serverapp.model.Flag;

import java.util.List;

public interface FlagDao {
    Flag getFlag(int id);
    int dbLength();
    List<String> getList();
}