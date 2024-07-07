package com.piebin.piebot.service.impl.scheduler;

import com.piebin.piebot.model.domain.Attendance;
import com.piebin.piebot.model.repository.AttendanceRepository;
import com.piebin.piebot.service.AttendanceSchedulerService;
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
public class AttendanceSchedulerServiceImpl implements AttendanceSchedulerService {
    public static LocalDateTime attendanceRankDateTime;
    public static List<Attendance> attendanceRankList = new ArrayList<>();

    private final AttendanceRepository attendanceRepository;

    private final Comparator<Attendance> attendanceComparator = (o1, o2) -> {
        if (o1.getCount() == o2.getCount())
            return 0;
        return (o1.getCount() > o2.getCount() ? -1 : 1);
    };

    @Async
    @Override
    @Transactional
    @Scheduled(fixedDelay = 120 * 1000)
    public void scheduleAttendanceRankTask() {
        attendanceRankList = attendanceRepository.findAll();
        attendanceRankList.sort(attendanceComparator);
        attendanceRankDateTime = LocalDateTime.now();
        log.info("Attendance Rank List Created");
    }
}
