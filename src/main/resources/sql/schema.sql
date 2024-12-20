-- 데이터베이스 사용
CREATE DATABASE IF NOT EXISTS dev_database;
USE dev_database;

-- users 테이블 생성
CREATE TABLE IF NOT EXISTS users
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- posts 테이블 생성
CREATE TABLE IF NOT EXISTS posts
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id    INT          NOT NULL,
    title      VARCHAR(100) NOT NULL,
    content    TEXT         NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- comments 테이블 생성
CREATE TABLE IF NOT EXISTS comments
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    post_id    INT  NOT NULL,
    user_id    INT  NOT NULL,
    content    TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- likes 테이블 생성
CREATE TABLE IF NOT EXISTS likes
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    post_id    INT NOT NULL,
    user_id    INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
-- cafes 테이블 생성
CREATE TABLE IF NOT EXISTS cafes
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    region       VARCHAR(50)  NOT NULL,
    address      VARCHAR(255) NOT NULL,
    size         INT          NOT NULL,   -- 카페 크기 (평수)
    multi_family BOOLEAN   DEFAULT FALSE, -- 다둥이 할인 여부
    room_exists  BOOLEAN   DEFAULT FALSE, -- 방 유무
    day_off      VARCHAR(50),             -- 휴무일
    parking      BOOLEAN   DEFAULT FALSE, -- 주차 가능 여부
    restaurant   BOOLEAN   DEFAULT FALSE, -- 식당 유무
    hyperlink    VARCHAR(255),            -- 링크
    opened_at    TIME         NOT NULL,
    closed_at    TIME         NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- price_policy 테이블 생성
CREATE TABLE IF NOT EXISTS price_policy
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    cafe_id     INT                            NOT NULL,
    target_type ENUM ('FEE', 'ROOM', 'PEOPLE') NOT NULL, -- TargetType Enum 정의
    target_id   INT                            NOT NULL,
    title       VARCHAR(255)                   NOT NULL,
    day_type    VARCHAR(255)                   NOT NULL,
    rate        DOUBLE                         NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cafe_id) REFERENCES cafes (id) ON DELETE CASCADE
);