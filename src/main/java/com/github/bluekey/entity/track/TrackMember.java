package com.github.bluekey.entity.track;

import com.github.bluekey.entity.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name="track_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrackMember {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "track_member_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "track_id")
	private Track track;

	private Long memberId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private int commissionRate;

	@Builder(builderClassName = "ByArtistBuilder", builderMethodName = "ByArtistBuilder")
	public TrackMember(Track track, Member member, int commissionRate) {
		this.track = track;
		this.memberId = member.getId();
		this.name = member.getName();
		this.commissionRate = commissionRate;
	}

	@Builder(builderClassName = "ByContractSingerBuilder", builderMethodName = "ByContractSingerBuilder")
	public TrackMember(Track track, String name) {
		this.track = track;
		this.name = name;
		this.commissionRate = 0;
	}

	public boolean isArtistTrack() {
		return this.memberId != null;
	}
}
