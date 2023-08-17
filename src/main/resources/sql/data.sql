-- Artist Member
INSERT INTO member (value, login_id, password, name, en_name, commission_rate, type, role, profile_image)
VALUES ('artist1@example.com', 'artist1', 'password123!', '혁기', 'hucki', 10, 'USER', 'ARTIST', 'artist.jpg');

INSERT INTO member (value, login_id, password, name, en_name, commission_rate, type, role, profile_image)
VALUES ('artist2@example.com', 'artist2', 'password123!', '53X', '53X', 20, 'USER', 'ARTIST', 'artist.jpg');

-- Admin Member
INSERT INTO member (value, login_id, password, name, role, type, profile_image)
VALUES ('admin@example.com', 'admin123', 'password123!', 'bluekey', 'ADMIN', 'SUPER_ADMIN', 'admin.jpg');

-- Album
INSERT INTO album (artist_id, artist_name, name, en_name, profile_image)
VALUES (1, '혁기', '사랑에도 공식이 있나요 OST PART 2', '사랑에도 공식이 있나요 OST PART 2', 'album.jpg');

INSERT INTO album (artist_id, artist_name, name, en_name, profile_image)
VALUES (1, '혁기', 'COME TO ME', 'COME TO ME', 'album.jpg');

INSERT INTO album (artist_id, artist_name, name, en_name, profile_image)
VALUES (null, null, '봄', 'Spring', 'album.jpg');

-- Track
INSERT INTO track (album_id, name, en_name, is_original_track)
VALUES (1, '그대 안아줄게요', '그대 안아줄게요', false);

INSERT INTO track (album_id, name, en_name, is_original_track)
VALUES (2, 'Praha', 'Praha', false);

INSERT INTO track (album_id, name, en_name, is_original_track)
VALUES (3, '내 마음 모르죠', 'mind', false);

-- TrackMember
INSERT INTO track_member (track_id, member_id, name, commission_rate)
VALUES (1, 1, '혁기', 50);

INSERT INTO track_member (track_id, member_id, name, commission_rate)
VALUES (1, 2, '53X', 30);

INSERT INTO track_member (track_id, member_id, name, commission_rate)
VALUES (3, null, '김여름', 0);