package com.piebin.piebot.factory.impl;

import com.piebin.piebot.factory.ManualFactory;
import com.piebin.piebot.factory.YachtCommandFactory;
import com.piebin.piebot.model.dto.embed.EmbedDto;
import com.piebin.piebot.model.entity.Sentence;
import com.piebin.piebot.model.entity.UniEmoji;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class ManualFactoryImpl implements ManualFactory {
    private final YachtCommandFactory yachtCommandFactory;

    private String getExampleManualOfCommand(List<String> commands) {
        return "-# - ex) " + commands.stream().map(cmd -> "ㅋ " + cmd).collect(Collectors.joining(", "));
    }

    private void addManual(List<String> manuals, String manual) {
        manuals.add(manual);
    }

    private void addManual(List<String> manuals, String manual, List<String> commands) {
        addManual(manuals, manual);
        if (commands == null || commands.isEmpty())
            return;
        manuals.add(getExampleManualOfCommand(commands));
    }

    @Bean
    @Override
    public List<String> getYachtManual() {
        List<String> manuals = new ArrayList<>();

        addManual(manuals, "**1. 게임 설명**");
        addManual(manuals, "- 요트 다이스는 주사위를 굴려 특정 조합을 만들어 점수를 획득하는 게임입니다.");
        addManual(manuals, "- 각 턴마다 최대 3번까지 주사위를 굴릴 수 있으며, 첫 굴림 이후 원하는 주사위를 선택하여 고정할 수 있습니다.");
        addManual(manuals, "- 고정된 주사위는 유지되며, 고정되지 않은 주사위만 다시 굴립니다.");
        addManual(manuals, "- 주사위를 고정하거나 해제하여 원하는 조합을 전략적으로 만들어야 합니다.");
        addManual(manuals, "- 주사위 굴림이 끝나면 원하는 점수 항목을 선택하여 해당 턴의 점수를 기록합니다.");
        addManual(manuals, "- 각 점수 항목은 한 번만 선택할 수 있으며, 이미 선택한 항목은 다시 사용할 수 없습니다.");
        addManual(manuals, "- 원하는 조합이 나오지 않더라도 반드시 하나의 항목을 선택하여 점수를 기록해야 합니다.");
        addManual(manuals, "- 모든 점수 항목을 채우면 게임이 종료되며, 최종 점수가 가장 높은 플레이어가 승리합니다.");
        addManual(manuals, "- 각 조합의 규칙을 잘 활용하여 높은 점수를 얻는 것이 중요합니다.");

        addManual(manuals, "");
        addManual(manuals, "**2. 조작 방법**");
        addManual(manuals, "- " + UniEmoji.SMALL_RED_TRIANGLE + " : 주사위 고정을 위한 모드로 전환합니다.");
        addManual(manuals, "- " + UniEmoji.SMALL_RED_TRIANGLE_DOWN + " : 주사위 해제을 위한 모드로 전환합니다.");
        addManual(manuals, "- " + UniEmoji.RECYCLE + " : 고정되지 않은 주사위를 모두 다시 굴립니다.");
        addManual(manuals, "- " + UniEmoji.NUM_1 + " ~ " + UniEmoji.NUM_5 + " : 선택한 슬롯의 주사위를 고정하거나 해제합니다.");
        addManual(manuals, "- -# tip) 매 라운드가 시작되면 기본 설정은 <주사위 고정 모드>로 초기화됩니다.");
        addManual(manuals, "- -# tip) 숫자 이모지를 누르면 뒤에 있는 주사위 슬롯 번호가 한 칸씩 앞으로 이동합니다.");
        addManual(manuals, "- -# tip) 슬롯 번호가 이동하더라도, 이모지를 역순으로 누르면 처음 확인한 번호 그대로 입력할 수 있습니다.");

        addManual(manuals, "");
        addManual(manuals, "**3. 점수 기입 방법**");
        addManual(manuals,
                "ㅋ 1~6 : 선택한 숫자와 같은 주사위 눈의 총합을 스코어로 기입합니다.",
                List.of("1", "2", "3", "4", "5", "6"));
        addManual(manuals,
                "ㅋ 초이스 : 모든 주사위 눈의 합을 스코어로 기입합니다.",
                yachtCommandFactory.getChoiceCommands());
        addManual(manuals,
                "ㅋ 포카드 : 같은 숫자의 주사위가 4개 이상일 경우, 모든 주사위 눈의 합을 스코어로 기입합니다.",
                yachtCommandFactory.getChoiceCommands());
        addManual(manuals,
                "ㅋ 풀하우스 : 같은 숫자가 3개와 2개일 경우, 모든 주사위 눈의 합을 스코어로 기입합니다.",
                yachtCommandFactory.getChoiceCommands());
        addManual(manuals,
                "ㅋ 스몰스트레이트 : 연속된 숫자 4개가 있을 경우, 15점을 기입합니다.",
                yachtCommandFactory.getSmallStraightCommands());
        addManual(manuals,
                "ㅋ 라지스트레이트 : 연속된 숫자 5개가 있을 경우, 30점을 기입합니다.",
                yachtCommandFactory.getLargeStraightCommands());
        addManual(manuals,
                "ㅋ 요트 : 같은 숫자 주사위 5개일 경우, 50점을 기입합니다.",
                yachtCommandFactory.getYachtCommands());

        addManual(manuals, " ");
        addManual(manuals,
                "- -# tip) <1 ~ 6>의 총합이 63점 이상일 경우, 추가로 35점을 획득합니다.");
        return manuals;
    }

    @Bean
    @Override
    public EmbedDto getYachtManualEmbedDto() {
        EmbedDto embedDto = new EmbedDto(
                Sentence.YACHT.getMessage(),
                "게임 설명서",
                String.join("\n", getYachtManual()),
                Color.GREEN
        );
        return embedDto;
    }

    private void addSection(List<EmbedDto> embedDtoList, String title, StringBuilder builder) {
        EmbedDto embedDto = new EmbedDto(
                Sentence.YACHT.getMessage(),
                title,
                String.join("\n", builder.toString()),
                Color.GREEN
        );
        embedDtoList.add(embedDto);
    }

    private List<EmbedDto> getEmbedDtoList(List<String> manuals) {
        List<EmbedDto> embedDtoList = new ArrayList<>();

        int sectionNumber = 0;
        String sectionTitle = null;
        StringBuilder sectionBuilder = new StringBuilder();
        for (String manual : manuals) {
            if (manual.equals("")) {
                sectionNumber = 0;
                continue;
            }
            if (sectionNumber++ == 0) {
                if (sectionTitle != null)
                    addSection(embedDtoList, sectionTitle, sectionBuilder);
                sectionTitle = manual;
                sectionBuilder = new StringBuilder();
                continue;
            }
            sectionBuilder.append('\n').append(manual);
        }
        if (sectionTitle != null)
            addSection(embedDtoList, sectionTitle, sectionBuilder);
        return embedDtoList;
    }

    @Bean
    @Override
    public List<EmbedDto> getYachtManualEmbedDtoList() {
        return getEmbedDtoList(getYachtManual());
    }
}
