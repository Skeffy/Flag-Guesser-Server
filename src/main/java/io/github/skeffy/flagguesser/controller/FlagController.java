package io.github.skeffy.flagguesser.controller;

import io.github.skeffy.flagguesser.DailyId;
import io.github.skeffy.flagguesser.dao.FlagDao;
import io.github.skeffy.flagguesser.model.Flag;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
public class FlagController {
    private final FlagDao flagDao;
    private DailyId dailyId;
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public FlagController(FlagDao flagDao) {
        this.flagDao = flagDao;
        this.dailyId = new DailyId(flagDao);
        ScheduledFuture<?> result = this.executor.scheduleAtFixedRate(this.dailyId, 0L, 1L, TimeUnit.DAYS);
    }

    @GetMapping(path = {"/daily"})
    public Flag daily() {
        Flag flag = this.flagDao.getFlag(this.dailyId.getId());
        if (flag == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Flag not found");
        } else {
            flag.setTimestamp(this.dailyId.getTimestamp());
            return flag;
        }
    }

    @GetMapping(path = {"/practice"})
    public Flag practice() {
        Random random = new Random();
        int dbLength = random.nextInt(this.dailyId.getDbLength() + 1);
        Flag flag = this.flagDao.getFlag(dbLength);
        if (flag == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Flag not found");
        } else {
            return flag;
        }
    }

    @GetMapping(path = {"/list"})
    public List<String> names() {
        List<String> names = flagDao.getList();
        if (names == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not get list of possible flags");
        } else {
            return names;
        }
    }
}
