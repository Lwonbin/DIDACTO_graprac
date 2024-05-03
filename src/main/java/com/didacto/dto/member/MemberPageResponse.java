package com.didacto.dto.member;

import com.didacto.domain.Member;
import com.didacto.dto.PageResponse;
import com.didacto.dto.PageInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberPageResponse extends PageResponse {
    private List<MemberResponse> members;

    public MemberPageResponse(
            PageInfoResponse pageInfo,
            List<Member> members
    ){
        super(pageInfo);
        this.members = members.stream()
                .map(MemberResponse::new)
                .toList();
    }
}
