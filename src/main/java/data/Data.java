package data;

import com.github.javafaker.Faker;

public class Data {


    private static final Faker faker = new Faker();

    public static String generateRandomEmail() {
        return faker.internet().emailAddress(); // Generates a realistic email address
    }

    public static String generateRandomPassword() {
        return faker.internet().password(8, 16, true, true, true); // Generates a strong password
    }
}
