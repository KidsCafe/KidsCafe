package com.sparta.kidscafe.dummy;

import com.sparta.kidscafe.domain.bookmark.entity.Bookmark;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.user.entity.User;
import java.util.ArrayList;
import java.util.List;

public class DummyBookmark {

  public static Bookmark createDummyBookmark(User user, Cafe cafe) {
    return Bookmark.builder()
        .user(user)
        .cafe(cafe)
        .build();
  }

  public static List<Bookmark> createDummyBookmarks(User user, List<Cafe> cafes) {
    List<Bookmark> bookmarks = new ArrayList<>();
    for (Cafe cafe : cafes)
      bookmarks.add(createDummyBookmark(user, cafe));
    return bookmarks;
  }
}
