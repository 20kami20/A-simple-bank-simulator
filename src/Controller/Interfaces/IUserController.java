package Controller.Interfaces;

public interface IUserController {


    void registerUser();

    void loginUser();

    void updateUserDetails(int userId);

    void updatePin(int userId);
    void getUserInfo(int userId);
    void getBalance(int userId);

    String getValidString(String prompt, String errorMessage);

    void updateProfile(int userId);
}
