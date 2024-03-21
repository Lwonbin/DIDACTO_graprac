package com.didacto.api.v1;

import com.didacto.common.ErrorDefineCode;
import com.didacto.common.response.CommonResponse;
import com.didacto.config.exception.custom.exception.AuthForbiddenException;
import com.didacto.dto.example.ExampleRequestDto;
import com.didacto.dto.example.ExampleResponseDto;
import com.didacto.dto.example.ExampleValidationRequestDto;
import com.didacto.service.example.ExampleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//TODO : Swagger API Docs 설정 이후
// API 문서 설명 내용(API 용도, 요청 및 응답 모델의 필드 설명, 성공 및 실패 케이스 정의 등에 대한 문서 작성 코드를 추가한다.)


@Tag(name = "EXAM API", description = "예제와 관련된 API") // Swagger Docs : API 이름
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/example")
public class ExampleApiController {

    private final ExampleService exampleService;

    @PostMapping()
    @Operation(summary = "EXAM_01 : 저장", description = "Example을 저장시킨다.")   // Swagger API 기능 설명
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success") // Swagger API : 응답 케이스 설명
    })
    public CommonResponse<Long> saveExample(
            @Valid @RequestBody ExampleRequestDto request
    ) {
        Long result = this.exampleService.addExample(request);

        return new CommonResponse(true, HttpStatus.OK, "Example 저장에 성공했습니다", result);
    }

    @GetMapping("/{pathValue}")
    @Operation(summary = "EXAM_02 : 키워드 조회", description = "키워드가 포함된 Example 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success") // Swagger API : 응답 케이스 설명
    })
    public CommonResponse<List<ExampleResponseDto>> searchExampleByKey(
            // Path Variable 사용 예시 : /api/v1/example/blah
            @Schema(description = "Path Variable 예시", example = "blah")   //Swagger 파라미터 설명
            @PathVariable String pathValue,

            // Query Parameter 사용 예시 : /api/v1/example?paramKeyword=blah
            @Parameter(description = "Parameter 예시", example = "blah") //Swagger 파라미터 설명
            @RequestParam(required = false) String paramValue

    ) {
        List<ExampleResponseDto> result = this.exampleService.searchExampleByKeyword(pathValue);


        return new CommonResponse(true, HttpStatus.OK, "리스트 조회에 성공했습니다", result);
    }


    @PostMapping("/error")
    @Operation(summary = "EXAM_03 : 예외 테스트", description = "예외를 반환한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success") // Swagger API : 응답 케이스 설명
    })
    public CommonResponse<List<ExampleResponseDto>> throwExceptionApi(
            @Valid @RequestBody ExampleValidationRequestDto request
    ) {
        if(request.getErrorCode() == 500){
            // 예상치 못한 오류 발생시키기
            int[] array = {1,2,3,4,5};
            System.out.println(array[50]);
        }

        if(request.getErrorCode() == 403){
            throw new AuthForbiddenException(ErrorDefineCode.EXAMPLE_OCCURE_ERROR);
        }

        if(request.getErrorCode() == 404){
            throw new AuthForbiddenException(ErrorDefineCode.EXAMPLE_OCCURE_ERROR);
        }

        return new CommonResponse(true, HttpStatus.OK, "무언가가 성공하기 전에 예외 발생", null);
    }
}
