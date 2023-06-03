package ps.account.checkmyaccount.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@AllArgsConstructor@NoArgsConstructor
public class TransactionDto {
    private LocalDate originalDate;
    private LocalDate orderedDate;
    private boolean dateStatus;
    private String day;
    private BigDecimal debit;
    private BigDecimal credit;
    private BigDecimal balance;
    private BigDecimal interest;
    private boolean isInterest;
    private BigDecimal interestRate;
    private LocalDate interestDate;

    private Map<String,String> description;


    public TransactionDto(LocalDate originalDate, LocalDate orderedDate, BigDecimal balance, boolean isInterest, BigDecimal interestRate,LocalDate interestDate) {
        this.originalDate = originalDate;
        this.orderedDate = orderedDate;
        this.dateStatus=orderedDate.equals(originalDate);
        this.day=originalDate.getDayOfWeek().name();
        this.balance = balance;
        this.isInterest = isInterest;
        this.interestRate = interestRate;
        this.interestDate = interestDate;
    }

    public TransactionDto(LocalDate originalDate, LocalDate orderedDate , LocalDate interestDate) {
        this.originalDate = originalDate;
        this.orderedDate = orderedDate;
        this.dateStatus=orderedDate.equals(originalDate);
        this.day=originalDate.getDayOfWeek().name();
        this.interestDate = interestDate;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
        this.isInterest = (!interest.equals(BigDecimal.ZERO));
    }

    public boolean isInterest() {
        return isInterest;
    }

    public void setOriginalDate(LocalDate originalDate) {
        this.originalDate = originalDate;
        if (originalDate!=null){
            this.day=originalDate.getDayOfWeek().name();
        }
    }

    public void setOrderedDate(LocalDate orderedDate) {
        this.orderedDate = orderedDate;
        boolean dateCheck = orderedDate.equals(originalDate);
        this.dateStatus=dateCheck;
        if (!dateCheck){
            addDescription("Date","Order of the date does not Match");
        }
    }

    public void addDescription(String key, String value){
        this.description.put(key,value);
    }
}
