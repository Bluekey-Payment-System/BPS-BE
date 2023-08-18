package com.github.bluekey.processor.validator;

import com.github.bluekey.entity.album.Album;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.track.Track;
import com.github.bluekey.entity.track.TrackMember;
import com.github.bluekey.processor.NameExtractor;
import com.github.bluekey.repository.album.AlbumRepository;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.track.TrackMemberRepository;
import com.github.bluekey.repository.track.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DBPersistenceValidator {
    private final MemberRepository memberRepository;
    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;
    private final TrackMemberRepository trackMemberRepository;

    public boolean hasNotExistedArtist(Cell cell) {
        String artistName = cell.getStringCellValue();
        List<String> artistExtractedNames = NameExtractor.getExtractedNames(artistName);

        for (String artistExtractedName : artistExtractedNames) {
            Optional<Member> memberFindByEnName = memberRepository.findMemberByEnName(artistExtractedName);
            if (memberFindByEnName.isPresent()) {
                return false;
            }
            Optional<Member> memberFindByKoName = memberRepository.findMemberByName(artistExtractedName);
            if (memberFindByKoName.isPresent()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasNotExistedAlbum(Cell cell) {
        String albumName = cell.getStringCellValue();
        List<String> albumExtractedNames = NameExtractor.getExtractedNames(albumName);

        for (String artistExtractedName : albumExtractedNames) {
            Optional<Album> albumFindByEnName = albumRepository.findAlbumByEnName(artistExtractedName);
            if (albumFindByEnName.isPresent()) {
                return false;
            }
            Optional<Album> albumFindByKoName = albumRepository.findAlbumByName(artistExtractedName);
            if (albumFindByKoName.isPresent()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasNotExistedTrackMember(Cell cellTrackMember, Cell cellTrack) {
        String artistName = cellTrackMember.getStringCellValue();
        List<String> artistExtractedNames = NameExtractor.getExtractedNames(artistName);
        for (String artistExtractedName : artistExtractedNames) {
            Optional<TrackMember> trackMemberFindByEnName = trackMemberRepository.findTrackMemberByName(artistExtractedName);
            if (trackMemberFindByEnName.isPresent()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasNotExistedTrack(Cell cell, Cell cellAlbum) {
        String trackName = cell.getStringCellValue();
        String albumName = cellAlbum.getStringCellValue();
        List<String> trackExtractedNames = NameExtractor.getExtractedNames(trackName);
        List<String> albumExtractedNames = NameExtractor.getExtractedNames(albumName);

        for(String trackExtractedName : trackExtractedNames) {
            for (String albumExtractedName : albumExtractedNames) {
                Optional<Album> albumFindByEnName = albumRepository.findAlbumByEnName(albumExtractedName);
                if (albumFindByEnName.isPresent()) {
                    List<Track> trackFindByEnName = trackRepository.findTracksByEnNameAndAlbum(trackExtractedName, albumFindByEnName.get());
                    if (trackFindByEnName.size() > 0) {
                        return false;
                    }
                }

                Optional<Album> albumFindByName = albumRepository.findAlbumByName(albumExtractedName);
                if (albumFindByName.isPresent()) {
                    List<Track> trackFindByKoName = trackRepository.findTracksByNameAndAlbum(trackExtractedName, albumFindByName.get());
                    if (trackFindByKoName.size() > 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
