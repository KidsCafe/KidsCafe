package com.sparta.kidscafe.domain.reservation.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReservationDetail is a Querydsl query type for ReservationDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReservationDetail extends EntityPathBase<ReservationDetail> {

    private static final long serialVersionUID = 829820192L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReservationDetail reservationDetail = new QReservationDetail("reservationDetail");

    public final NumberPath<Long> count = createNumber("count", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final QReservation reservation;

    public final NumberPath<Long> targetId = createNumber("targetId", Long.class);

    public final EnumPath<com.sparta.kidscafe.common.enums.TargetType> targetType = createEnum("targetType", com.sparta.kidscafe.common.enums.TargetType.class);

    public QReservationDetail(String variable) {
        this(ReservationDetail.class, forVariable(variable), INITS);
    }

    public QReservationDetail(Path<? extends ReservationDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReservationDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReservationDetail(PathMetadata metadata, PathInits inits) {
        this(ReservationDetail.class, metadata, inits);
    }

    public QReservationDetail(Class<? extends ReservationDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reservation = inits.isInitialized("reservation") ? new QReservation(forProperty("reservation"), inits.get("reservation")) : null;
    }

}

