import DB.BSWDatabase;
import DB.Product;
import DB.ShoppingCart;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/ShoppingCartServlet")
public class ShoppingCartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletRequest.getRequestDispatcher("ShoppingCart.jsp").forward(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        BSWDatabase db = (BSWDatabase) httpServletRequest.getServletContext().getAttribute("database");
        ShoppingCart sc = (ShoppingCart) httpServletRequest.getSession().getAttribute("shoppingCart");
        if(sc == null){
            sc = new ShoppingCart();
            httpServletRequest.getSession().setAttribute("shoppingCart", sc);
        }
        Product product = null;
        try {
            product = db.getProduct(httpServletRequest.getParameter("id"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        RequestDispatcher rd;
        if(product == null){
            Map<String, String[]> mp = httpServletRequest.getParameterMap();
            for(String key: mp.keySet()){
                try {
                    sc.updateShoppingCart(db.getProduct(key), Integer.parseInt(mp.get(key)[0]));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            rd = httpServletRequest.getRequestDispatcher("ShoppingCart.jsp");
        }else{
            sc.addItem(product);
            rd = httpServletRequest.getRequestDispatcher("ShoppingCart.jsp");
        }
        rd.forward(httpServletRequest, httpServletResponse);
    }
}
