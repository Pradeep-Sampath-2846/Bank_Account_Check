package ps.account.checkmyaccount.domain.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionDto {
    private LocalDate originalDate;
    private LocalDate orderedDate;
    private boolean dateStatus;
    private String day;
    private double balance;
    private boolean isInterest;
    private double interestRate;
    private LocalDate interestDate;


    public TransactionDto(LocalDate originalDate, LocalDate orderedDate, double balance, boolean isInterest, double interestRate,LocalDate interestDate) {
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


}
