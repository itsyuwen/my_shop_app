package yuwen.project.shopapp.dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignInRequest {
    @NotNull
    private String username; // or email, depending on your authentication identifier
    @NotNull
    private String password;

}


