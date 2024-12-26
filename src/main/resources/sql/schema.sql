-- 데이터베이스 생성 및 사용
CREATE DATABASE IF NOT EXISTS kidscafe;
USE kidscafe;

-- users 테이블 생성
CREATE TABLE IF NOT EXISTS users
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- cafes 테이블 생성
CREATE TABLE IF NOT EXISTS cafes
(
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    name               VARCHAR(100) NOT NULL,   -- 카페 이름
    region             VARCHAR(50)  NOT NULL,   -- 지역명
    address            VARCHAR(255) NOT NULL,   -- 주소
    size               INT          NOT NULL,   -- 카페 크기 (평수)
    multi_family       BOOLEAN   DEFAULT FALSE, -- 다둥이 할인 여부
    day_off            VARCHAR(50),             -- 휴무일
    parking            BOOLEAN   DEFAULT FALSE, -- 주차 가능 여부
    restaurant         BOOLEAN   DEFAULT FALSE, -- 식당 유무
    care_service       BOOLEAN   DEFAULT FALSE, -- 돌봄 서비스 여부
    swimming_pool      BOOLEAN   DEFAULT FALSE,-- 수영장 여부
    clothes_rental     BOOLEAN   DEFAULT FALSE,-- 옷 대여 여부
    monitoring         BOOLEAN   DEFAULT FALSE, -- 모니터링 여부
    feeding_room       BOOLEAN   DEFAULT FALSE, -- 수유실 여부
    outdoor_playground BOOLEAN   DEFAULT FALSE, -- 야외 시설 여부
    safety_guard       BOOLEAN   DEFAULT FALSE, -- 안전관리요원 여부
    hyperlink          VARCHAR(255),            -- 카페 링크
    opened_at          TIME         NOT NULL,   -- 오픈 시간
    closed_at          TIME         NOT NULL,   -- 마감 시간
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- cafe_images 테이블 생성
CREATE TABLE IF NOT EXISTS cafe_images
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    cafe_id    INT          NOT NULL,
    image_path VARCHAR(255) NOT NULL, -- 이미지 경로
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cafe_id) REFERENCES cafes (id) ON DELETE CASCADE
);

-- price_policy 테이블 생성
CREATE TABLE IF NOT EXISTS price_policy
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    cafe_id     INT                            NOT NULL,
    target_type ENUM ('FEE', 'ROOM', 'PEOPLE') NOT NULL, -- 할인 대상 타입 (예: 요금, 방, 인원)
    target_id   INT                            NOT NULL, -- 할인 대상 ID
    title       VARCHAR(255)                   NOT NULL, -- 할인 제목
    day_type    VARCHAR(255)                   NOT NULL, -- 적용 요일
    rate        DOUBLE                         NOT NULL, -- 할인율 또는 요금
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cafe_id) REFERENCES cafes (id) ON DELETE CASCADE
);

-- reviews 테이블 생성
CREATE TABLE IF NOT EXISTS reviews
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    cafe_id    INT    NOT NULL,
    user_id    INT    NOT NULL,
    rating     DOUBLE NOT NULL, -- 별점
    content    TEXT   NOT NULL, -- 리뷰 내용
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cafe_id) REFERENCES cafes (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- lessons 테이블 생성
CREATE TABLE IF NOT EXISTS lessons
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    cafe_id     INT          NOT NULL,
    title       VARCHAR(255) NOT NULL, -- 수업 제목
    description TEXT,                  -- 수업 설명
    price       DOUBLE       NOT NULL, -- 수업 가격
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cafe_id) REFERENCES cafes (id) ON DELETE CASCADE
);

-- rooms 테이블 생성
CREATE TABLE IF NOT EXISTS rooms
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    cafe_id    INT          NOT NULL,
    name       VARCHAR(100) NOT NULL, -- 방 이름
    capacity   INT          NOT NULL, -- 방 최대 수용 인원
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cafe_id) REFERENCES cafes (id) ON DELETE CASCADE
);

-- bookmarks 테이블 생성
CREATE TABLE IF NOT EXISTS bookmarks
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id    INT NOT NULL,
    cafe_id    INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (cafe_id) REFERENCES cafes (id) ON DELETE CASCADE
);

-- Cafe SPATIAL INDEX 생성 JPA에서 제공 안해줌ㅠ
ALTER TABLE cafe ADD SPATIAL INDEX idx_location (location);