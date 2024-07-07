package com.piebin.piebot.service.impl.reactions;

import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.service.PieReactionAdd;
import com.piebin.piebot.service.impl.commands.AttendanceRankCommand;
import com.piebin.piebot.service.impl.commands.MoneyRankCommand;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendanceRankReactionAdd implements PieReactionAdd {
    private final AttendanceRankCommand attendanceRankCommand;

    @Override
    public void execute(MessageReactionAddEvent event) {
        PageReactionAdd pageReactionAdd = new PageReactionAdd(Sentence.ATTENDANCE_RANK, attendanceRankCommand);
        pageReactionAdd.execute(event);
    }
}
