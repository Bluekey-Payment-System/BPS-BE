package com.github.bluekey.repository.album;

import com.github.bluekey.entity.album.Album;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    Optional<Album> findAlbumByEnNameIgnoreCase(String name);

    Optional<Album> findAlbumByNameIgnoreCase(String name);
    Optional<Album> findAlbumByNameIgnoreCaseOrEnNameIgnoreCase(String name, String enName);

    Optional<Album> findAlbumByIdAndIsRemovedFalse(Long id);

    Page<Album> findAllByIsRemovedFalseOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT a " +
            "FROM Album a " +
            "WHERE a.isRemoved = false " +
            "AND (a.name LIKE %:keyword% OR a.enName LIKE %:keyword%) " +
            "ORDER BY a.createdAt DESC")
    Page<Album> findAllByIsRemovedFalseAndSearchByKeyword(Pageable pageable, @Param("keyword") String keyword);

    default Album findAlbumByIdAndIsRemovedFalseOrElseThrow(Long id) {
        return this.findAlbumByIdAndIsRemovedFalse(id).orElseThrow(() ->
                new BusinessException(ErrorCode.ALBUM_NOT_FOUND)
        );
    }
}
