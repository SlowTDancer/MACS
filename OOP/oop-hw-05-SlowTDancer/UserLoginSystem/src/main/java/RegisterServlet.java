import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletRequest.getRequestDispatcher("createAccount.jsp").forward(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        AccountManager manager = (AccountManager) httpServletRequest.getServletContext().getAttribute("manager");
        String username = (String) httpServletRequest.getParameter("username");
        String password = (String) httpServletRequest.getParameter("password");
        RequestDispatcher rd;
        if(!manager.existsUser(username)){
            manager.createAccount(username, password);
            rd = httpServletRequest.getRequestDispatcher("userWelcome.jsp");
        }else{
            rd = httpServletRequest.getRequestDispatcher("nameInUse.jsp");
        }
        rd.forward(httpServletRequest, httpServletResponse);
    }
}
