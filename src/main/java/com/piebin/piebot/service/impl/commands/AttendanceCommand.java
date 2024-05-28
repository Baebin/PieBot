package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.domain.Attendance;
import com.piebin.piebot.model.dto.embed.EmbedDto;
import com.piebin.piebot.model.entity.CommandSentence;
import com.piebin.piebot.model.repository.AccountRepository;
import com.piebin.piebot.model.repository.AttendanceRepository;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.utility.DateTimeManager;
import com.piebin.piebot.utility.EmbedMessageHelper;
import com.piebin.piebot.utility.NumberManager;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceCommand implements PieCommand {
    private static final long REWARD = 2000;

    private final AccountRepository accountRepository;
    private final AttendanceRepository attendanceRepository;

    @Override
    @Transactional
    public void execute(MessageReceivedEvent event) {
        String userId = event.getAuthor().getId();
        Optional<Account> optionalAccount = accountRepository.findById(userId);
        if (optionalAccount.isEmpty())
            return;
        Account account = optionalAccount.get();
        Optional<Attendance> optionalAttendance = attendanceRepository.findByAccount(account);

        Attendance attendance;
        if (optionalAttendance.isEmpty()) {
            attendance = Attendance.builder()
                    .account(account)
                    .count(1L)
                    .dateTime(LocalDateTime.now())
                    .build();
            attendanceRepository.save(attendance);
        } else {
            attendance = optionalAttendance.get();
            if (attendance.getDateTime() != null && DateTimeManager.isToday(attendance.getDateTime())) {
                EmbedMessageHelper.replyCommandErrorMessage(event.getMessage(), CommandSentence.ATTENDANCE_WAITING);
                return;
            }
            attendance.setCount(attendance.getCount() + 1);
            attendance.setDateTime(LocalDateTime.now());
        }
        account.setMoney(account.getMoney() + REWARD);

        EmbedDto dto = new EmbedDto(CommandSentence.ATTENDANCE_COMPLETED, Color.GREEN);
        dto.changeTitle(NumberManager.getNumber(attendance.getCount()));
        dto.changeMessage(NumberManager.getNumber(REWARD));
        dto.changeDescription(NumberManager.getNumber(account.getMoney()));
        EmbedMessageHelper.replyEmbedMessage(event.getMessage(), dto);
    }
}
