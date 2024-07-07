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


## 결제 시스템 기본동작 설명

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
        
        여기에서 결금액 위변조로 의심되는 결제금액을 취소하게 되는데 이게 이미 결제가 완료된 부분이여도 결제 검증을 통과하지 못하면 결제취소를 할 수 있습니다.
        
       ![image](https://github.com/Lwonbin/DIDACTO_graprac/assets/128762057/99eafe62-5a5d-43a0-beef-f9723289fa02)

        
        실제 금액을 낮춰서 결제하는 행위를 했을 경우 결제가 취소되는 것을 확인할 수 있다.



## 결제시스템 고도화 진행
# 웹훅을 이용한 결제시스템 안정성 높이기

포트원에서 설명하는 웹훅

“특정 이벤트가 발생하였을 때 타 서비스나 응용프로그램으로 알림을 보내는 기능입니다. Webhook 프로바이더는 해당 이벤트가 발행하면 `HTTP POST` 요청을 생성하여 callback URL(endpoint)로 이벤트 정보을 보냅니다. 주기적으로 데이터를 폴링(polling)하지 않고 원하는 이벤트에 대한 정보만 수신할 수 있어서 webhook은 리소스나 통신측면에서 훨씬 더 효율적입니다.”

즉 웹훅이란 특정 이벤트가 발생하였을 때(결제가 완료되었을 때) 설정해놓은 URL로 데이터를 보내주는 기능이다.

만약 결제가 성공했다면 해당 데이터를 설정해놓은 URL로 보낼 수 있게 된다.

`curl -H "Content-Type: application/json" -X POST -d '{ "imp_uid": "imp_1234567890", "merchant_uid": "order_id_8237352", "status": "paid" }' { NotificationURL }`

imp_uid :  결제번호

merchant_uid : 주문 번호

status : 결제상태

보통 웹훅은 가상계좌에 돈이 입금되었는지 안되었는지 확인하는 용도로 많이사용한다고 한다. 가상계좌는 즉시 돈이 입금되지 않기때문에 포트원쪽에서 대기를 하고 있다가 만약 사용자가 돈을 입금했다! 라는 이벤트가 발생하면 그 때 백엔드단에서 처리를 해줄 수 있다.

우리는 가상계좌에 돈을 입금하는 경우는 일단 고려하지 않고 있으니 배제하고 인터넷 연결 끊김, 브라우저 자동 새로고침 등의 이유로 클라이언트에서 결제 완료에 대한 응답을 받지 못하는 경우가 간헐적으로 발생하는 경우에 대비해 웹훅을 사용하도록 한다.

웹훅을 설정하는 방식은 두가지가 있다.

1. 포트원 웹훅 URL 설정에서 설정하기 
    
    ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/739e4001-c3ec-42b1-bb21-9e84768b738e/872ac9b6-ea7e-4b0e-b4f0-0a96ca6f80d8/Untitled.png)
    
2. 결제건마다 URL 설정하기
    
    ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/739e4001-c3ec-42b1-bb21-9e84768b738e/5fad35fa-a1b4-4d9a-8819-bdcaec33e0ac/Untitled.png)
    
    결제건마다 URL을 설정하면 포트원에서 설정한 URL보다 우선순위를 가진다.
    

딱히 결제건마다 URL을 바꿀필요는 없기때문에 1번 방식을 이용해 설정을 하려고 하고 있고 테스트를 진행중에 있다. 포트원에서 직접 URL을 작성해 테스트하려면 ngrok을 이용해서 localhost를 외부에서 접근가능한 상태로 만들어야한다.(포트포워딩등 여러방식이 있지만 가장 직관적인 방법이라 채택)

기본적으로 webhook 호출 테스트는 **외부망에서 접근 가능한 도메인만 가능하기 떄문이다.**

따라서 ngrok를 이용해 http://localhost:8080을 외부에서 접근가능한 도메인으로 변경시켜준다.

