package com.github.bluekey.entity.track;

import com.github.bluekey.entity.album.Album;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name="track")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Track {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "track_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "album_id")
	private Album album;

	@OneToMany(mappedBy = "track", cascade = CascadeType.ALL)
	private List<TrackMember> trackMembers = new ArrayList<>();

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String enName;

	@Column(nullable = false)
	private boolean isOriginalTrack;

	@Builder
	public Track(Album album, String name, String enName, boolean isOriginalTrack) {
		this.album = album;
		this.name = name;
		this.enName = enName;
		this.isOriginalTrack = isOriginalTrack;
	}

	public boolean isSameKoNameWithEnName() {
		return name.equals(enName);
	}

	public void updateName(String name) {
		this.name = name;
	}

	public void updateEnName(String enName) {
		this.enName = enName;
	}

	public void updateIsOriginalTrack(boolean isOriginalTrack) {
		this.isOriginalTrack = isOriginalTrack;
	}
}
