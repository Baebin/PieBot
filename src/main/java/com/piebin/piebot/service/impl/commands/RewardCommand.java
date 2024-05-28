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
public class RewardCommand implements PieCommand {
    private static final long REWARD = 100;
    private static final long MINUTES = 10;

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
        if (optionalAttendance.isEmpty()) {
            Attendance attendance = Attendance.builder()
                    .account(account)
                    .rewardCount(1L)
                    .rewardDateTime(LocalDateTime.now())
                    .build();
            attendanceRepository.save(attendance);
        } else {
            Attendance attendance = optionalAttendance.get();
            if (DateTimeManager.hasWaitingMinutes(attendance.getRewardDateTime(), MINUTES)) {
                EmbedDto dto = new EmbedDto(CommandSentence.REWARD_WAITING, Color.RED);
                dto.changeDescription(NumberManager.getNumber(MINUTES));
                EmbedMessageHelper.replyEmbedMessage(event.getMessage(), dto);
                return;
            }
            attendance.setRewardCount(attendance.getRewardCount() + 1);
            attendance.setRewardDateTime(LocalDateTime.now());
        }
        account.setMoney(account.getMoney() + REWARD);

        EmbedDto dto = new EmbedDto(CommandSentence.REWARD_COMPLETED, Color.GREEN);
        dto.changeMessage(NumberManager.getNumber(REWARD));
        dto.changeDescription(NumberManager.getNumber(account.getMoney()));
        EmbedMessageHelper.replyEmbedMessage(event.getMessage(), dto);
    }
}