ngrok를 다운받아 실행 후 다음과 같이 작성 > ngrok http 8080

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/739e4001-c3ec-42b1-bb21-9e84768b738e/bea316a7-673c-4b88-bdf8-6137665c9251/Untitled.png)

그럼 다음과 같은 창이 나오게 된다.

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/739e4001-c3ec-42b1-bb21-9e84768b738e/82c69d25-5661-4517-9a6f-72559b0a248a/Untitled.png)

저기 https://399e-121-163-190-204.ngrok-free.app이 새롭 발급받은 도메인이다. http://localhost:8080이 다음과 같은 도메인으로 변경되면서 외부접속이 허용됨을 의미한다.

이것을 이용해 간단히 테스트를 해보자.

```java
package com.didacto.controller.v1.pay;

import com.didacto.dto.pay.WebhookPayloadRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebhookController {

    @PostMapping("/webhook")
    public String handleWebhook(@RequestBody WebhookPayloadRequest payload) {
        String impUid = payload.getImpUid();
        String merchantUid = payload.getMerchantUid();
        String status = payload.getStatus();
        int amount = payload.getAmount();

        // 결제 상태 업데이트 로직
        if ("paid".equals(status)) {
            // 결제 성공 처리
            System.out.println("Payment " + merchantUid + " is paid with amount " + amount);
        } else {
            // 결제 실패 처리
            System.out.println("Payment " + merchantUid + " failed.");
        }

        return "Webhook received";
    }
}

```

이런식으로 데이터가 정상적으로 들어오는지 확인할 수 있다. 이렇게 작성후 포트원에서 웹훅 테스트를 해보면 200응답이 오는것을 확인할 수 있다.

포트원 창

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/739e4001-c3ec-42b1-bb21-9e84768b738e/1a648779-4054-4d85-a736-4afaf40785e5/Untitled.png)

ngrok창

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/739e4001-c3ec-42b1-bb21-9e84768b738e/94fe693f-ea70-4bf8-bc68-8b84fdb368f4/Untitled.png)

고민했던 것

- 결제가 안되었다면 어떤식으로 처리를 해줘야 하는가?
    - 결제처리는 크게 두가지로 나눌 수 있다. 프론트에서 직접 PG사와 연결되어 결제처리가 되는것과 백엔드에서 그 결제처리가 된 데이터를 가지고 실제 데이터가 맞는지, 주문이 맞는지, 실제 결제된 금액과 주문 금액이 같은지 검증과정을 거치는 처리 두가지로 나눌 수 있다. 간단히 말하자면 백엔드와 프론트 두가지의 결제 처리가 있다.
    - 논의 결과 프론트에서 결제 오류가 발생했다면 이것은 그냥 결제를 다시하는 형태로 가는 것이 일반적이라는 결론을 내렸다. 우리도 결제를 시도할 때 다양한 오류가 발생했는데 이런 경우에는 보통 다시 결제를 진행하는 것이 일반적일 것이다.
    - 그러나 실제 결제는 잘 이루어졌는데 네트워크상의 오류등등으로 백엔드에서 오류가 발생했을 시에 문제가 발생한다. 처음에는 어찌됐건 오류가 발생했으니 결제취소(환불)을 가정해서 코드를 작성하려고 했으나 생각해보니 이미 사용자는 결제를 정상적으로 했는데 이거를 다시 취소하고 재결제를 요청하는 것은 일반적인 플로우가 아니였다.
    - 따라서 백엔드 단에서 어떻게든 결제처리가 정상적으로 이루어 지게끔 만드는 것이 이 결제 고도화의 주 목표라고 보면 되겠고 웹훅을 이용해보기로 했다.
