package com.piebin.piebot.service.impl.scheduler;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.repository.AccountRepository;
import com.piebin.piebot.service.MoneySchedulerService;
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
public class MoneySchedulerServiceImpl implements MoneySchedulerService {
    public static LocalDateTime moneyRankDateTime;
    public static List<Account> moneyRankList = new ArrayList<>();

    private final AccountRepository accountRepository;

    private final Comparator<Account> moneyComparator = (o1, o2) -> {
        if (o1.getMoney() == o2.getMoney())
            return 0;
        return (o1.getMoney() > o2.getMoney() ? -1 : 1);
    };

    @Async
    @Override
    @Transactional
    @Scheduled(fixedDelay = 60 * 1000)
    public void scheduleMoneyRankTask() {
        moneyRankList = accountRepository.findAll();
        moneyRankList.sort(moneyComparator);
        moneyRankDateTime = LocalDateTime.now();
        log.info("Money Rank List Created");
    }
}
