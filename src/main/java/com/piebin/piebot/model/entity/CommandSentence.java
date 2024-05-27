package com.piebin.piebot.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandSentence {
    DICE_ARG1("주사위 굴리기", "주사위 범위가 올바르지 않습니다.", "[2~100] 사이의 수를 입력해주세요."),

    PAY_COMPLETED("송금", "{A}빙 송금이 완료되었습니다.", "전송에 착오가 없었는지 확인해주세요."),
    PAY_ARG1("송금", "대상을 입력해주세요.", "서비스 이용자만 멘션이 가능합니다."),
    PAY_ARG1_SELF("송금", "송금할 수 없는 대상입니다.", "자기 자신에게는 송금할 수 없습니다."),
    PAY_ARG2_MIN("송금", "최소 금액은 1빙입니다.", "올바른 액수를 입력해주세요."),
    PAY_ARG2_LESS("송금", "잔여 금액이 부족합니다.", "올바른 액수를 입력해주세요."),

    // Omok
    OMOK_ARG1("오목", "[대전/퇴장/순위/프로필/이어하기]", "올바르지 않은 명령어입니다."),
    OMOK_PVP("오목 대전", "대전을 신청한 유저가 흑돌이 됩니다.", "수락하려면 이모지를 눌러주세요."),
    OMOK_PVP_ARG2("오목 대전", "대상을 입력해주세요.", "서비스 이용자만 멘션이 가능합니다."),
    OMOK_PVP_SELF("오목 대전", "대전 신청을 할 수 없는 대상입니다.", "자기 자신에게는 대전을 신청할 수 없습니다."),
    OMOK_PVP_EXISTS_FROM("오목 대전", "이미 진행중인 게임이 있습니다.", "게임을 이어하거나 퇴장해주세요."),
    OMOK_PVP_EXISTS_TO("오목 대전", "상대방이 이미 진행중인 게임이 있습니다.", "게임이 끝날 때까지 대기해주세요."),
    OMOK_QUIT("오목 퇴장", "오목 게임에서 퇴장하였습니다.", "게임 결과가 무승부로 처리됩니다."),
    OMOK_QUIT_NONE("오목 퇴장", "진행중인 게임이 존재하지 않습니다.", "유저에게 오목 대전을 신청해주세요."),
    OMOK_CONTINUE_NONE("오목 이어하기", "진행중인 게임이 존재하지 않습니다.", "유저에게 오목 대전을 신청해주세요."),

    // Gambling
    GAMBLING_ARG1("도박", "[묵찌빠]", "올바르지 않은 명령어입니다."),
    GAMBLING_MUKCHIBA_ARG2_MIN("묵찌빠 게임", "최소 금액은 1빙입니다.", "보유 자산: {A}빙"),
    GAMBLING_MUKCHIBA_ARG2_LESS("묵찌빠 게임", "잔여 금액이 부족합니다.", "보유 자산: {A}빙"),
    GAMBLING_MUKCHIBA_ARG3("묵찌빠 게임", "[묵/찌/빠]", "올바르지 않은 명령어입니다."),

    // Secret
    SECRET_EASTEREGG("Q. 에그를 찾아서", "모든 이스터에그를 찾으면 빈이가 밥을 사준다는 전설이 있다구", "이스터에그는 총 {A}개나 있단다?"),

    // Secret (Food)
    SECRET_FOOD_ARG1("음식 맞추기", "음식 이름을 입력해주세요.", "올바르지 않은 명령어입니다."),
    SECRET_FOOD_SUCCESS("음식 맞추기", "맞았습니다.", "우와.. 대박"),
    SECRET_FOOD_FAILED("음식 맞추기", "틀렸습니다.", "화이팅 :D"),
    SECRET_FOOD_HINT1("음식 맞추기", "힌트 1", "이름에 '치즈'가 들어갑니다."),
    SECRET_FOOD_HINT2("음식 맞추기", "힌트 2", "면류입니다."),
    SECRET_FOOD_HINT3("음식 맞추기", "힌트 3", "겠나요.. 어디서 욕심 많은 달팽이가 !"),
    SECRET_FOOD_CONTAIN1("음식 맞추기", "힌트", "완전 근접했는걸? 이 빙구는 감동했어."),
    SECRET_FOOD_CONTAIN2("음식 맞추기", "힌트", "...겠나요 팽달씨?"),
    SECRET_FOOD_CONTAIN3("음식 맞추기", "힌트", "정녕 저를 삶아먹으실 계획인가요");

    private final String title;
    private final String message;
    private final String description;
}
