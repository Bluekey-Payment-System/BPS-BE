-- Artist Member
INSERT INTO member (email, login_id, password, name, en_name, commission_rate, type, role, profile_image, created_at, modified_at, is_removed)
VALUES ('artist1@example.com', 'artist1', '$2a$12$b46rJp7uMeFkgWcnkyfOr.A9PGlDxmyJE0mKuAnt.KXBq6zYro33S', '혁기', 'hucki', 10, 'USER', 'ARTIST', 'artist.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

INSERT INTO member (email, login_id, password, name, en_name, commission_rate, type, role, profile_image, created_at, modified_at, is_removed)
VALUES ('artist2@example.com', 'artist2', '$2a$12$b46rJp7uMeFkgWcnkyfOr.A9PGlDxmyJE0mKuAnt.KXBq6zYro33S', '53X', '53X', 20, 'USER', 'ARTIST', 'artist.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

INSERT INTO member (email, login_id, password, name, en_name, commission_rate, type, role, profile_image, created_at, modified_at, is_removed)
VALUES ('artist3@example.com', 'artist3', 'password123!', '덤프', 'dump', 20, 'USER', 'ARTIST', 'artist.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);


-- admin Member
INSERT INTO member (email, login_id, password, name, role, type, profile_image, created_at, modified_at, is_removed)
VALUES ('admin@example.com', 'super_admin', '$2a$12$b46rJp7uMeFkgWcnkyfOr.A9PGlDxmyJE0mKuAnt.KXBq6zYro33S', 'bluekey', 'SUPER_ADMIN', 'ADMIN', 'admin.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

INSERT INTO member (email, login_id, password, name, role, type, profile_image, created_at, modified_at, is_removed)
VALUES ('admin1@example.com', 'admin1234', '$2a$12$b46rJp7uMeFkgWcnkyfOr.A9PGlDxmyJE0mKuAnt.KXBq6zYro33S', 'bluekey1', 'ADMIN', 'ADMIN', 'admin.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);


-- Album
-- 1
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (1, '혁기', '사랑에도 공식이 있나요 OST PART 2', '사랑에도 공식이 있나요 OST PART 2', 'album.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 2
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (1, '혁기', 'COME TO ME', 'COME TO ME', 'album.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 3
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (1, '혁기', 'Underwater', 'Underwater', 'album.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 4
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (1, '혁기', '프라하', 'Praha', 'album.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 5
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (1, '혁기', 'Is there an equation in Love? Pt. 2 (Original Motion Picture Soundtrack)', 'Is there an equation in Love? Pt. 2 (Original Motion Picture Soundtrack)', 'album.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 6
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (2, '53X', 'C&C', 'C&C', 'album.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 7
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image, created_at, modified_at, is_removed)
VALUES (2, '53X', 'About Money', 'About Money', 'album.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

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