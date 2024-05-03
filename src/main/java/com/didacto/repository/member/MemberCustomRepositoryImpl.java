package com.didacto.repository.member;

import com.didacto.domain.Member;
import com.didacto.dto.member.MemberQueryFilter;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.didacto.domain.QMember.member;

@Repository
@AllArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> findMemberPage(Pageable pageable, MemberQueryFilter request) {
        JPAQuery<Member> query = queryWithFilter(request);

        for (Sort.Order order : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(member.getType(), member.getMetadata());
            query.orderBy(new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(order.getProperty())));
        }

        return query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Long countMembers(MemberQueryFilter request) {
        JPAQuery<Member> query = queryWithFilter(request);
        return query.fetchCount();
    }

    private JPAQuery<Member> queryWithFilter(MemberQueryFilter filter) {
        JPAQuery<Member> query = queryFactory.select(member)
                .from(member)
                .where(
                        filter.getEmail() != null ? member.email.eq(filter.getEmail()) : null,
                        filter.getName() != null ? member.name.eq(filter.getName()) : null,
                        filter.getDeleted() != null ? member.deleted.eq(filter.getDeleted()) : null
                );
        return query;
    }
}
