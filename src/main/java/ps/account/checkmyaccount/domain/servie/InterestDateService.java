package ps.account.checkmyaccount.domain.servie;

import org.springframework.stereotype.Service;
import ps.account.checkmyaccount.application.dto.request.InterestDetail;
import ps.account.checkmyaccount.application.dto.request.RequestDto;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
@Service
public class InterestDateService {

    public LocalDate getInterestDate(InterestDetail interestDetail,LocalDate date){
        if (interestDetail.isMidDate() && interestDetail.isRemoveSunday()){
            return getMonthMidDateWithoutSunday(date);
        } else if (interestDetail.isMidDate() && interestDetail.isRemoveSaturday()) {
            return getMonthMidDateWithoutWeekEnd(date);
        } else if (!interestDetail.isMidDate() && interestDetail.isRemoveSunday()) {
            return getMonthEndWithoutSunday(date);
        }
        return null;
    }
    private LocalDate getMonthEndInterest(LocalDate date){
        Month month = date.getMonth();
        try {
            return LocalDate.of(date.getYear(),date.getMonth().getValue(),month.maxLength());
        }catch (DateTimeException e){
            return LocalDate.of(date.getYear(),date.getMonth().getValue(),month.maxLength()-1);
        }
    }

    private LocalDate getMonthEndWithoutSunday(LocalDate date){
        LocalDate monthEndDate = getMonthEndInterest(date);
        if (monthEndDate.getDayOfWeek().name().equalsIgnoreCase("SUNDAY")){
            return monthEndDate.minusDays(1);
        }
        return monthEndDate;
    }

    private LocalDate getMonthEndWithoutWeekEnd(LocalDate date){
        LocalDate monthEndDate = getMonthEndInterest(date);
        if (monthEndDate.getDayOfWeek().name().equalsIgnoreCase("SUNDAY")){
            return monthEndDate.minusDays(2);
        } else if (monthEndDate.getDayOfWeek().name().equalsIgnoreCase("SATURDAY")) {
            return monthEndDate.minusDays(1);
        }
        return monthEndDate;
    }

    private LocalDate getMonthMidDateWithoutSunday(LocalDate date){
        if (date.getDayOfWeek().name().equalsIgnoreCase("SUNDAY")){
            return date.plusDays(1);
        }
        return date;
    }
    private LocalDate getMonthMidDateWithoutWeekEnd(LocalDate date){
        if (date.getDayOfWeek().name().equalsIgnoreCase("SUNDAY")){
            return date.plusDays(1);
        } else if (date.getDayOfWeek().name().equalsIgnoreCase("SATURDAY")) {
            return date.plusDays(2);
        }
        return date;
    }
}
