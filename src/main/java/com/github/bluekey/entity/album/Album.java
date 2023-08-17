package com.github.bluekey.entity.album;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name="album")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Album {

	private static final int ARTIST_ID_THRESHOLD = 0;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "album_id")
	private Long id;

	private Long artistId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String enName;

	private String profileImage;

	@Builder(builderClassName = "ByAlbumWithOutRepresentativeArtistBuilder", builderMethodName = "ByAlbumWithOutRepresentativeArtistBuilder")
	public Album(String name, String enName, String profileImage) {
		this.name = name;
		this.enName = enName;
		this.profileImage = profileImage;
	}

	@Builder(builderClassName = "ByAlbumWithRepresentativeArtistBuilder", builderMethodName = "ByAlbumWithRepresentativeArtistBuilder")
	public Album(Long artistId, String name, String enName, String profileImage) {
		this.artistId = artistId;
		this.name = name;
		this.enName = enName;
		this.profileImage = profileImage;
	}

	public boolean isSameKoNameWithEnName() {
		return name.equals(enName);
	}

	public boolean hasRepresentativeArtist() {
		return artistId != null;
	}

	public void updateProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public void updateName(String name) {
		this.name = name;
	}

	public void updateEnName(String enName) {
		this.enName = enName;
	}

	public void updateArtistId(Long artistId) {
		if (artistId <= ARTIST_ID_THRESHOLD) {
			throw new IllegalArgumentException("Invalid artistId format.");
		}
		this.artistId = artistId;
	}
}
