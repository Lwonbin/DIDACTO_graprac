package com.didacto.controller.v1.member;

import com.didacto.common.response.CommonResponse;
import com.didacto.config.security.SecurityUtil;
import com.didacto.dto.member.MemberModificationRequest;
import com.didacto.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@Tag(name = "MEMBER COMMAND API", description = "회원과 관련된 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "MEMBER_01 : 회원 정보 수정 API", description = "회원 정보를 수정한다.")
    @PutMapping("/members")
    public CommonResponse editMemberInfo(@RequestBody MemberModificationRequest memberEditRequest){
        Long userId = SecurityUtil.getCurrentMemberId();
        memberService.modifyInfo(userId, memberEditRequest);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userId,
                memberEditRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new CommonResponse<>(true, HttpStatus.OK, "회원 정보 수정에 성공했습니다.", null);
    }

    @Operation(summary = "MEMBER_02 : 회원 탈퇴 API", description = "회원을 탈퇴시킨다.")
    @DeleteMapping("/members")
    public CommonResponse deleteMemberInfo() {
        Long userId = SecurityUtil.getCurrentMemberId();
        memberService.delete(userId);
        return new CommonResponse<>(true, HttpStatus.OK, "회원이 탈퇴되었습니다.", null);
    }
}
