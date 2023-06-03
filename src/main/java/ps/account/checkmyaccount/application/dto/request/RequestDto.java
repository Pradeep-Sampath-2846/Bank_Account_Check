package ps.account.checkmyaccount.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data@AllArgsConstructor@NoArgsConstructor
public class RequestDto implements Serializable {
    private RequestHeaders headers;

    private RequestBody body;

}
