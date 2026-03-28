package com.piebin.piebot.yacht.scheduler.impl;

import com.piebin.piebot.yacht.domain.Yacht;
import com.piebin.piebot.yacht.repository.YachtRepository;
import com.piebin.piebot.yacht.scheduler.YachtSchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class YachtSchedulerServiceImpl implements YachtSchedulerService {
    public static LocalDateTime yachtRankDateTime;
    public static List<Yacht> yachtRankList = new ArrayList<>();

    private final YachtRepository yachtRepository;

    private final Comparator<Yacht> yachtComparator = (o1, o2) -> {
        if (o1.getWin() == o2.getWin()) {
            long total1 = (o1.getWin() + o1.getTie() + o1.getLose());
            long total2 = (o2.getWin() + o2.getTie() + o2.getLose());
            if (total1 == 0 || total2 == 0)
                return 0;
            double odds1 = (o1.getWin() / total1);
            double odds2 = (o2.getWin() / total2);
            if (odds1 == odds2)
                return 0;
            return (odds1 > odds2 ? -1 : 1);
        }
        return (o1.getWin() > o2.getWin() ? -1 : 1);
    };

    @Async
    @Override
    @Transactional
    @Scheduled(fixedDelay = 60 * 1000)
    public void scheduleYachtRankTask() {
        yachtRankList = yachtRepository.findAll();
        yachtRankList.sort(yachtComparator);
        yachtRankDateTime = LocalDateTime.now();
        log.info("Yacht Rank List Created");
    }
}
