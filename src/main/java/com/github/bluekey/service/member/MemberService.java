package com.github.bluekey.service.member;

import com.github.bluekey.dto.admin.AdminAccountDto;
import com.github.bluekey.dto.admin.AdminArtistProfileListDto;
import com.github.bluekey.dto.admin.AdminProfileUpdateDto;
import com.github.bluekey.dto.admin.AdminProfileViewDto;
import com.github.bluekey.dto.artist.ArtistAccountDto;
import com.github.bluekey.dto.artist.ArtistProfileDto;
import com.github.bluekey.dto.artist.ArtistProfileViewDto;
import com.github.bluekey.dto.request.admin.AdminArtistProfileRequestDto;
import com.github.bluekey.dto.request.artist.ArtistProfileRequestDto;
import com.github.bluekey.dto.response.admin.AdminAccountsResponseDto;
import com.github.bluekey.dto.response.admin.AdminArtistProfileListReponseDto;
import com.github.bluekey.dto.response.admin.AdminProfileResponseDto;
import com.github.bluekey.dto.response.artist.ArtistAccountsResponseDto;
import com.github.bluekey.dto.response.artist.ArtistProfileResponseDto;
import com.github.bluekey.dto.response.artist.SimpleArtistAccountListResponseDto;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.entity.member.MemberType;
import com.github.bluekey.entity.track.Track;
import com.github.bluekey.entity.track.TrackMember;
import com.github.bluekey.entity.transaction.Transaction;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.exception.member.MemberNotFoundException;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.track.TrackMemberRepository;
import com.github.bluekey.repository.transaction.TransactionRepository;
import com.github.bluekey.util.ImageUploadUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;
    private final ImageUploadUtil imageUploadUtil;

    @Transactional
    public ArtistProfileResponseDto updateArtistProfile(ArtistProfileRequestDto dto, MultipartFile file, Long memberId) {
        Member member = memberRepository.findByIdOrElseThrow(memberId);
        updateArtistEmail(dto.getEmail(), member);
        updateProfileImages(file, member);
        memberRepository.save(member);
        return ArtistProfileResponseDto.from(member);
    }

    @Transactional
    public ArtistAccountDto updateArtistProfileByAdmin(AdminArtistProfileRequestDto dto, Long memberId) {
        Member member = memberRepository.findByIdOrElseThrow(memberId);
        updateArtistName(dto, member);
        updateArtistCommissionRate(dto, member);
        memberRepository.save(member);
        return ArtistAccountDto.from(member);
    }

    @Transactional
    public AdminProfileResponseDto updateAdminProfile(AdminProfileUpdateDto dto, MultipartFile file, Long memberId) {
        Member member = memberRepository.findByIdOrElseThrow(memberId);
        updateAdminEmail(dto.getEmail(), member);
        updateAdminNickname(dto.getNickname(), member);
        updateProfileImages(file, member);
        memberRepository.save(member);
        return AdminProfileResponseDto.from(member);
    }

    @Transactional(readOnly = true)
    public AdminAccountsResponseDto getAdminAccounts(PageRequest pageable) {
        Page<Member> admins = memberRepository.findMembersByType(MemberType.ADMIN, pageable);
        return AdminAccountsResponseDto.builder()
                .totalItems(admins.getTotalElements())
                .contents(admins.getContent().stream().map(AdminAccountDto::from).collect(Collectors.toList()))
                .build();
    }

    @Transactional(readOnly = true)
    public ArtistAccountsResponseDto getArtistAccounts(PageRequest pageable) {

        Page<Member> artists = memberRepository.findMembersByRole(MemberRole.ARTIST, pageable);
        return ArtistAccountsResponseDto.builder()
                .totalItems(artists.getTotalElements())
                .contents(artists.getContent().stream().map(ArtistAccountDto::from).collect(Collectors.toList()))
                .build();
    }

    @Transactional(readOnly = true)
    public SimpleArtistAccountListResponseDto getSimpleArtistAccounts() {
        List<Member> artists = memberRepository.findMemberByRoleAndIsRemovedFalse(MemberRole.ARTIST);
        return SimpleArtistAccountListResponseDto.from(artists);
    }

    @Transactional(readOnly = true)
    public ArtistProfileViewDto getArtistProfile(Long memberId) {
        Member member = memberRepository.findByIdOrElseThrow(memberId);
        return ArtistProfileViewDto.from(member);
    }

    @Transactional(readOnly = true)
    public AdminProfileViewDto getAdminProfile(Long memberId) {
        Member member = memberRepository.findByIdOrElseThrow(memberId);
        return AdminProfileViewDto.from(member);
    }

    public void permissionCheck(Long memberId, Long loginMemberId) {
        Member member = memberRepository.findByIdOrElseThrow(memberId);
        if (member.getRole() == MemberRole.ARTIST && !memberId.equals(loginMemberId)) {
            throw new BusinessException(ErrorCode.AUTHENTICATION_FAILED);
        }
    }

    public void validateAdminEmail(String email) {
        memberRepository.findMemberByEmailAndType(email, MemberType.ADMIN)
                .ifPresent(member -> {
                    throw new BusinessException(ErrorCode.INVALID_EMAIL_VALUE);
                });
    }

    public void validateAdminNickname(String nickname) {
        // 아티스트의 활동 예명을 닉네임으로 사용할 수 없다.
        if (memberRepository.findMemberByNameAndType(nickname, MemberType.USER).isPresent() ||
                memberRepository.findMemberByEnNameAndType(nickname, MemberType.USER).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATE_ARTIST_NAME);
        }
    }

    private void updateArtistName(AdminArtistProfileRequestDto dto, Member member) {
        if (dto.getName() != null) {
            member.updateName(dto.getName());
        }
        if (dto.getEnName() != null) {
            member.updateEnName(dto.getEnName());
        }
    }

    private void updateArtistCommissionRate(AdminArtistProfileRequestDto dto, Member member) {
        if (dto.getCommissionRate() != null) {
            member.updateCommissionRate(Integer.valueOf(dto.getCommissionRate()));
        }
    }

    private void updateProfileImages(MultipartFile file, Member member) {
        if (hasNotProfileImage(file)) {
            return;
        }
        if (member.getProfileImage() != null) {
            imageUploadUtil.deleteImage(member.getProfileImage());
        }
        String profileImage = imageUploadUtil.uploadImage(file,
                imageUploadUtil.getProfileImageKey(file.getOriginalFilename(), member.getId()));
        member.updateProfileImage(profileImage);
    }

    private boolean hasNotProfileImage(MultipartFile file) {
        return file == null || file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty();
    }

    private void updateArtistEmail(String email, Member member) {
        if (email == null) {
            return;
        }
        memberRepository.findMemberByEmailAndType(email, MemberType.USER)
                .ifPresent(m -> {
                    throw new BusinessException(ErrorCode.INVALID_EMAIL_VALUE);
                });
        member.updateEmail(email);
    }

    private void updateAdminEmail(String email, Member member) {
        if (email != null) {
            validateAdminEmail(email);
            member.updateEmail(email);
        }
    }

    private void updateAdminNickname(String nickname, Member member) {
        if (nickname != null) {
            validateAdminNickname(nickname);
            member.updateName(nickname);
        }
    }

    public AdminArtistProfileListReponseDto getArtistsPagination(Pageable pageable, String monthly, String keyword) {
        List<Transaction> transactions = transactionRepository.findTransactionsByDuration(monthly);
        List<Transaction> previousMonthTransactions = transactionRepository.findTransactionsByDuration(getPreviousMonth(monthly));
        List<Member> artists;
        if (keyword != null) {
            artists = memberRepository.findMembersByRoleAndIsRemovedFalseAndNameContainingIgnoreCaseOrEnNameContainingIgnoreCase(MemberRole.ARTIST, keyword, keyword);
        } else {
            artists = memberRepository.findMemberByRoleAndIsRemovedFalse(MemberRole.ARTIST);
        }
        List<AdminArtistProfileListDto> adminArtistProfiles = new ArrayList<>();

        for (Member artist : artists) {
            ArtistProfileDto artistProfileDto = ArtistProfileDto.builder()
                    .memberId(artist.getId())
                    .name(artist.getName())
                    .enName(artist.getEnName())
                    .profileImage(artist.getProfileImage())
                    .build();

            Map<Long, Double> artistMappedByAmount = transactions.stream()
                    .filter(transaction -> transaction.getTrackMember().getMemberId() != null)
//					.filter(transaction -> filterSearchKeyword(keyword, artist))
                    .collect(Collectors.groupingBy(
                            transaction -> transaction.getTrackMember().getMemberId(),
                            Collectors.summingDouble(Transaction::getAmount)
                    ));

            Map<Long, Double> sortedArtistMappedByAmount = artistMappedByAmount.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));

            Map<Long, Double> artistPreviousMonthMappedByAmount = previousMonthTransactions.stream()
                    .filter(transaction -> transaction.getTrackMember().getMemberId() != null)
