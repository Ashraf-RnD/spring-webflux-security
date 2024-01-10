package ashraf.rnd.springwebfluxsecurity.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {
    private String userAudience;
    private String userId;
    private String deviceId;

}
