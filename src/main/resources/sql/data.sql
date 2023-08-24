-- Artist Member
INSERT INTO member (email, login_id, password, name, en_name, commission_rate, type, role, profile_image, created_at, modified_at, is_removed)
VALUES ('artist1@example.com', 'artist1', '$2a$12$b46rJp7uMeFkgWcnkyfOr.A9PGlDxmyJE0mKuAnt.KXBq6zYro33S', '혁기', 'hucki', 10, 'USER', 'ARTIST', 'image/profile/artist.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

INSERT INTO member (email, login_id, password, name, en_name, commission_rate, type, role, profile_image, created_at, modified_at, is_removed)
VALUES ('artist2@example.com', 'artist2', '$2a$12$b46rJp7uMeFkgWcnkyfOr.A9PGlDxmyJE0mKuAnt.KXBq6zYro33S', '53X', '53X', 20, 'USER', 'ARTIST', 'image/profile/artist.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

INSERT INTO member (email, login_id, password, name, en_name, commission_rate, type, role, profile_image, created_at, modified_at, is_removed)
VALUES ('artist3@example.com', 'artist3', 'password123!', '덤프', 'dump', 20, 'USER', 'ARTIST', 'image/profile/artist.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

INSERT INTO member (email, login_id, password, name, en_name, commission_rate, type, role, profile_image, created_at, modified_at, is_removed)
VALUES ('artist4@example.com', 'artist4', '$2a$12$b46rJp7uMeFkgWcnkyfOr.A9PGlDxmyJE0mKuAnt.KXBq6zYro33S', '이은성', '이은성', 20, 'USER', 'ARTIST', 'image/profile/artist.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

INSERT INTO member (email, login_id, password, name, en_name, commission_rate, type, role, profile_image, created_at, modified_at, is_removed)
VALUES ('artist5@example.com', 'artist5', '$2a$12$b46rJp7uMeFkgWcnkyfOr.A9PGlDxmyJE0mKuAnt.KXBq6zYro33S', 'SKY', 'SKY', 20, 'USER', 'ARTIST', 'image/profile/artist.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

INSERT INTO member (email, login_id, password, name, en_name, commission_rate, type, role, profile_image, created_at, modified_at, is_removed)
VALUES ('artist6@example.com', 'artist6', '$2a$12$b46rJp7uMeFkgWcnkyfOr.A9PGlDxmyJE0mKuAnt.KXBq6zYro33S', '블루키', '블루키', 20, 'USER', 'ARTIST', 'image/profile/artist.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);


-- admin Member
INSERT INTO member (email, login_id, password, name, role, type, profile_image, created_at, modified_at, is_removed)
VALUES ('admin@example.com', 'super_admin', '$2a$12$b46rJp7uMeFkgWcnkyfOr.A9PGlDxmyJE0mKuAnt.KXBq6zYro33S', 'bluekey', 'SUPER_ADMIN', 'ADMIN', 'image/profile/admin.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

INSERT INTO member (email, login_id, password, name, role, type, profile_image, created_at, modified_at, is_removed)
VALUES ('admin1@example.com', 'admin1234', '$2a$12$b46rJp7uMeFkgWcnkyfOr.A9PGlDxmyJE0mKuAnt.KXBq6zYro33S', 'bluekey1', 'ADMIN', 'ADMIN', 'image/profile/admin.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);


-- Album
-- 1
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (1, '혁기', '사랑에도 공식이 있나요 OST PART 2', '사랑에도 공식이 있나요 OST PART 2', 'image/album.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 2
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (1, '혁기', 'COME TO ME', 'COME TO ME', 'image/album.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 3
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (1, '혁기', 'Underwater', 'Underwater', 'image/album.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 4
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (1, '혁기', '프라하', 'Praha', 'image/album.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 5
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (1, '혁기', 'Is there an equation in Love? Pt. 2 (Original Motion Picture Soundtrack)', 'Is there an equation in Love? Pt. 2 (Original Motion Picture Soundtrack)', 'image/album.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 6
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (2, '53X', 'C&C', 'C&C', 'image/album.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 7
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (2, '53X', 'About Money', 'About Money', 'image/album.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 8
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (null, null, '미녀 OST', '미녀 OST', 'image/album.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 9
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (6, '블루키', '미녀 Original Score', '미녀 Original Score', 'image/album.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);



-- Track
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

-- 19
INSERT INTO track (album_id, name, en_name, is_original_track, created_at, modified_at, is_removed)
VALUES (8, 'Wait For You', 'Wait For You', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

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

-- TrackMember

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
VALUES (19, 5, 'SKY', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

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
