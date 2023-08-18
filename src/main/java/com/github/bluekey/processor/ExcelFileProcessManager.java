package com.github.bluekey.processor;

import com.github.bluekey.processor.provider.AtoDistributorExcelFileProvider;
import com.github.bluekey.processor.provider.ExcelFileProvider;
import com.github.bluekey.processor.type.MusicDistributorType;
import com.github.bluekey.processor.validator.DBPersistenceValidator;
import com.github.bluekey.repository.album.AlbumRepository;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.track.TrackMemberRepository;
import com.github.bluekey.repository.track.TrackRepository;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class ExcelFileProcessManager {
    private static final String FILE_SEPARATOR = "\\.";
    private final MultipartFile file;
    private final ExcelFileProvider excelFileProvider;
    private String filetype;
    private String fileName;
    private final MemberRepository memberRepository;
    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;
    private final TrackMemberRepository trackMemberRepository;
    private final DBPersistenceValidator dbPersistenceValidator;

    public ExcelFileProcessManager(
            MultipartFile file,
            MemberRepository memberRepository,
            AlbumRepository albumRepository,
            TrackRepository trackRepository,
            TrackMemberRepository trackMemberRepository
    ) {
        setExcelFileBasicInformation(file);
        this.file = file;
        this.memberRepository = memberRepository;
        this.albumRepository = albumRepository;
        this.trackRepository = trackRepository;
        this.trackMemberRepository = trackMemberRepository;
        this.excelFileProvider = setProvider();
        this.dbPersistenceValidator = new DBPersistenceValidator(memberRepository, albumRepository, trackRepository, trackMemberRepository);
    }

    public void process() {
        excelFileProvider.process(excelFileProvider.getActiveSheet());
    }

    public List<ExcelRowException> getErrors() {
        return excelFileProvider.getErrors();
    }

    public List<ExcelRowException> getWarnings() {return excelFileProvider.getWarnings();}

    private void setExcelFileBasicInformation(MultipartFile file) {
        String[] originalFileNameInformation = file.getOriginalFilename().split(FILE_SEPARATOR);
        this.fileName = originalFileNameInformation[0];
        this.filetype = originalFileNameInformation[1];
    }

    private ExcelFileProvider setProvider() {
        for (MusicDistributorType type : MusicDistributorType.values()) {
            Pattern pattern = Pattern.compile(type.getPattern());
            Matcher matcher = pattern.matcher(fileName);
            if (matcher.matches()) {
                return determineExcelFileProviderWithMusicDistributorType(type);
            }
        }
        throw new IllegalArgumentException("Unsupported file");
    }

    private ExcelFileProvider determineExcelFileProviderWithMusicDistributorType(MusicDistributorType type) {
        if (type.getCls().equals(AtoDistributorExcelFileProvider.class)) {
            return new AtoDistributorExcelFileProvider(file, dbPersistenceValidator);
        }
        throw new IllegalArgumentException("Excel File Provider exception");
    }
}
