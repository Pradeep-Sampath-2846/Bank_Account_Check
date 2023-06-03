package ps.account.checkmyaccount.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor@NoArgsConstructor
public class RequestHeaders implements Serializable {

    private String bankName;
    private String branch;
    private String accountHolder;
}
