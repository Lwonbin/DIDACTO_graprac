package com.didacto.service.member;

import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.domain.Member;
import com.didacto.dto.PageInfoResponse;
import com.didacto.dto.member.MemberPageResponse;
import com.didacto.dto.member.MemberQueryFilter;
import com.didacto.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryService {
    private final MemberRepository memberRepository;

    public Member queryOne(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementFoundException404(ErrorDefineCode.MEMBER_NOT_FOUND));
    }

    public List<Member> query(MemberQueryFilter request){
        return memberRepository.findMembers(request);
    }

    public MemberPageResponse queryPage(Pageable pageable, MemberQueryFilter request) {
        long page = pageable.getOffset();
        long size = pageable.getPageSize();

        // Query : 페이지네이션 및 조건 필터링
        List<Member> members = memberRepository.findMemberPage(pageable, request);

        // Query : Pagenation을 위한 총 개수 집계
        Long count = memberRepository.countMembers(request);

        // Calc : 총 페이지 수와 다음 페이지 존재 여부 계산
        long totalPage = (long) Math.ceil((double) count / size);
        boolean isHaveNext = page < totalPage;

        // Out
        PageInfoResponse pageInfo = PageInfoResponse.builder()
                .pageNo(page)
                .pageSize(size)
                .totalPages(totalPage)
                .totalElements(count)
                .haveNext(isHaveNext)
                .build();

        return new MemberPageResponse(pageInfo, members);
    }
}
