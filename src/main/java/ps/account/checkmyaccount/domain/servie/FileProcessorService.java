package ps.account.checkmyaccount.domain.servie;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ps.account.checkmyaccount.application.dto.request.RequestDto;
import ps.account.checkmyaccount.domain.dto.TransactionDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class FileProcessorService {
    @Autowired
    private InterestDateService interestDateService;

    public void validateExcelFile(XSSFSheet sheet, RequestDto dto){

        List<TransactionDto> recordList = getRecordList(sheet);
        List<LocalDate> dateList = recordList.stream().map(TransactionDto::getOriginalDate).collect(Collectors.toList());
        List<LocalDate> dateListOrd = dateList.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());

        for (int i = 0; i < dateList.size(); i++) {
            TransactionDto transactionDto = recordList.get(i);
            transactionDto.setOrderedDate(dateListOrd.get(i));
            if (transactionDto.isInterest()){
                transactionDto.setInterestDate(interestDateService.getInterestDate(dto.getBody().getInterestDetail(),dateList.get(i)));
            }
        }

        recordList.forEach(System.out::println);



      /*  LocalDate now = LocalDate.now();
        Month month = now.getMonth();
        int i = month.maxLength();
        String displayName = month.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        System.out.println(displayName);
        System.out.println(i);
        LocalDate of = LocalDate.of(now.getYear(), now.getMonth().getValue(), i);
        System.out.println(of.getDayOfWeek().name());*/


    }

    private List<LocalDate> getTransactionDates(XSSFSheet sheet){
        int lastRowNum = sheet.getLastRowNum();
        ArrayList<String> dateValues = new ArrayList<>();

        for (int rowNum = 0; rowNum <= lastRowNum; rowNum++) {
            XSSFRow row = sheet.getRow(rowNum);
            if (row != null) {
                XSSFCell cell = row.getCell(0); // Assuming you want to read column 0 (A)
                if (cell != null) {
                    String value;
                    if (cell.getCellType() == CellType.NUMERIC) {
                        value = String.valueOf(cell.getNumericCellValue());
                    } else {
                        value = cell.getStringCellValue();
                    }
                    dateValues.add(value);
                }
            }
        }
        return dateValues.stream().filter(this::isDate).map(this::toLocalDate).collect(Collectors.toList());
    }
    private boolean isDate(String value){
        try {
            Double.valueOf(value);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private LocalDate toLocalDate(String value){
        Double excelDateValue = Double.valueOf(value);


        // Excel stores dates as the number of days since January 1, 1900
        // We need to subtract the number of days between Excel's epoch and Java's epoch
        long javaEpochDifference = 25569; // Number of days between January 1, 1900, and January 1, 1970

        // Convert the numeric value to days
        long days = (long) (excelDateValue - javaEpochDifference);

        // Convert days to LocalDate
        return LocalDate.ofEpochDay(days);
    }

    private List<TransactionDto> getRecordList(XSSFSheet sheet){
        int lastRowNum = sheet.getLastRowNum();
        ArrayList<TransactionDto> trDtoList = new ArrayList<>();

        for (int rowNum = 1; rowNum <= lastRowNum; rowNum++) {
            XSSFRow row = sheet.getRow(rowNum);
            if (row != null && Objects.nonNull(row.getCell(6))) {
                XSSFCell dateCell = row.getCell(0); // Assuming you want to read column 0 (A)
                XSSFCell balanceCell = row.getCell(6); //G
                XSSFCell interestValCell = row.getCell(7); //H

                TransactionDto trDto = new TransactionDto();
                trDto.setOriginalDate((Objects.nonNull(dateCell)&&dateCell.getCellType().equals(CellType.NUMERIC)&&
                        isDate(String.valueOf(dateCell.getNumericCellValue())))?toLocalDate(String.valueOf(dateCell.getNumericCellValue())):null);
                BigDecimal balance = (Objects.nonNull(balanceCell)&&balanceCell.getCellType().equals(CellType.NUMERIC))?
                        BigDecimal.valueOf(balanceCell.getNumericCellValue()):BigDecimal.ZERO;
                if (balance.equals(BigDecimal.ZERO)) {
                    continue;
                }
                trDto.setBalance(balance);

                boolean isInterest= Objects.nonNull(interestValCell) && interestValCell.getCellType().equals(CellType.NUMERIC)
                        && !BigDecimal.ZERO.equals(BigDecimal.valueOf(interestValCell.getNumericCellValue()));


                if (rowNum>1){
                    XSSFCell prevBalanceCell = sheet.getRow(rowNum - 1).getCell(6);
                    BigDecimal prevBalance = (Objects.nonNull(prevBalanceCell)&&prevBalanceCell.getCellType().equals(CellType.NUMERIC))?
                            BigDecimal.valueOf(prevBalanceCell.getNumericCellValue()):BigDecimal.ZERO;
                    BigDecimal debit = prevBalance.compareTo(balance)>0?prevBalance.subtract(balance):BigDecimal.ZERO;
                    BigDecimal credit = prevBalance.compareTo(balance)<0?balance.subtract(prevBalance):BigDecimal.ZERO;
                    if (isInterest) trDto.setInterest(credit);
                    trDto.setDebit(debit);
                    trDto.setCredit(credit);
                }else {
                    trDto.setCredit(BigDecimal.ZERO);
                    trDto.setDebit(BigDecimal.ZERO);
                }

                trDtoList.add(trDto);
            }
        }
        return trDtoList;
    }
}