- 어차피 웹훅이랑 콜백 처리가 똑같으면 웹훅만 있으면 되는거 아닌가?
    
    사실 웹훅에대한 결제처리와 콜백에 대한 결제처리가 같다. 실제 넘어오는 데이터 형식을 확인해보면 똑같은 데이터를 넘기는 것을 볼 수 있다. 이에 대해서 똑같은 데이터를 어차피 넘겨주는데 왜 두번의 검증과정이 필요한지 의문이였다. 
    
    포트원측에서는 이렇게 말한다. 웹훅은 결제의 안정성을 더하기 위해 포트원에서 강력히 권고 드리는 통지 서비스입니다.
    
    웹훅을 권장하는 이유는 인터넷 연결 끊김, 브라우저 자동 새로고침 등의 이유로 클라이언트에서 결제 완료에 대한 응답을 받지 못하는 경우가 간헐적으로 발생하게 된다. 이런 경우 연동한 웹훅 사용하시는 상황이라면  누락 없이 결제 정보를 동기화가 가능하므로 웹훅이 있는 것이다. 
    
- 웹훅 테스트의 어려움
    
    웹훅을 통한 테스트는 실제 데이터를 임시로 만든후에 그에대한 결제를 처리하는식으로 동작여부를 확인할 수 있었는데, 포트원쪽에서 제공하는 웹훅 테스트는 임시의 데이터를 담아서 보내주는 것이여서 다양한 오류의 가능성이 존재했다.
    
    예를들어 결제 단건 조회는 웹훅시에 필수적으로 들어가야한다고 말한다.
    
    “웹훅 수신주소는 공개된 URL로 포트원이 아닌 서버에서 웹훅을 보낼 위험이 있기 때문에, 고객사 서버는 **웹훅을 수신하고 반드시 [결제내역 단건조회 API](https://developers.portone.io/docs/ko/api/payment-api/get-payment-by-impuid-api)를 통해 결제건을 조회하여 웹훅의 내용을 검증해야 합니다.”**
    
    그러나 이 코드를 작성하면 
    
    ```java
    // 결제 단건 조회(아임포트)
                IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(payload.getImpUid());
    ```
    
    실제 존재하지않은 결제id(ImpUid)를 조회하는 셈이 되니 오류가 발생할 수 밖에 없다.(다양한 오류의 발생 원인이 존재하지만 생각해보니 여기서부터 오류가 발생할 수 밖에 없을 것 같음)
    
    그래서 위에 처럼 간단히 데이터가 정상적으로 넘어오는지 ngrok을이용해 외부 접속망으로 설정되었는지만 확인할 수 있었다.
    
- 웹훅에서 오류가 발생하면 어떻게하나?
    
    결제처리를 안정적으로 하기위해 웹훅을 도입했지만 이 웹훅또한 네트워크 오류상의 이유로 전송이 안될 수 있는 것도 고려해야한다. 현재 포트원의 결제버전 V2에서는 좀더 발전된 웹훅과 재전송 방법을 사용하고 있지만 도입한 결제 시스템이 V1버전이기 때문에 V1에 해당하는 내용을 확인해보면
    
    **웹훅 재 전송이 가능한가요?**
    
    웹훅은 기본적으로 1회 전송되도록 설정되어 있습니다. 네트워크 에러가 발생하거나 고객사 응답(HTTP 상태 코드)이 500번대인 경우, 재발송 설정을 원하신다면 웹훅이 최대 5회까지 1분 간격으로 재시도될 수 있습니다. 웹훅 재발송 설정 요청은 [support@portone.io](mailto:support@portone.io) 로 고객사 식별코드를 기재하여 요청해 주시면 됩니다.
    
    V1에서 도입하는 재전송 기능은 최대 5회까지 1분 간격으로 재시도될 수 있다고 한다. 이 방법도 완벽하진 않지만? 어찌됐든 결제처리를 안정적으로 도입하려면 끝도없다… 라는 사실과 그만큼 결제부분이 돈과관련된 문제기 때문에 안정적인 결제처리가 너무 중요하다 라는 생각이 든다.



