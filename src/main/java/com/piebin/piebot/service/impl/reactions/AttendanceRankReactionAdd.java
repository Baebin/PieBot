package com.piebin.piebot.service.impl.reactions;

import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.service.PageReactionAdd;
import com.piebin.piebot.service.PieReactionAdd;
import com.piebin.piebot.service.impl.commands.AttendanceRankCommand;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendanceRankReactionAdd implements PieReactionAdd {
    private final PageReactionAdd pageReactionAdd;
    private final AttendanceRankCommand attendanceRankCommand;

    @Override
    public void execute(MessageReactionAddEvent event) {
        pageReactionAdd.execute(Sentence.ATTENDANCE_RANK, attendanceRankCommand, event);
    }
}
