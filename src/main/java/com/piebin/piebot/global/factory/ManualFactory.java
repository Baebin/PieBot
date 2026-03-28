package com.piebin.piebot.global.factory;

import com.piebin.piebot.global.dto.embed.EmbedDto;

import java.util.List;

public interface ManualFactory {
    List<String> getYachtManual();
    EmbedDto getYachtManualEmbedDto();
    List<EmbedDto> getYachtManualEmbedDtoList();
}
