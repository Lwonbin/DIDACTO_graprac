package com.didacto.service.enrollment;


import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.domain.*;
import com.didacto.dto.PageInfoResponse;
import com.didacto.dto.enrollment.EnrollmentQueryFilter;
import com.didacto.dto.enrollment.*;
import com.didacto.repository.enrollment.EnrollmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class EnrollmentQueryService {
    private final EnrollmentRepository enrollmentRepository;

    public Enrollment queryOne(Long enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new NoSuchElementFoundException404(ErrorDefineCode.ENROLLMENT_NOT_FOUND));
    }

    public Enrollment queryOne(EnrollmentQueryFilter request){
        return enrollmentRepository.findEnrollment(request)
                .orElseThrow(() -> new NoSuchElementFoundException404(ErrorDefineCode.LECTURE_NOT_FOUND));
    }

    public List<Enrollment> query(EnrollmentQueryFilter request){
        return enrollmentRepository.findEnrollments(request);
    }

    public EnrollmentPageResponse queryPage(Pageable pageable, EnrollmentQueryFilter request) {
        long page = pageable.getOffset();
        long size = pageable.getPageSize();

        // Query : 페이지네이션 및 조건 필터링
        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentPage(pageable, request);

        // Query : Pagenation을 위한 총 개수 집계
        long count = enrollmentRepository.countEnrollments(request);

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

        return new EnrollmentPageResponse(pageInfo, enrollments);
    }
}
