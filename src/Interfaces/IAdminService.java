package Interfaces;

public interface IAdminService {
    void blockAccount(int userId);
    void unblockAccount(int userId);
    void deleteUser(int userId);


    void viewAllUsers();


    void searchUserByIdOrUsername(String input);
}