//					.filter(transaction -> filterSearchKeyword(keyword, artist))
                    .collect(Collectors.groupingBy(
                            transaction -> transaction.getTrackMember().getMemberId(),
                            Collectors.summingDouble(Transaction::getAmount)
                    ));

            Map<Long, Double> sortedPreviousMonthArtistMappedByAmount = artistPreviousMonthMappedByAmount.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));

            Map<TrackMember, Double> trackMemberMappedByAmount = transactions.stream()
                    .filter(transaction -> transaction.getTrackMember().getMemberId() != null)
//					.filter(transaction -> filterSearchKeyword(keyword, artist))
                    .collect(Collectors.groupingBy(
                            Transaction::getTrackMember,
                            Collectors.summingDouble(Transaction::getAmount)
                    ));

            Map<TrackMember, Double> sortedTrackMemberMappedByAmount = trackMemberMappedByAmount.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));

            Map<Track, Double> trackMappedByAmount = transactions.stream()
                    .filter(transaction -> transaction.getTrackMember().getMemberId() != null)
//					.filter(transaction -> filterSearchKeyword(keyword, artist))
                    .collect(Collectors.groupingBy(
                            transaction -> transaction.getTrackMember().getTrack(),
                            Collectors.summingDouble(Transaction::getAmount)
                    ));

            Map<Track, Double> sortedTrackMappedByAmount = trackMappedByAmount.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));

            double netIncome = 0.0;
            double representativeTrackAmount = 0.0;
            String representativeTrackName = "";

            for (Map.Entry<TrackMember, Double> entry : sortedTrackMemberMappedByAmount.entrySet()) {
                TrackMember trackMember = entry.getKey();
                Double amount = entry.getValue();
                if (trackMember.getMemberId().equals(artist.getId())) {
                    int commissionRate = trackMember.getCommissionRate();
                    netIncome += amount * commissionRate / 100;

                    Double trackAmount = sortedTrackMappedByAmount.get(trackMember.getTrack());
                    if (representativeTrackAmount < trackAmount) {
                        representativeTrackName = trackMember.getTrack().getName();
                        representativeTrackAmount = trackAmount;
                    }
                }
            }


            Double amount;
            amount = sortedArtistMappedByAmount.get(artist.getId());
            if (amount == null) {
                amount = 0.0;
            }
            Double previousMonthAmount = 0.0;
            previousMonthAmount = sortedPreviousMonthArtistMappedByAmount.get(artist.getId());
            if (previousMonthAmount == null) {
                previousMonthAmount = 0.0;
            }

            AdminArtistProfileListDto artistProfileListDto = AdminArtistProfileListDto.builder()
                    .artist(artistProfileDto)
                    .revenue((int) Math.floor(amount))
                    .netIncome((int) Math.floor(netIncome))
                    .settlementAmount((int) Math.floor(netIncome - (netIncome * 33) / 1000))
                    .representativeTrack(representativeTrackName)
                    .monthlyIncreaseRate(getGrowthRate(previousMonthAmount, amount))
                    .build();

            adminArtistProfiles.add(artistProfileListDto);
        }
        return AdminArtistProfileListReponseDto.builder()
                .totalItems(adminArtistProfiles.size())
                .contents(getPage(adminArtistProfiles, pageable.getPageNumber(), pageable.getPageSize()))
                .build();
    }

    private Double getGrowthRate(Double previousMonthAmount, double amount) {
        if (previousMonthAmount == null || amount == 0.0) {
            return null;
        }

        if (previousMonthAmount == 0.0) {
            return null;
        }

        double percentage = (amount - previousMonthAmount) / previousMonthAmount * 100;
        if (0 < percentage && percentage < 10) {
            return Math.floor(percentage * 10) / 10;
        }
        return Math.floor(percentage);
    }

    private String getPreviousMonth(String monthly) {
        LocalDate date = LocalDate.parse(monthly + "01", DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate previousMonth = date.minusMonths(1);
        return previousMonth.format(DateTimeFormatter.ofPattern("yyyyMM"));
    }

    public static <T> List<T> getPage(List<T> sourceList, int page, int pageSize) {
        if (pageSize <= 0) {
            throw new BusinessException(ErrorCode.PAGINATION_INVALID_INDEX);
        }

        int fromIndex = (page) * pageSize;
        if (sourceList == null || sourceList.size() <= fromIndex) {
            return Collections.emptyList();
        }

        return sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size()));
    }

    @Deprecated
    private Boolean filterSearchKeyword(String keyword, Member artist) {
        if (keyword == null) {
            return true;
        }

        String convertedKeyword = keyword.toLowerCase();
        ;
        String artistName = artist.getName().toLowerCase();
        String artistEnName = artist.getEnName().toLowerCase();

        return artistName.contains(convertedKeyword) || artistEnName.contains(convertedKeyword);
    }
}
