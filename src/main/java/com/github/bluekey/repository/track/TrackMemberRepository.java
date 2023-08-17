package com.github.bluekey.repository.track;

import com.github.bluekey.entity.track.TrackMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackMemberRepository extends JpaRepository<TrackMember, Long> {
}
