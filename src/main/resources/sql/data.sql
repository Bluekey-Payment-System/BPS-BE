-------------------
-- Artist Member --
-------------------

-- 1
INSERT INTO member (email, login_id, password, name, en_name, commission_rate, type, role, profile_image, created_at, modified_at, is_removed)
VALUES ('artist1@example.com', 'artist1', '$2a$12$b46rJp7uMeFkgWcnkyfOr.A9PGlDxmyJE0mKuAnt.KXBq6zYro33S', '혁기', 'hucki', 10, 'USER', 'ARTIST', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 2
INSERT INTO member (email, login_id, password, name, en_name, commission_rate, type, role, profile_image, created_at, modified_at, is_removed)
VALUES ('artist2@example.com', 'artist2', '$2a$12$b46rJp7uMeFkgWcnkyfOr.A9PGlDxmyJE0mKuAnt.KXBq6zYro33S', '53X', '53X', 20, 'USER', 'ARTIST', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 3
INSERT INTO member (email, login_id, password, name, en_name, commission_rate, type, role, profile_image, created_at, modified_at, is_removed)
VALUES ('artist3@example.com', 'artist3', '$2a$12$b46rJp7uMeFkgWcnkyfOr.A9PGlDxmyJE0mKuAnt.KXBq6zYro33S', '덤프', 'dump', 20, 'USER', 'ARTIST', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 4
INSERT INTO member (email, login_id, password, name, en_name, commission_rate, type, role, profile_image, created_at, modified_at, is_removed)
VALUES ('artist4@example.com', 'artist4', '$2a$12$b46rJp7uMeFkgWcnkyfOr.A9PGlDxmyJE0mKuAnt.KXBq6zYro33S', '이은성', '이은성', 20, 'USER', 'ARTIST', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- -- 5
-- INSERT INTO member (email, login_id, password, name, en_name, commission_rate, type, role, profile_image, created_at, modified_at, is_removed)
-- VALUES ('artist5@example.com', 'artist5', '$2a$12$b46rJp7uMeFkgWcnkyfOr.A9PGlDxmyJE0mKuAnt.KXBq6zYro33S', 'SKY', 'SKY', 20, 'USER', 'ARTIST', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 5
INSERT INTO member (email, login_id, password, name, en_name, commission_rate, type, role, profile_image, created_at, modified_at, is_removed)
VALUES ('artist5@example.com', 'artist5', '$2a$12$b46rJp7uMeFkgWcnkyfOr.A9PGlDxmyJE0mKuAnt.KXBq6zYro33S', '김세훈', '김세훈', 20, 'USER', 'ARTIST', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 6
INSERT INTO member (email, login_id, password, name, en_name, commission_rate, type, role, profile_image, created_at, modified_at, is_removed)
VALUES ('artist6@example.com', 'artist6', '$2a$12$b46rJp7uMeFkgWcnkyfOr.A9PGlDxmyJE0mKuAnt.KXBq6zYro33S', '블루키', '블루키', 20, 'USER', 'ARTIST', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 7
INSERT INTO member (email, login_id, password, name, en_name, commission_rate, type, role, profile_image, created_at, modified_at, is_removed)
VALUES ('artist7@example.com', 'artist7', '$2a$12$b46rJp7uMeFkgWcnkyfOr.A9PGlDxmyJE0mKuAnt.KXBq6zYro33S', '송민섭', '송민섭', 30, 'USER', 'ARTIST', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);


------------------
-- admin Member --
------------------

-- 8
INSERT INTO member (email, login_id, password, name, role, type, profile_image, created_at, modified_at, is_removed)
VALUES ('admin@example.com', 'super_admin', '$2a$12$b46rJp7uMeFkgWcnkyfOr.A9PGlDxmyJE0mKuAnt.KXBq6zYro33S', 'bluekey', 'SUPER_ADMIN', 'ADMIN', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 9
INSERT INTO member (email, login_id, password, name, role, type, profile_image, created_at, modified_at, is_removed)
VALUES ('admin1@example.com', 'admin1234', '$2a$12$b46rJp7uMeFkgWcnkyfOr.A9PGlDxmyJE0mKuAnt.KXBq6zYro33S', 'bluekey1', 'ADMIN', 'ADMIN', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);


-----------
-- Album --
-----------

-- 1
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (1, '혁기', '사랑에도 공식이 있나요 OST PART 2', '사랑에도 공식이 있나요 OST PART 2', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 2
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (1, '혁기', 'COME TO ME', 'COME TO ME', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 3
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (1, '혁기', 'Underwater', 'Underwater', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 4
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (1, '혁기', '프라하', 'Praha', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 5
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (1, '혁기', 'Is there an equation in Love? Pt. 2 (Original Motion Picture Soundtrack)', 'Is there an equation in Love? Pt. 2 (Original Motion Picture Soundtrack)', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 6
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (2, '53X', 'C&C', 'C&C', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 7
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (2, '53X', 'About Money', 'About Money', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 8
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (null, null, '미녀 OST', '미녀 OST', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 9
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (6, '블루키', '미녀 Original Score', '미녀 Original Score', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 10
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (7, '송민섭', 'A Trilogia', 'A Trilogia', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);


-----------
-- Track --
-----------

-- 1
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (1, '그대 안아줄게요', '그대 안아줄게요', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 2
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (2, 'Praha', 'Praha', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 3
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (2, 'Wave love', 'Wave love', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

--4
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (3, 'Underwater', 'Underwater', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 5
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (3, 'Hold me like the ocean', 'Hold me like the ocean', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 6
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (6, 'C&C', 'C&C (feat. Xio)', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 7
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (7, '돈', 'About Money (feat. BRVR, Daily Wave, Juel)', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 8
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (2, 'Burn', 'Burn', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 9
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (2, 'Headache', 'Headache', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 10
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (4, 'Praha (inst.)', 'Praha (inst.)', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 11
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (1, '내 마음 모르죠', 'mind', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 12
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (1, '그대 안아줄게요 (Acoustic ver.)', '그대 안아줄게요 (Acoustic ver.)', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 13
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (5, 'You make me hug', 'You make me hug', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 14
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (5, 'Can you read my heart', 'Can you read my heart', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 15
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (1, '내 마음 모르죠 (Acoustic ver.)', '내 마음 모르죠 (Acoustic ver.)', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 16
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (2, 'Come to me', 'Come to me', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 17
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (2, 'Come to me (inst.)', 'Come to me (inst.)', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 18
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (8, '내 세상은', '내 세상은', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- -- 19
-- INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
-- VALUES (8, 'Wait For You', 'Wait For You', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 19
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (8, 'love ya', 'love ya', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 20
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (8, '고백이야', '고백이야', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 21
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (8, '비밀스럽게', '비밀스럽게', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 22
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'Truth', 'Truth', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 23
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'Witch', 'Witch', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 24
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'Glasses', 'Glasses', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 25
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'Evil', 'Evil', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 26
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'Hash Tag', 'Hash Tag', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 27
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'Nervous', 'Nervous', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 28
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'Tarot', 'Tarot', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 29
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'Magic Show', 'Magic Show', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 30
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'Moring Mood', 'Moring Mood', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 31
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'Sweet Couple', 'Sweet Couple', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 32
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'FollowFollow', 'FollowFollow', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 33
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'Return', 'Return', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 34
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'Omen', 'Omen', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 35
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'Strange', 'Strange', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 36
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (10, '물방울 (feat. 수잔)', '물방울 (feat. 수잔)', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 37
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (10, '물방울', '물방울', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 38
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (10, '봄비', '봄비', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 38
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (10, '비행', '비행', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 39
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (10, '수선화', '수선화', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 40
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (10, '아지랑이', '아지랑이', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 41
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (10, '아직', '아직', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 42
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (10, '처서', '처서', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 43
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (10, 'Caminhada', 'Caminhada', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 44
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, '미녀 Title', '미녀 Title', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 45
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'Voice', 'Voice', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 46
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'Emergency', 'Emergency', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 47
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'What gives?', 'What gives?', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 48
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'The Divine Will', 'The Divine Will', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 49
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'karma', 'karma', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 50
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'Death is Coming', 'Death is Coming', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 51
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'Sugar Waltz', 'Sugar Waltz', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 52
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'Mother', 'Mother', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 53
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'Scamp', 'Scamp', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 54
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (9, 'Past Life', 'Past Life', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);


-----------------
-- TrackMember --
-----------------

-- 1
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (1, 1, '혁기', 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 2
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (1, 3, '덤프', 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 3
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (2, 1, '혁기', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 4
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (3, 1, '혁기', 60, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 5
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (4, 1, '혁기', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 6
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (5, 1, '혁기', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 7
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (6, 2, '53X', 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 8
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (7, 2, '53X', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 9
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (8, 1, '혁기', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 10
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (9, 1, '혁기', 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 11
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (10, 1, '혁기', 35, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 12
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (11, null, '김여름', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 13
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (12, 1, '혁기', 40, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 14
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (13, 1, '혁기', 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 15
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (14, 1, '혁기', 40, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 16
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (15, null, '김여름', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 17
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (16, 1, '혁기', 40, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 18
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (17, 1, '혁기', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 19
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (18, 4, '이은성', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 20
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (19, 5, '김세훈', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 21
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (20, 1, '혁기', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 22
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (21, 1, '혁기', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 23
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (22, 6, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 24
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (23, 6, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 25
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (24, 6, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 26
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (25, 6, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 27
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (26, 6, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 28
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (27, 6, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 29
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (28, 6, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 30
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (29, 6, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 31
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (30, 6, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 32
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (31, 6, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 33
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (32, 6, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 34
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (33, 6, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 35
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (34, 6, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 36
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (35, 6, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 37
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (36, 7, '송민섭', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 38
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (37, 7, '송민섭', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 39
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (38, 7, '송민섭', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 40
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (39, 7, '송민섭', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 41
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (40, 7, '송민섭', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 42
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (41, 7, '송민섭', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 43
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (42, 7, '송민섭', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 44
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (43, 7, '송민섭', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 45
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (44, 7, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 46
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (45, 7, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 47
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (46, 7, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 48
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (47, 7, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 49
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (48, 7, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 50
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (49, 7, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 51
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (50, 7, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 52
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (51, 7, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 53
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (52, 7, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 54
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (53, 7, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 55
INSERT INTO track_member (track_id, member_id, name, commission_rate, created_at, modified_at, is_removed)
VALUES (54, 7, '블루키', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

