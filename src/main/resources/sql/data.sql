-- Artist Member
INSERT INTO member (value, login_id, password, name, en_name, commission_rate, type, role, profile_image)
VALUES ('artist1@example.com', 'artist1', 'password123!', '혁기', 'hucki', 10, 'USER', 'ARTIST', 'artist.jpg');

INSERT INTO member (value, login_id, password, name, en_name, commission_rate, type, role, profile_image)
VALUES ('artist2@example.com', 'artist2', 'password123!', '53X', '53X', 20, 'USER', 'ARTIST', 'artist.jpg');

-- Admin Member
INSERT INTO member (value, login_id, password, name, role, type, profile_image)
VALUES ('admin@example.com', 'admin123', 'password123!', 'bluekey', 'ADMIN', 'SUPER_ADMIN', 'admin.jpg');

-- Album
-- 1
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image)
VALUES (1, '혁기', '사랑에도 공식이 있나요 OST PART 2', '사랑에도 공식이 있나요 OST PART 2', 'album.jpg');

-- 2
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image)
VALUES (1, '혁기', 'COME TO ME', 'COME TO ME', 'album.jpg');

-- 3
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image)
VALUES (1, '혁기', 'Underwater', 'Underwater', 'album.jpg');

-- 4
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image)
VALUES (1, '혁기', '프라하', 'Praha', 'album.jpg');

-- 5
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image)
VALUES (1, '혁기', 'Is there an equation in Love? Pt. 2 (Original Motion Picture Soundtrack)', 'Is there an equation in Love? Pt. 2 (Original Motion Picture Soundtrack)', 'album.jpg');

-- 6
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image)
VALUES (2, '53X', 'C&C', 'C&C', 'album.jpg');

-- 7
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image)
VALUES (2, '53X', 'About Money', 'About Money', 'album.jpg');

-- Track
-- 1
INSERT INTO track (album_id, name, en_name, is_original_track)
VALUES (1, '그대 안아줄게요', '그대 안아줄게요', false);

-- 2
INSERT INTO track (album_id, name, en_name, is_original_track)
VALUES (2, 'Praha', 'Praha', false);

-- 3
INSERT INTO track (album_id, name, en_name, is_original_track)
VALUES (2, 'Wave love', 'Wave love', false);

--4
INSERT INTO track (album_id, name, en_name, is_original_track)
VALUES (3, 'Underwater', 'Underwater', false);

-- 5
INSERT INTO track (album_id, name, en_name, is_original_track)
VALUES (3, 'Hold me like the ocean', 'Hold me like the ocean', false);

-- 6
INSERT INTO track (album_id, name, en_name, is_original_track)
VALUES (6, 'C&C', 'C&C (feat. Xio)', false);

-- 7
INSERT INTO track (album_id, name, en_name, is_original_track)
VALUES (7, '돈', 'About Money (feat. BRVR, Daily Wave, Juel)', false);

-- 8
INSERT INTO track (album_id, name, en_name, is_original_track)
VALUES (2, 'Burn', 'Burn', false);

-- 9
INSERT INTO track (album_id, name, en_name, is_original_track)
VALUES (2, 'Headache', 'Headache', false);

-- 10
INSERT INTO track (album_id, name, en_name, is_original_track)
VALUES (4, 'Praha (inst.)', 'Praha (inst.)', false);

-- 11
INSERT INTO track (album_id, name, en_name, is_original_track)
VALUES (1, '내 마음 모르죠', 'mind', false);

-- 12
INSERT INTO track (album_id, name, en_name, is_original_track)
VALUES (1, '그대 안아줄게요 (Acoustic ver.)', '그대 안아줄게요 (Acoustic ver.)', false);

-- 13
INSERT INTO track (album_id, name, en_name, is_original_track)
VALUES (5, 'You make me hug', 'You make me hug', false);

-- 14
INSERT INTO track (album_id, name, en_name, is_original_track)
VALUES (5, 'Can you read my heart', 'Can you read my heart', false);

-- 15
INSERT INTO track (album_id, name, en_name, is_original_track)
VALUES (4, 'Praha', 'Praha', false);

-- 16
INSERT INTO track (album_id, name, en_name, is_original_track)
VALUES (1, '내 마음 모르죠 (Acoustic ver.)', '내 마음 모르죠 (Acoustic ver.)', false);

-- 17
INSERT INTO track (album_id, name, en_name, is_original_track)
VALUES (2, 'Come to me', 'Come to me', false);

-- 18
INSERT INTO track (album_id, name, en_name, is_original_track)
VALUES (2, 'Come to me (inst.)', 'Come to me (inst.)', false);


-- TrackMember
INSERT INTO track_member (track_id, member_id, name, commission_rate)
VALUES (1, 1, '혁기', 50);

INSERT INTO track_member (track_id, member_id, name, commission_rate)
VALUES (1, 2, '53X', 30);

INSERT INTO track_member (track_id, member_id, name, commission_rate)
VALUES (11, null, '김여름', 0);