package com.sparta.kidscafe.domain.fee.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFee is a Querydsl query type for Fee
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFee extends EntityPathBase<Fee> {

    private static final long serialVersionUID = 1211874991L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFee fee1 = new QFee("fee1");

    public final com.sparta.kidscafe.common.entity.QTimestamped _super = new com.sparta.kidscafe.common.entity.QTimestamped(this);

    public final EnumPath<com.sparta.kidscafe.common.enums.AgeGroup> ageGroup = createEnum("ageGroup", com.sparta.kidscafe.common.enums.AgeGroup.class);

    public final com.sparta.kidscafe.domain.cafe.entity.QCafe cafe;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> fee = createNumber("fee", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QFee(String variable) {
        this(Fee.class, forVariable(variable), INITS);
    }

    public QFee(Path<? extends Fee> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFee(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFee(PathMetadata metadata, PathInits inits) {
        this(Fee.class, metadata, inits);
    }

    public QFee(Class<? extends Fee> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cafe = inits.isInitialized("cafe") ? new com.sparta.kidscafe.domain.cafe.entity.QCafe(forProperty("cafe"), inits.get("cafe")) : null;
    }

}

