package johnoliveira.eventTracker_capstoneProject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NewUserDTO(
        @NotEmpty(message = "The name must be provided")
        @Size(min = 2, max = 40, message = "The name must contain at least two characters and cannot exceed 40 characters")
        String name,

        @NotEmpty(message = "The surname must be provided")
        @Size(min = 2, max = 40, message = "The surname must contain at least two characters and cannot exceed 40 characters")
        String surname,

        @NotEmpty(message = "Email must be provided")
        @Email(message = "Invalid email")
        String email,

        @NotEmpty(message = "Password must be provided")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,20}$",
                message = "Password must be 8-20 characters long and include at least one digit, one uppercase letter, one lowercase letter, and one special character")
        String password
) {}

