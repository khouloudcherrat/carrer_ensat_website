package factories;
import com.ENSATApp.EApp.model.LoginInfo;
import com.github.javafaker.Faker;

public class LoginInfoFactory {
    private static final Faker faker = new Faker();

    // List of available roles
    private static final String[] ROLE_OPTIONS = {
        "Alumni",
        "Student",
        "Partner",
        "Admin",
    };

    // Method to generate a random LoginInfo object
    public static LoginInfo create() {
        LoginInfo loginInfo = new LoginInfo();

        loginInfo.setEmail(faker.internet().emailAddress());
        loginInfo.setPassword(faker.internet().password());
        loginInfo.setRole(faker.options().option(ROLE_OPTIONS));// Randomly pick from ROLE_OPTIONS

        return loginInfo;
    }
}
