# DIDACTO-API

## 프로젝트 배경

### 기간/인원
2024-02-01~진행중
5명(백엔드3, 프론트엔드2)

### DIDACTO란?
DIDACTO는 배우다 라는 의미를 가진 라틴어로 졸업프로젝트의 지향점, 주제는 다음과 같습니다.
"교육환경제공은 필요하지만, 현재 실습 인프라 운영이 미흡하고 관리하가 힘든것이 사실이다. 실습환경을 조성해두어야 한다면 양질의 인프라를 제공할 수 있는 SW를 만들자"

### 문제 설정 
현재 우리는 IT 시대에 살고 있다. 이에 따라서 IT 교육의 확대가 이루어졌으며, 프로그래밍은 물론 디자인, 통계, 회계, 예체능 등 다양한 필드에서 컴퓨터 실습 기반의 교육이 초등교육 기관부터 고등교육 기관까지 널리 확산되고 있다. 그러나 교육방식에서 비효율적인 면모가 있다. 교강사가 질문이 들어오면 자리로 가서 하나하나 봐주는 것은 흔히 대학교 강의실에서 볼 수 있는 비효율적인 사례 중 하나이다. 또한 시험 부정행위, 수업 시간 중 컴퓨터를 이용하여 수업 외적인 행동을 하는 등의 학생들의 통제에도 난관을 겪고 있다.

### 과제 목적
교육기관에서는 교육을 위해 실습환경 마련이 필수적이므로 구축된 인프라를 활용할 수 있는 효율적인 방법이 필요하다. 따라서 실습환경을 조성하기위한 양질의 인프라를 제공할 수 있는 SW를 설계해보자.

## 프로젝트 상세 내용

