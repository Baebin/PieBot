package com.piebin.piebot.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandSentence {
    DICE_ARG1("주사위 굴리기", "주사위 범위가 올바르지 않습니다.", "[2~6] 사이의 수를 입력해주세요."),

    // Secret (Tazo)
    SECRET_TAZO("타조 ㅋ", "아니 이거 어케 찾은거지? 넌 역시 천재야.", "첫번째 이스터에그야."),
    SECRET_KOOKOO("루삥뽕 ㅋ", "킹받네", "두번째 이스터에그야."),
    SECRET_KOOKOO2("얍삐", "와 이거까지?", "세번째 이스터에그야."),
    SECRET_YABBEE("예압삐 ~", "할줄 알았지 ㅋ", "네번째 이스터에그야."),
    SECRET_DANMOOJI("단무지다", "ㄷㄷ.. 아니 어케 앎", "다섯번째 이스터에그야."),
    SECRET_BINGSIN("빙구들의 신", "발음 주의. 그의 매력에 홀릴지도 모르니 주의할 것.", "여섯번째 이스터에그야."),
    SECRET_PAENGSIN("달팽이들의 신", "빙신의 양대 산맥. 그녀를 한번만 보고 간 달팽이는 없다.", "일곱번째 이스터에그야."),
    SECRET_ULEOG("캬~ 여기엔 코코넛이지", "인천대에서 출몰하는 우럭이 맛있다던데..", "여덟번째 이스터에그야."),
    SECRET_HAWAWA("머나먼 휴양지 \uD83D\uDC0C\uD83D\uDC0C\uD83E\uDD6C\uD83C\uDFDD", "하와와 섬은 달팽이들의 천국이야.", "아홉번째 이스터에그야."),
    SECRET_BUIKK("븪븩 ~", "고대어로 '안녕'이라는 뜻이래. 현재는 팽달 부족이 '응'이라는 의미로 쓰고있다나 뭐라나.", "열번째 이스터에그야."),
    SECRET_COOKIE("머랭 ~ ..쿠키", "빙달어의 감탄사 중 하나, 이 표현은 특이하게 두 명일 때만 공감의 표시로 사용할 수 있다고 한다.", "열한번째 이스터에그야."),
    SECRET_RESET("어머나! 또 리셋이야?", "야전삽을 든 제키를 조심할 것", "열두번째 이스터에그야."),
    SECRET_POTATO("얘야, 봄 감쟈가 맛있단다.", "예나 누나의 마지막 한 마디", "열세번째 이스터에그야."),
    SECRET_KIMCHICHESSEUDONG("Q. 정체불명의 음식을 찾아서", "24년 5월 24일. 팽달이는 이 음식을 보고 경악을 금치 못했다는 전설이 있다.", "열네번째 이스터에그야."),
    SECRET_BINGDAL("빙신과 팽신의 합작품", "세상에 존재하는 모든 언어들을 표현할 수 있는 고차원적인 언어", "열다섯번째 이스터에그야."),
    SECRET_JACKYGYU("제키볶음 제 1장 1절", "하늘에서 프로 손전등이 내려오니, 모든 괴물들은 그 빛을 두려워했으며 곧이어 제키가 그녀를 따랐다.", "열여섯번째 이스터에그야."),
    SECRET_TARO("제키신이 운영하는 축복받은 지점", "사랑의 작대기만 있다면 운명의 상대를 찾을 수 있을지어다.", "열일곱번째 이스터에그야."),
    SECRET_CHOPIN("'쇼'하는 달'팽'이", "그녀의 황홀한 연주를 듣지 않고 지나친 생명체들은 모두 제키에게 맞을 운명", "열여덟번째 이스터에그야."),
    SECRET_YAJEONSAB("제키교의 신성물", "사악한 데빙구를 퇴치할 때 사용되었던 고대의 유물", "열아홉번째 이스터에그야."),

    SECRET_EASTEREGG("Q. 에그를 찾아서", "모든 이스터에그를 찾으면 빈이가 밥을 사준다는 전설이 있다구", "이스터에그는 총 19개나 있단다?"),

    // Secret (Food)
    SECRET_FOOD_ARG1("음식 맞추기", "음식 이름을 입력해주세요.", ""),
    SECRET_FOOD_SUCCESS("음식 맞추기", "맞았습니다.", "우와.. 대박"),
    SECRET_FOOD_FAILED("음식 맞추기", "틀렸습니다.", "화이팅 :D"),
    SECRET_FOOD_HINT1("음식 맞추기", "힌트 1", "이름에 '치즈'가 들어갑니다."),
    SECRET_FOOD_HINT2("음식 맞추기", "힌트 2", "면류입니다."),
    SECRET_FOOD_HINT3("음식 맞추기", "힌트 3", "겠나요.. 어디서 욕심 많은 달팽이가 !"),
    SECRET_FOOD_CONTAIN1("음식 맞추기", "힌트", "완전 근접했는걸? 이 빙구는 감동했어."),
    SECRET_FOOD_CONTAIN2("음식 맞추기", "힌트", "...겠나요 팽달씨?"),
    SECRET_FOOD_CONTAIN3("음식 맞추기", "힌트", "정녕 저를 삶아먹으실 계획인가요");

    private String title;
    private String message;
    private String description;
}
