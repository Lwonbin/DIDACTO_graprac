package com.didacto.repository.member;

import com.didacto.domain.Member;
import com.didacto.dto.member.MemberQueryFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberCustomRepository {
    List<Member> findMemberPage(Pageable pageable, MemberQueryFilter request);
    Long countMembers(MemberQueryFilter request);
}
