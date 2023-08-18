package com.github.bluekey.processor.validator;

import com.github.bluekey.repository.album.AlbumRepository;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.track.TrackMemberRepository;
import com.github.bluekey.repository.track.TrackRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DBPersistenceValidator {
    private final MemberRepository memberRepository;
    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;
    private final TrackMemberRepository trackMemberRepository;

}
