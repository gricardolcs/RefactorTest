package sat.recruitment.api.common;

public class HandleErrors {

    public String validateErrors(String name, String email, String address, String phone, String errors) {
        if (name == null)
            // Validate if Name is null
            errors = "The name is required";
        if (email == null)
            // Validate if Email is null
            errors = errors + " The email is required";
        if (address == null)
            // Validate if Address is null
            errors = errors + " The address is required";
        if (phone == null)
            // Validate if Phone is null
            errors = errors + " The phone is required";

        return errors;
    }
}
