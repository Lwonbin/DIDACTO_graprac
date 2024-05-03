package com.didacto.dto.enrollment;

import com.didacto.domain.EnrollmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollmentQueryFilter {
    private Long id;
    @Schema(description = "WAITING || CANCELLED || ACCEPTED || REJECTED")
    private EnrollmentStatus status;
    private Long lectureId;
    private Long memberId;
}

