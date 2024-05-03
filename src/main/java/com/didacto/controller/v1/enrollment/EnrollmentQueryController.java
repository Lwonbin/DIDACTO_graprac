package com.didacto.controller.v1.enrollment;

import com.didacto.common.response.CommonResponse;
import com.didacto.domain.Enrollment;
import com.didacto.dto.enrollment.EnrollmentPageResponse;
import com.didacto.dto.enrollment.EnrollmentQueryFilter;
import com.didacto.dto.enrollment.EnrollmentResponse;
import com.didacto.service.enrollment.EnrollmentQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/enrollment")
@Tag(name = "ENROLLMENT-QUERY API", description = "강의 등록 요청 및 처리 API")
public class EnrollmentQueryController {

    private final EnrollmentQueryService enrollmentQueryService;

    @GetMapping("/{enrollmentId}")
    @Operation(summary = "ENROLL_QUERY_01 : 강의 등록 요청 데이터 조회")
    public CommonResponse<EnrollmentResponse> queryOne(
            @Schema(example = "1")
            @PathVariable("enrollmentId") Long enrollmentId
    ){
        Enrollment enrollment = enrollmentQueryService.queryOne(enrollmentId);
        return new CommonResponse(
                true,
                HttpStatus.OK,
                "강의 등록 요청을 조회하였습니다.",
                new EnrollmentResponse(enrollment)
        );
    }

    @GetMapping()
    @Operation(summary = "ENROLL_QUERY_02 : 강의 등록 요청 목록 조회")
    public CommonResponse<EnrollmentPageResponse> queryEnrollmentsByUser(
            @PageableDefault(size = 100)
            @SortDefault(sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable,
            @ParameterObject EnrollmentQueryFilter request
    ){
        EnrollmentPageResponse enrollmentPageResponse = enrollmentQueryService.queryPage(pageable, request);

        return new CommonResponse(
                true,
                HttpStatus.OK,
                "강의 등록 요청 목록을 조회하였습니다.",
                enrollmentPageResponse
        );
    }
}
