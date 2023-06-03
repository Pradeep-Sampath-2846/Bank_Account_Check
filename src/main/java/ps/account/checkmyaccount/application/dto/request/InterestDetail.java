package ps.account.checkmyaccount.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data@NoArgsConstructor@AllArgsConstructor
public class InterestDetail implements Serializable {

    private boolean removeSunday;

    private boolean removeSaturday;

    private boolean midDate;

    private int intDay;


}
