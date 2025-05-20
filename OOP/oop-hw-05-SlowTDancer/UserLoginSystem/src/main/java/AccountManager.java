import java.util.HashMap;
import java.util.Map;

public class AccountManager {
    private Map<String, String> userData;
    public AccountManager(){
        userData = new HashMap<>();
        createStartingAccounts();
    }

    private void createStartingAccounts(){
        userData.put("Patrick", "1234");
        userData.put("Molly", "FloPup");
    }
    public boolean existsUser(String username){
        return userData.containsKey(username);
    }

    public boolean isValidUser(String username, String password){
        if(!existsUser(username)) return false;
        String realPassword = userData.get(username);
        return realPassword.equals(password);
    }

    public void createAccount(String username, String password){
        userData.put(username, password);
    }
}
