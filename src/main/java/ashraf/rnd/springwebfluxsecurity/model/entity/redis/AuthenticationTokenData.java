package ashraf.rnd.springwebfluxsecurity.model.entity.redis;

import ashraf.rnd.springwebfluxsecurity.model.request.AppUser;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationTokenData extends AppUser {

    private String token;

}
