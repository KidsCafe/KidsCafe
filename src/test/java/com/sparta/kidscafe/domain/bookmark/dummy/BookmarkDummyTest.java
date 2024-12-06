package com.sparta.kidscafe.domain.bookmark.dummy;

import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.common.util.TestUtil;
import com.sparta.kidscafe.domain.bookmark.repository.BookmarkRepository;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import com.sparta.kidscafe.dummy.DummyBookmark;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
public class BookmarkDummyTest {

  @Autowired
  private CafeRepository cafeRepository;

  @Autowired
  private BookmarkRepository bookmarkRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  @Rollback(false)
  void createBookmark() {
    // user dummy test 돌려야함
    // cafe dummy test를 돌리고 와야함
    List<User> users = userRepository.findAllByRole(RoleType.USER);
    List<Cafe> cafes = cafeRepository.findAll();
    for (User user : users) {
      Collections.shuffle(cafes);
      List<Cafe> bookmarkedCafes = cafes.subList(0, TestUtil.getRandomInteger(1, cafes.size()));
      bookmarkRepository.saveAll(DummyBookmark.createDummyBookmarks(user, bookmarkedCafes));
    }
  }
}
