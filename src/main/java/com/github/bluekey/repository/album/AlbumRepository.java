package com.github.bluekey.repository.album;

import com.github.bluekey.entity.album.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {
}
