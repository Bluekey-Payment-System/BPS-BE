package com.github.bluekey.processor;

import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.track.TrackMemberRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
@RequiredArgsConstructor
public class ExcelFileDBMigrationProcessManager implements ProcessManager{
    private final MemberRepository memberRepository;
    private final TrackMemberRepository trackMemberRepository;

    private List<Workbook> workbooks = new ArrayList<>();

    @Override
    public void process() {

    }

    public void updateWorkbooks(List<Workbook> workbooks) {
        this.workbooks = workbooks;
    }
}
