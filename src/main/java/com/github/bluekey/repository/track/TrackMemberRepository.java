package com.github.bluekey.repository.track;

import com.github.bluekey.entity.track.TrackMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrackMemberRepository extends JpaRepository<TrackMember, Long> {
    Optional<TrackMember> findTrackMemberByName(String name);
}
