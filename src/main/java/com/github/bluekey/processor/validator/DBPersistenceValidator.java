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
import org.apache.poi.ss.usermodel.DataFormatter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
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
            Optional<Member> memberFindByEnName = memberRepository.findMemberByEnNameAndIsRemoved(artistExtractedName, false);
            if (memberFindByEnName.isPresent()) {
                return false;
            }
            Optional<Member> memberFindByKoName = memberRepository.findMemberByNameAndIsRemoved(artistExtractedName, false);
            if (memberFindByKoName.isPresent()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasNotExistArtistWithExcelName(String artistName) {
        Optional<Member> memberFindByEnName = memberRepository.findMemberByEnNameAndIsRemoved(artistName, false);
        if (memberFindByEnName.isPresent()) {
            return false;
        }
        Optional<Member> memberFindByKoName = memberRepository.findMemberByNameAndIsRemoved(artistName, false);
        if (memberFindByKoName.isPresent()) {
            return false;
        }
        return true;
    }

    public boolean hasNotExistedAlbum(Cell cell) {
        DataFormatter dataFormatter = new DataFormatter();
        String albumName = dataFormatter.formatCellValue(cell);

        Optional<Album> albumFindByEnName = albumRepository.findAlbumByEnNameIgnoreCaseAndIsRemoved(albumName, false);
        if (albumFindByEnName.isPresent()) {
            return false;
        }
        Optional<Album> albumFindByKoName = albumRepository.findAlbumByNameIgnoreCaseAndIsRemoved(albumName, false);
        if (albumFindByKoName.isPresent()) {
            return false;
        }
        return true;
    }

    public boolean hasNotExistedTrackMember(Cell cellTrackMember, Cell cellTrack) {
        DataFormatter dataFormatter = new DataFormatter();
        String artistName = cellTrackMember.getStringCellValue();
        String trackName = dataFormatter.formatCellValue(cellTrack);

        List<String> artistExtractedNames = NameExtractor.getExtractedNames(artistName);

        for (String artistExtractedName : artistExtractedNames) {

            Optional<Track> trackFindByEnName = trackRepository.findTrackByEnNameIgnoreCase(trackName);
            if(trackFindByEnName.isPresent()) {
                Optional<TrackMember> trackMemberFindByEnName = trackMemberRepository.findTrackMemberByNameAndTrackAndIsRemoved(artistExtractedName, trackFindByEnName.get(), false);
                if (trackMemberFindByEnName.isPresent()) {
                    return false;
                }
            }
            Optional<Track> trackFindByName = trackRepository.findTrackByNameIgnoreCase(trackName);
            if(trackFindByName.isPresent()) {
                Optional<TrackMember> trackMemberFindByEnName = trackMemberRepository.findTrackMemberByNameAndTrackAndIsRemoved(artistExtractedName, trackFindByName.get(), false);
                if (trackMemberFindByEnName.isPresent()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hasDuplicatedTrack(Cell cell) {
        DataFormatter dataFormatter = new DataFormatter();
        String trackName = dataFormatter.formatCellValue(cell);

        List<Track> trackByName = trackRepository.findAllByNameAndIsRemoved(trackName, false);
//        List<Track> trackByEnName = trackRepository.findAllByEnNameAndIsRemoved(trackName, false);

        return trackByName.size() > 1;
    }

    public boolean hasNotExistedTrack(Cell cell, Cell cellAlbum) {
        DataFormatter dataFormatter = new DataFormatter();
        String trackName = cell.getStringCellValue();
        String albumName = dataFormatter.formatCellValue(cellAlbum);

        Optional<Album> albumFindByEnName = albumRepository.findAlbumByEnNameIgnoreCaseAndIsRemoved(albumName, false);
        if (albumFindByEnName.isPresent()) {
            Optional<Track> trackFindByEnName = trackRepository.findTrackByEnNameIgnoreCaseAndAlbumAndIsRemoved(trackName, albumFindByEnName.get(), false);
            if(trackFindByEnName.isPresent()) {
                return false;
            }

            Optional<Track> trackFindByName = trackRepository.findTrackByNameIgnoreCaseAndAlbumAndIsRemoved(trackName, albumFindByEnName.get(), false);
            if(trackFindByName.isPresent()) {
                return false;
            }
        }

        Optional<Album> albumFindByName = albumRepository.findAlbumByNameIgnoreCaseAndIsRemoved(albumName, false);
        if (albumFindByName.isPresent()) {
            Optional<Track> trackFindByEnName = trackRepository.findTrackByEnNameIgnoreCaseAndAlbumAndIsRemoved(trackName, albumFindByEnName.get(), false);
            if(trackFindByEnName.isPresent()) {
                return false;
            }

            Optional<Track> trackFindByName = trackRepository.findTrackByNameIgnoreCaseAndAlbumAndIsRemoved(trackName, albumFindByEnName.get(), false);
            if(trackFindByName.isPresent()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasNotExistedTrackInMafia(Cell cell, String albumName) {
        String trackName = cell.getStringCellValue();

        Optional<Album> albumFindByEnName = albumRepository.findAlbumByEnNameIgnoreCase(albumName);
        if (albumFindByEnName.isPresent()) {
            Optional<Track> trackFindByEnName = trackRepository.findTrackByEnNameIgnoreCaseAndAlbum(trackName, albumFindByEnName.get());
            if(trackFindByEnName.isPresent()) {
                return false;
            }

            Optional<Track> trackFindByName = trackRepository.findTrackByNameIgnoreCaseAndAlbum(trackName, albumFindByEnName.get());
            if(trackFindByName.isPresent()) {
                return false;
            }
        }

        Optional<Album> albumFindByName = albumRepository.findAlbumByNameIgnoreCase(albumName);
        if (albumFindByName.isPresent()) {
            Optional<Track> trackFindByEnName = trackRepository.findTrackByEnNameIgnoreCaseAndAlbum(trackName, albumFindByEnName.get());
            if(trackFindByEnName.isPresent()) {
                return false;
            }

            Optional<Track> trackFindByName = trackRepository.findTrackByNameIgnoreCaseAndAlbum(trackName, albumFindByEnName.get());
            if(trackFindByName.isPresent()) {
                return false;
            }
        }
        return true;
    }
}
