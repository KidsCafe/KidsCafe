package com.sparta.kidscafe.domain.pricepolicy.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPricePolicy is a Querydsl query type for PricePolicy
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPricePolicy extends EntityPathBase<PricePolicy> {

    private static final long serialVersionUID = 869640111L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPricePolicy pricePolicy = new QPricePolicy("pricePolicy");

    public final com.sparta.kidscafe.common.entity.QTimestamped _super = new com.sparta.kidscafe.common.entity.QTimestamped(this);

    public final com.sparta.kidscafe.domain.cafe.entity.QCafe cafe;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath dayType = createString("dayType");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> rate = createNumber("rate", Double.class);

    public final NumberPath<Long> targetId = createNumber("targetId", Long.class);

    public final EnumPath<com.sparta.kidscafe.common.enums.TargetType> targetType = createEnum("targetType", com.sparta.kidscafe.common.enums.TargetType.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPricePolicy(String variable) {
        this(PricePolicy.class, forVariable(variable), INITS);
    }

    public QPricePolicy(Path<? extends PricePolicy> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPricePolicy(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPricePolicy(PathMetadata metadata, PathInits inits) {
        this(PricePolicy.class, metadata, inits);
    }

    public QPricePolicy(Class<? extends PricePolicy> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cafe = inits.isInitialized("cafe") ? new com.sparta.kidscafe.domain.cafe.entity.QCafe(forProperty("cafe"), inits.get("cafe")) : null;
    }

}

