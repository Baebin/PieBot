package com.piebin.piebot.factory;

import com.piebin.piebot.model.dto.embed.EmbedDto;

import java.util.List;

public interface ManualFactory {
    List<String> getYachtManual();
    EmbedDto getYachtManualEmbedDto();
    List<EmbedDto> getYachtManualEmbedDtoList();
}
