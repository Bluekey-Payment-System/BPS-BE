package com.github.bluekey.repository.track;

import com.github.bluekey.entity.track.Track;
import com.github.bluekey.entity.track.TrackMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrackMemberRepository extends JpaRepository<TrackMember, Long> {
    Optional<TrackMember> findTrackMemberByNameAndTrack(String name, Track track);
    Optional<TrackMember> findTrackMemberByNameAndTrackAndIsRemoved(String name, Track track, boolean isRemoved);

    List<TrackMember> findTrackMembersByTrack(Track track);

    List<TrackMember> findAllByMemberIdAndIsRemovedFalse(Long memberId);
}
