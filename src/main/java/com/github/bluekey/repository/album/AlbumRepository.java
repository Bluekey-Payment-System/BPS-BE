package com.github.bluekey.repository.album;

import com.github.bluekey.entity.album.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    Optional<Album> findAlbumByName(String name);
    Optional<Album> findAlbumByEnName(String enName);
}
