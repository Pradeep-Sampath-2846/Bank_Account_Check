package ps.account.checkmyaccount.domain.servie;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ps.account.checkmyaccount.domain.dto.TransactionDto;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class FileProcessorService {
    @Autowired
    private InterestDateService interestDateService;

    public void validateExcelFile(XSSFSheet sheet){

        List<LocalDate> dateList = getTransactionDates(sheet);
        List<LocalDate> dateListOrd = dateList.stream().collect(Collectors.toList());
        Collections.sort(dateListOrd, Comparator.naturalOrder());

        ArrayList<TransactionDto> transactionDtos = new ArrayList<>();
        for (int i = 0; i < dateList.size(); i++) {
            transactionDtos.add(new TransactionDto(dateList.get(i),
                    dateListOrd.get(i),interestDateService.getMonthEndWithoutSunday(dateList.get(i))));
        }

        transactionDtos.stream().forEach(System.out::println);


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
        System.out.println("length of the list: "+dateValues.size());
        return dateValues.stream().filter(a->isDate(a)).map(a -> toLocalDate(a)).collect(Collectors.toList());
    }
    private boolean isDate(String value){
        try {
            Double num = Double.valueOf(value);
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
}
