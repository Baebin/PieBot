package com.piebin.piebot.global.service.impl.reactions;

import com.piebin.piebot.global.entity.Sentence;
import com.piebin.piebot.global.service.PageReactionAdd;
import com.piebin.piebot.global.service.PieReactionAdd;
import com.piebin.piebot.global.service.impl.commands.PatchNoteCommand;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatchNoteReactionAdd implements PieReactionAdd {
    private final PageReactionAdd pageReactionAdd;
    private final PatchNoteCommand patchNoteCommand;

    @Override
    public void execute(MessageReactionAddEvent event) {
        pageReactionAdd.execute(Sentence.PATCH_NOTE, patchNoteCommand, event);
    }
}
