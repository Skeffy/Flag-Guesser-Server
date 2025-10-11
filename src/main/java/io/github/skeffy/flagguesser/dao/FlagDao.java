package io.github.skeffy.flagguesser.dao;

import io.github.skeffy.flagguesser.model.Flag;

import java.util.List;

public interface FlagDao {
    Flag getFlag(int id);
    int dbLength();
    List<String> getList();
}