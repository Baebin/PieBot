package com.piebin.piebot.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmbedSentence {
    REGISTER("회원가입", "아래 이모지를 클릭해주세요.", "서비스 이용을 위한 계정이 필요합니다."),
    REGISTER_COMPLETED("회원가입", "계정이 생성되었습니다.", "모든 서비스 이용이 가능합니다."),
    REGISTER_ALREADY_EXISTS("회원가입", "계정을 생성할 수 없습니다.", "이미 서비스에 가입되어 있습니다."),

    PROFILE_NOT_FOUND("프로필", "내 정보를 불러올 수 없습니다.", "관리자에게 문의해주세요."),

    OMOK_PVP_STARTED("오목 대전", "게임이 시작되었습니다.", "즐거운 플레이 되세요."),
    OMOK_PVP_WIN("오목 대전", "{A} 승리", "게임이 종료되었습니다."),

    EASTER_EGG_FIND("이스터에그", "해당 이스터에그의 최초 발견자입니다.\n업적 달성 보상으로 3,000빙이 입금되었습니다.", "이 업적은 명예의 전당에 자동으로 기록됩니다."),
    EASTER_EGG_FIND_ALREADY("이스터에그", "해당 이스터에그를 처음 발견했습니다.\n업적 달성 보상으로 1,000빙이 입금되었습니다.", "최초 발견자가 아니므로 명예의 전당에 기록되지 않습니다.");;

    private final String title;
    private final String message;
    private final String description;
}
