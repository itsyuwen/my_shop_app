package yuwen.project.shopapp.dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequest {
    @NotNull
    private String username;
    @NotNull
    private String email; // Include only if required
    @NotNull
    private String password;


}