### 개발 목표
![image](https://github.com/Lwonbin/DIDACTO_graprac/assets/128762057/00c79df5-b5d1-4d63-8051-822e53524537)

### 1차 개발범위
1차 개발범위에 해당하는 내용입니다.

1. MEMBER(회원)
  1. 회원 가입, 수정, 탈퇴
  2. 인증(Authentication)
  3. 인가(Authorization)
2. LECTURE(강의)
   1. 강의 생성, 수정, 삭제
   2. 강의 검색 (키워드 검색)
   3. 강의에 속한 학생들 조회
   4. 자신이 속해있는 강의들 조회
   5. 강의 탈퇴 (학생)
   6. 학생 강퇴 (교수)
3. ENROLLMENT(참여)
   1. 강의 참여 요청/취소
   2. 참여 요청 수락/거절

### 2차 개발범위

1. 모니터링 : 일정 간격으로 실습 컴퓨터들의 상태와 스크린을 Push 및 Polling하여 웹에서 학생들의 접속 현황과 스크린 상태를 모니터링   할 수 있는 모니터링 기능
2. 결제시스템 : SW의 지속성을 고려해 수익모델이 필요함. 따라서 유료결제 시스템을 이용해 교수자의 등급으로 강의 설계를 제한하도록 하는 결제시스템 기능
3. 스트리밍 : 학생과 교수자의 1:N 단위의 화면 스트리밍기능


## 개인 기여
1. MEMBER(회원) API 작성 -1차
2. 결제시스템 구축 - 2차


## 결제 시스템 설명

1. 결제시작

```java
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>결제페이지</title>
    <!-- jQuery -->
    <script
            type="text/javascript"
            src="https://code.jquery.com/jquery-3.5.1.min.js"
    ></script>
    <!-- iamport.payment.js -->
    <script
            type="text/javascript"
            src="https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"
    ></script>
    <script src="https://cdn.jsdelivr.net/npm/axios@1.1.2/dist/axios.min.js"></script>
    <script>
        function requestPay() {
          const amount = document.getElementById("price").value;
          const IMP = window.IMP; // 생략 가능
          IMP.init("imp51203488"); // PG사 사업자 IMP번호 (Secret으로 관리되어야 함)

          //임시로 넣어놓은 데이터
          var orderuid = "643c7b2d-748a-4e0b-9b47-7de6f8fd2da7"
          var item_name = "premium"
          var buyer_email = "asdf1234@naver.com"
          var buyer_name = "홍길동"

          console.log("결제 요청 시작");
          IMP.request_pay(
            {
              pg: "html5_inicis", // PG사
              pay_method: "card", // 결제방식
              merchant_uid: orderuid, // 주문번호-상품ID
              name: item_name, // 상품이름
              amount: amount, // 가격
              buyer_email: buyer_email,
              buyer_name: buyer_name
            },
            function(rsp) {
                     // callback
                    console.log(rsp); //백엔드에 넘겨줄 결제 처리 완료 정보
                    if (rsp.success) {
                        // 결제 성공 시: 결제 승인 또는 가상계좌 발급에 성공한 경우
                        alert('call back!!: ' + JSON.stringify(rsp));
                        axios.post("/api/v1/payment", {
                            payment_uid: rsp.imp_uid, // 결제 고유번호
                            order_uid: rsp.merchant_uid // 주문번호
                            //item_name: rsp.name // 주문아이템 이름
                        }, {
                            headers: { "Content-Type": "application/json" }
                        })
                        .then(function (response) {
                            console.log(response.data);
                            // 가맹점 서버 결제 API 성공시 로직
                            alert('결제 완료!' + JSON.stringify(rsp));
                            window.location.href = "/success-payment.html";
                        })
                        .catch(function (error) {
                            console.error('결제 실패: ', error);
                            alert('결제 처리 중 오류가 발생했습니다.');
                        });
                    } else {
                        // 결제 실패 시 처리 로직
                        alert("결제 실패");
                        window.location.href = "/fail-payment.html";
                    }
                }
            );
        }
    </script>
</head>
<body>
결제금액: <input type="text" id="price" />
<button onclick="requestPay()">결제하기</button>
</body>
</html>

```

결제가 완료되었다면 callback 으로 서버쪽에 payment_uid와 order_uid를 보내게 된다.

1. PaymentController       api/v1/payment

```java
@PreAuthorize(AuthConstant.AUTH_ADMIN)
    @Operation(summary = "PAYMENT_02 : 결제 API", description = "결제를 진행한다.")
    @PostMapping("/payment")
    public ResponseEntity<IamportResponse<Payment>> validationPayment(@RequestBody PaymentCallbackRequest request) {
        IamportResponse<Payment> iamportResponse = paymentService.paymentByCallback(request);
        log.info("결제 응답={}", iamportResponse.getResponse().toString());
        return new ResponseEntity<>(iamportResponse, HttpStatus.OK);
    }
```

RequestBody로 넘어온 imp_uid와 order_uid를 service단으로 넘겨줌

2.  PaymentService
    paymentByCallback 함수
        
        ```java
            @Transactional
            public IamportResponse<Payment> paymentByCallback(PaymentCallbackRequest request) {
                try {
                    // 결제 단건 조회(아임포트)
                    IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(request.getPayment_uid());
                    if (iamportResponse == null) {
                        throw new IllegalArgumentException("결제 내역이 없습니다.");
                    }
                    // 주문내역 조회
                    Order order = orderRepository.findOrderAndPayment(request.getOrder_uid())
                            .orElseThrow(() -> new IllegalArgumentException("주문 내역이 없습니다."));
        
                    // 결제 상태 검증 및 처리
                    validateAndProcessPayment(iamportResponse, order);
        
                    return iamportResponse;
        
                } catch (IamportResponseException e) {
                    log.error("IamportResponseException 발생", e);
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    log.error("IOException 발생", e);
                    throw new RuntimeException(e);
                }
            }
        ```
        
        - 결제 단건 조회를 통해서 결제가 있는지 확인 후 받아오기
        
        - 주문내역 조회를 통해서 주문 이있는지 확인 후 받아오기
        
        - 두개를 활용해 검증 과정 시작
        
    2. validateAndProcessPayment
        
        ```java
        private void validateAndProcessPayment(IamportResponse<Payment> iamportResponse, Order order) throws IamportResponseException, IOException {
                // 결제 완료가 아니면
                if (!"paid".equals(iamportResponse.getResponse().getStatus())) {
                    // 주문, 결제 삭제
                    orderRepository.delete(order);
                    paymentRepository.delete(order.getPayment());
                    throw new RuntimeException("결제 미완료");
                }
        
                // DB에 저장된 결제 금액
                Long price = order.getPayment().getPrice();
                // 실 결제 금액
                int iamportPrice = iamportResponse.getResponse().getAmount().intValue();
        
                // 결제 금액 검증
                if (iamportPrice != price) {
                    // 주문, 결제 삭제
                    orderRepository.delete(order);
                    paymentRepository.delete(order.getPayment());
        
                    // 결제금액 위변조로 의심되는 결제금액을 취소(아임포트)
                    iamportClient.cancelPaymentByImpUid(new CancelData(iamportResponse.getResponse().getImpUid(), true, new BigDecimal(iamportPrice)));
                    throw new RuntimeException("결제금액 위변조 의심");
                }
        
                // 결제 상태 변경
                order.getPayment().changePaymentBySuccess(iamportResponse.getResponse().getStatus(), iamportResponse.getResponse().getImpUid());
                order.getMember().premium();
            }
        ```
        
        1. 상태가 결제 완료인지확인
        2. DB 저장금액과 실결제 금액을 받아와 결제 금액이 정확한지 확인
        3. 결제 상태 변경(DB에 payment 도메인에 imp_uid 저장 및 결제상태 변경 Ready → paid)
        
        여기에서 결금액 위변조로 의심되는 결제금액을 취소하게 되는데 이게 이미 결제가 완료된 부분이여도 결제 검증을 통과하지 못하면 결제취소를 할 수 있다
        
        ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/739e4001-c3ec-42b1-bb21-9e84768b738e/824ee5a6-9cf1-4f5a-bb22-3c90347e525b/Untitled.png)
        
        실제 금액을 낮춰서 결제하는 행위를 했을 경우 결제가 취소되는 것을 확인할 수 있다.



