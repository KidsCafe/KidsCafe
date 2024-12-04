-- 데이터베이스 사용
USE dev_database;

-- users 테이블 초기 데이터 삽입
INSERT INTO users (username, email, password) VALUES
                                                  ('김혜진', 'hyejin@example.com', '$2a$10$examplehashforpassword1'),
                                                  ('김혜수', 'hyesoo@example.com', '$2a$10$examplehashforpassword2');

-- posts 테이블 초기 데이터 삽입
INSERT INTO posts (user_id, title, content) VALUES
                                                (1, '첫 번째 글', '이것은 첫 번째 글의 내용입니다.'),
                                                (2, '두 번째 글', '이것은 두 번째 글의 내용입니다.'),
                                                (1, '다른 글', '이것은 김혜진이 작성한 또 다른 글입니다.');

-- comments 테이블 초기 데이터 삽입
INSERT INTO comments (post_id, user_id, content) VALUES
                                                     (1, 2, '김혜수가 첫 번째 글에 댓글을 남겼습니다.'),
                                                     (2, 1, '김혜진이 두 번째 글에 댓글을 남겼습니다.'),
                                                     (3, 2, '김혜수가 김혜진의 다른 글에 댓글을 남겼습니다.');

-- likes 테이블 초기 데이터 삽입
INSERT INTO likes (post_id, user_id) VALUES
                                         (1, 2),
                                         (1, 1),
                                         (2, 2),
                                         (3, 1);
