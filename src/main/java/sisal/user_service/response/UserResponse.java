
package sisal.user_service.response;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private UUID id;
    private String email;

    public UserResponse (UUID id, String email) {
        this.id = id;
        this.email = email;
    }


}
