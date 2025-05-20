import junit.framework.TestCase;

public class AccountManagerTest extends TestCase {
    public void testStart(){
        AccountManager manager = new AccountManager();
        assertTrue(manager.existsUser("Patrick"));
        assertTrue(manager.existsUser("Molly"));
        assertTrue(manager.isValidUser("Patrick", "1234"));
        assertTrue(manager.isValidUser("Molly", "FloPup"));
    }

    public void testCreateAccount(){
        AccountManager manager = new AccountManager();
        manager.createAccount("Haru", "Balance");
        manager.createAccount("Emperor", "Dominance");
        manager.createAccount("Shiro", "Colors");
        assertTrue(manager.existsUser("Haru"));
        assertTrue(manager.existsUser("Emperor"));
        assertTrue(manager.existsUser("Shiro"));
        assertFalse(manager.existsUser("Life"));
        assertTrue(manager.isValidUser("Haru", "Balance"));
        assertTrue(manager.isValidUser("Emperor", "Dominance"));
        assertTrue(manager.isValidUser("Shiro", "Colors"));
        assertFalse(manager.isValidUser("Haru", "Colors"));
    }
}
