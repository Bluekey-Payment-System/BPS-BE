package com.github.bluekey.processor.validator;

import com.github.bluekey.processor.type.AtoExcelColumnType;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AtoDistributorCellValidator implements ExcelValidator<AtoExcelColumnType> {

    @Override
    public boolean hasCellNullValue(AtoExcelColumnType columnType, Cell cell) {
        CellType cellType = cell.getCellType();

        if (cellType.equals(CellType.STRING)) {
            return cell.getStringCellValue() == null;
        }

        if(cellType.equals(CellType.BLANK)) {
            return cell.getStringCellValue() == null;
        }

        return false;
    }

}
