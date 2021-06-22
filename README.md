#  사전과제 1 - 결제 시스템  

## 개발 환경  
- 기본 환경   
    - IDE: Eclipse(utf-8)  
    - OS: Window  
    - GIT  
- Server
    - Java8
    - Spring Boot 
    - JPA
    - H2
    - Gradle
    - Junit  

- 접속 Base URI: `http://localhost:8080`

## 빌드 및 실행방법
1. Git Clone https://github.com/cskim178/KakaopayPretest.git
2. Gradle Project import
3. Refresh Gradle proejct
4. Build
5. Run Spring Boot

## 도메인(ENTITY)
- PAYMENT 결정보
    - ID(관리번호 - ID), PAY_TYPE(결제유형), ENCR_CARD_INFO(암호화된 카드정보), INST_MM(할부개월수), VAT(부가세), REF_ID(원거래관리번호), CREATE(생성일시) 
- TRANSACTION 카드사와 통신
    - CONTENT(카드사에 전달한 데이터 - ID)

## API 목록
- 결제 /payment
    - 결제정보를 입력받아서 결제정보 및 카드사와 협의된 문자열 데이터를 DB에 저장
    - request 
        -  required : 카드번호, 유효기간, CVC, 할부개월수, 결제금액
        -  optaional : 부가가치세
    - response 
        -  관리번호, 카드사에 전달한 문자열
    - 입력값에 대한 Validation 체크 후 데이터 저장(카드정보 AES-256bit 암호화)

- 결제취소(전체/부분) /payment
    - 관리번호를 입력받아서 금액과 부가세를 확인 후 결제정보 및 카드사와 협의된 문자열 데이터를 DB에 저장
    - request 
        -  required : 관리번호, 취소요청금액
        -  optaional : 부가가치세
    - response 
        -  관리번호, 카드사에 전달한 문자열
    - 요청된 값과 결제된 데이터를 비교하여 가능시 결제정보 및 카드사와 협의된 문자열 데이터를 DB에 저장
        -  요청된 관리번호를 원거래관리번호로 처리한다.
        -  취소결제는 취소가능 금액이 0원일때까지 부분취소가 가능하다.
        -  부분취소의 횟수에 상관없이 취소가능 금액이 0원이 되면 전체 취소가 된 것으로 판단한다.
        -  취소요청금액은 결제된 금액보다 클수 없다.
        -  최종결제시 부가가치세는 0원이 되어야 한다.

- 결제조회 /getInfo
    - 관리번호를 입력받아서 해당 결제정보를 리턴
    - request 
        -  required : 관리번호
    - response 
        -  required : 관리번호, 카드번호, 유효기간, CVC, 결제유형, 결제/취소금액, 부가가치세

## 후기
이관일과 사전과제 마감일이 겹치며 시간에 너무 쫒기다 보니 아쉬움이 많이 남습니다. 지원서 결과가 이렇게 빨리 나올 줄, 바로 사전과제가 시작될 줄 몰랐네요. 막상 제출하려고 보니 지급을 MAIN으로, 취소를 SUB로 DB를 설계했다면 좀 더 효율적이지 않았을까 합니다. 밤도 많이 새고 고생도 많았지만 신선하고 재미있는 경험이었습니다. 좋은 모습으로 뵐 기회가 있기를 바랍니다. 감사합니다.
