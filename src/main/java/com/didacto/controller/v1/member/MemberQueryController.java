package com.didacto.controller.v1.member;

import com.didacto.common.response.CommonResponse;
import com.didacto.domain.Member;
import com.didacto.dto.member.MemberPageResponse;
import com.didacto.dto.member.MemberQueryFilter;
import com.didacto.dto.member.MemberResponse;
import com.didacto.service.member.MemberQueryService;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "MEMBER QUERY API", description = "회원과 관련된 API") // Swagger Docs : API 이름
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/member")
public class MemberQueryController {
    private final MemberQueryService memberQueryService;

    @Operation(summary = "MEMBER_QUERY_01 : 회원 조회", description = "회원을 조회한다.")
    @GetMapping("{memberId}")
    public CommonResponse<MemberResponse> queryOne(@PathVariable("memberId") Long memberId) {
        Member member = memberQueryService.queryOne(memberId);

        return new CommonResponse(
                true,
                HttpStatus.OK,
                "회원을 조회하였습니다.",
                new MemberResponse(member)
        );
    }

    @GetMapping("page")
    @Operation(summary = "MEMBER_QUERY_02 : 회원 목록 조회")
    public CommonResponse<MemberPageResponse> queryPage(
            @PageableDefault(size = 100)
            @SortDefault(sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable,
            @ParameterObject MemberQueryFilter request
    ){
        MemberPageResponse memberPageResponse = memberQueryService.queryPage(pageable, request);

        return new CommonResponse(
                true,
                HttpStatus.OK,
                "회원 목록을 조회하였습니다.",
                memberPageResponse
        );
    }
}
