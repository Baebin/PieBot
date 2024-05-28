package com.piebin.piebot.service.impl.reactions;

import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.service.PieReactionAdd;
import com.piebin.piebot.service.impl.commands.PatchNoteCommand;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatchNoteReactionAdd implements PieReactionAdd {
    private final PatchNoteCommand patchNoteCommand;

    @Override
    public void execute(MessageReactionAddEvent event) {
        PageReactionAdd pageReactionAdd = new PageReactionAdd(Sentence.PATCH_NOTE, patchNoteCommand);
        pageReactionAdd.execute(event);
    }
}
