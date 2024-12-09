package com.sparta.kidscafe.domain.cafe.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCafe is a Querydsl query type for Cafe
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCafe extends EntityPathBase<Cafe> {

    private static final long serialVersionUID = -40321149L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCafe cafe = new QCafe("cafe");

    public final com.sparta.kidscafe.common.entity.QTimestamped _super = new com.sparta.kidscafe.common.entity.QTimestamped(this);

    public final StringPath address = createString("address");

    public final ListPath<com.sparta.kidscafe.domain.bookmark.entity.Bookmark, com.sparta.kidscafe.domain.bookmark.entity.QBookmark> bookmarks = this.<com.sparta.kidscafe.domain.bookmark.entity.Bookmark, com.sparta.kidscafe.domain.bookmark.entity.QBookmark>createList("bookmarks", com.sparta.kidscafe.domain.bookmark.entity.Bookmark.class, com.sparta.kidscafe.domain.bookmark.entity.QBookmark.class, PathInits.DIRECT2);

    public final ListPath<CafeImage, QCafeImage> cafeImages = this.<CafeImage, QCafeImage>createList("cafeImages", CafeImage.class, QCafeImage.class, PathInits.DIRECT2);

    public final TimePath<java.time.LocalTime> closedAt = createTime("closedAt", java.time.LocalTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath dayOff = createString("dayOff");

    public final ListPath<com.sparta.kidscafe.domain.fee.entity.Fee, com.sparta.kidscafe.domain.fee.entity.QFee> fees = this.<com.sparta.kidscafe.domain.fee.entity.Fee, com.sparta.kidscafe.domain.fee.entity.QFee>createList("fees", com.sparta.kidscafe.domain.fee.entity.Fee.class, com.sparta.kidscafe.domain.fee.entity.QFee.class, PathInits.DIRECT2);

    public final StringPath hyperlink = createString("hyperlink");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath multiFamily = createBoolean("multiFamily");

    public final StringPath name = createString("name");

    public final TimePath<java.time.LocalTime> openedAt = createTime("openedAt", java.time.LocalTime.class);

    public final BooleanPath parking = createBoolean("parking");

    public final ListPath<com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy, com.sparta.kidscafe.domain.pricepolicy.entity.QPricePolicy> pricePolicies = this.<com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy, com.sparta.kidscafe.domain.pricepolicy.entity.QPricePolicy>createList("pricePolicies", com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy.class, com.sparta.kidscafe.domain.pricepolicy.entity.QPricePolicy.class, PathInits.DIRECT2);

    public final StringPath region = createString("region");

    public final BooleanPath restaurant = createBoolean("restaurant");

    public final ListPath<com.sparta.kidscafe.domain.room.entity.Room, com.sparta.kidscafe.domain.room.entity.QRoom> rooms = this.<com.sparta.kidscafe.domain.room.entity.Room, com.sparta.kidscafe.domain.room.entity.QRoom>createList("rooms", com.sparta.kidscafe.domain.room.entity.Room.class, com.sparta.kidscafe.domain.room.entity.QRoom.class, PathInits.DIRECT2);

    public final NumberPath<Integer> size = createNumber("size", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.sparta.kidscafe.domain.user.entity.QUser user;

    public QCafe(String variable) {
        this(Cafe.class, forVariable(variable), INITS);
    }

    public QCafe(Path<? extends Cafe> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCafe(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCafe(PathMetadata metadata, PathInits inits) {
        this(Cafe.class, metadata, inits);
    }

    public QCafe(Class<? extends Cafe> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.sparta.kidscafe.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

