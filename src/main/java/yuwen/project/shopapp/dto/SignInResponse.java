package yuwen.project.shopapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInResponse {
    private final String accessToken;
    private final String tokenType = "Bearer"; // This field does not change, so no setter is needed

    // Lombok will generate the all-args constructor and the getter for accessToken
    // There's no need for a setter for the tokenType since it's a constant
}



