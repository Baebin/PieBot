package com.piebin.piebot.service.impl.scheduler;

import com.piebin.piebot.model.domain.Omok;
import com.piebin.piebot.model.repository.OmokRepository;
import com.piebin.piebot.service.OmokSchedulerService;
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
public class OmokSchedulerServiceImpl implements OmokSchedulerService {
    public static LocalDateTime omokRankDateTime;
    public static List<Omok> omokRankList = new ArrayList<>();

    private final OmokRepository omokRepository;

    private final Comparator<Omok> omokComparator = (o1, o2) -> {
        if (o1.getWin() == o2.getWin()) {
            long total1 = (o1.getWin() + o1.getTie() + o1.getLose());
            long total2 = (o2.getWin() + o2.getTie() + o2.getLose());
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
    public void scheduleOmokRankTask() {
        omokRankList = omokRepository.findAll();
        omokRankList.sort(omokComparator);
        omokRankDateTime = LocalDateTime.now();
        log.info("Omok Rank List Created");
    }
}
