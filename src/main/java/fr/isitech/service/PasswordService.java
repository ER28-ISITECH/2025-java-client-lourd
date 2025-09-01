package fr.isitech.service;

import fr.isitech.model.Password;

public class PasswordService {
    private Password[] passwords;

    public PasswordService() {
        // Initialize with some dummy data
        passwords = new Password[]{
                new Password() {{
                    id = 1;
                    title = "Google";
                    username = "johndoe";
                    password = "password123";
                }},
                new Password() {{
                    id = 2;
                    title = "Facebook";
                    username = "janedoe";
                    password = "mypassword";
                }}
        };
    }

    public Password[] getAllPasswords() {
        return passwords;
    }

    public Password getPasswordById(int id) {
        for (Password pwd : passwords) {
            if (pwd.id == id) {
                return pwd;
            }
        }
        return null;
    }
}
