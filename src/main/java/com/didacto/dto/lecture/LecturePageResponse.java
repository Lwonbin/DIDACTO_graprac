package com.didacto.dto.lecture;

import com.didacto.domain.Lecture;
import com.didacto.dto.PageResponse;
import com.didacto.dto.PageInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LecturePageResponse extends PageResponse {
    private List<LectureResponse> lectureMembers;

    public LecturePageResponse(
            PageInfoResponse pageInfo,
            List<Lecture> lectures
    ){
        super(pageInfo);
        this.lectureMembers = lectures.stream()
                .map(LectureResponse::new)
                .toList();
    }
}