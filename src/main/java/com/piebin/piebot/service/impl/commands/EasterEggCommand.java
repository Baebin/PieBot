package com.piebin.piebot.service.impl.commands;

import com.piebin.piebot.model.dto.embed.EmbedDto;
import com.piebin.piebot.model.entity.CommandSentence;
import com.piebin.piebot.model.repository.EasterEggRepository;
import com.piebin.piebot.service.PieCommand;
import com.piebin.piebot.utility.EmbedMessageHelper;
import com.piebin.piebot.utility.impl.EmbedMessageHelperImpl;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EasterEggCommand implements PieCommand {
    private final EasterEggRepository easterEggRepository;

    private final EmbedMessageHelper embedMessageHelper;

    @Override
    @Transactional(readOnly = true)
    public void execute(MessageReceivedEvent event) {
        EmbedDto dto = new EmbedDto(CommandSentence.SECRET_EASTEREGG);

        String cnt = Long.toString(easterEggRepository.count());
        dto.changeDescription(cnt);

        embedMessageHelper.replyEmbedMessage(event.getMessage(), dto);
    }
}
