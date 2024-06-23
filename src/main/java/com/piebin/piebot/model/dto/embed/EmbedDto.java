package com.piebin.piebot.model.dto.embed;

import com.piebin.piebot.model.entity.CommandSentence;
import com.piebin.piebot.model.entity.EmbedSentence;
import lombok.*;

import java.awt.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmbedDto {
    private String title;
    private String message;
    private String description;
    private Color color;

    public EmbedDto(EmbedSentence sentence) {
        this.title = sentence.getTitle();
        this.message = sentence.getMessage();
        this.description = sentence.getDescription();
    }

    public EmbedDto(CommandSentence sentence) {
        this.title = sentence.getTitle();
        this.message = sentence.getMessage();
        this.description = sentence.getDescription();
    }

    public EmbedDto(EmbedSentence sentence, Color color) {
        this(sentence);
        this.color = color;
    }

    public EmbedDto(CommandSentence sentence, Color color) {
        this(sentence);
        this.color = color;
    }

    // 가변 인자
    private String change(String message, String ... strings) {
        char key = 'A';
        for (int i = 0; i < strings.length; i++) {
            String _key = "{" + (key++) + "}";
            message = message.replace(_key, strings[i]);
        }
        return message;
    }

    public void changeTitle(String ... strings) {
        this.title = change(this.title, strings);
    }

    public void changeMessage(String ... strings) {
        this.message = change(this.message, strings);
    }

    public void changeDescription(String ... strings) {
        this.description = change(this.description, strings);
    }
}
