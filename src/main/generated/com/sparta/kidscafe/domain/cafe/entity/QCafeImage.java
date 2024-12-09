package com.sparta.kidscafe.domain.cafe.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCafeImage is a Querydsl query type for CafeImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCafeImage extends EntityPathBase<CafeImage> {

    private static final long serialVersionUID = -1832307816L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCafeImage cafeImage = new QCafeImage("cafeImage");

    public final com.sparta.kidscafe.common.entity.QTimestamped _super = new com.sparta.kidscafe.common.entity.QTimestamped(this);

    public final QCafe cafe;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imagePath = createString("imagePath");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCafeImage(String variable) {
        this(CafeImage.class, forVariable(variable), INITS);
    }

    public QCafeImage(Path<? extends CafeImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCafeImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCafeImage(PathMetadata metadata, PathInits inits) {
        this(CafeImage.class, metadata, inits);
    }

    public QCafeImage(Class<? extends CafeImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cafe = inits.isInitialized("cafe") ? new QCafe(forProperty("cafe"), inits.get("cafe")) : null;
    }

}

