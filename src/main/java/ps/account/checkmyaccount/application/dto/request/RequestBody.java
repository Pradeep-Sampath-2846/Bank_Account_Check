package ps.account.checkmyaccount.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data@AllArgsConstructor@NoArgsConstructor
public class RequestBody implements Serializable {
    private BigDecimal finalBalance;
    private BigDecimal totalCredits;
    private BigDecimal totalDebits;

    private InterestDetail interestDetail;

}
