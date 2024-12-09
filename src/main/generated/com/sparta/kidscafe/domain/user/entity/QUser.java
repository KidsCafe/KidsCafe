package com.sparta.kidscafe.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1712967391L;

    public static final QUser user = new QUser("user");

    public final com.sparta.kidscafe.common.entity.QTimestamped _super = new com.sparta.kidscafe.common.entity.QTimestamped(this);

    public final StringPath address = createString("address");

    public final ListPath<com.sparta.kidscafe.domain.bookmark.entity.Bookmark, com.sparta.kidscafe.domain.bookmark.entity.QBookmark> bookmarks = this.<com.sparta.kidscafe.domain.bookmark.entity.Bookmark, com.sparta.kidscafe.domain.bookmark.entity.QBookmark>createList("bookmarks", com.sparta.kidscafe.domain.bookmark.entity.Bookmark.class, com.sparta.kidscafe.domain.bookmark.entity.QBookmark.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final ListPath<com.sparta.kidscafe.domain.reservation.entity.Reservation, com.sparta.kidscafe.domain.reservation.entity.QReservation> reservations = this.<com.sparta.kidscafe.domain.reservation.entity.Reservation, com.sparta.kidscafe.domain.reservation.entity.QReservation>createList("reservations", com.sparta.kidscafe.domain.reservation.entity.Reservation.class, com.sparta.kidscafe.domain.reservation.entity.QReservation.class, PathInits.DIRECT2);

    public final ListPath<com.sparta.kidscafe.domain.review.entity.Review, com.sparta.kidscafe.domain.review.entity.QReview> reviews = this.<com.sparta.kidscafe.domain.review.entity.Review, com.sparta.kidscafe.domain.review.entity.QReview>createList("reviews", com.sparta.kidscafe.domain.review.entity.Review.class, com.sparta.kidscafe.domain.review.entity.QReview.class, PathInits.DIRECT2);

    public final EnumPath<com.sparta.kidscafe.common.enums.RoleType> role = createEnum("role", com.sparta.kidscafe.common.enums.RoleType.class);

    public final EnumPath<com.sparta.kidscafe.common.enums.SocialLoginType> socialType = createEnum("socialType", com.sparta.kidscafe.common.enums.SocialLoginType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

