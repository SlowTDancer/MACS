<%@ page import="DB.ShoppingCart" %>
<%@ page import="DB.BSWDatabase" %>
<%@ page import="DB.Product" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Shopping Cart</title>
</head>
<h1>Shopping Cart</h1>
<body>
<form action="ShoppingCartServlet" method="post">
<ul>
    <% ShoppingCart sc = (ShoppingCart) request.getSession().getAttribute("shoppingCart");
        BSWDatabase db = (BSWDatabase) request.getServletContext().getAttribute("database");
        for(String key: sc.getShoppingCart().keySet()){
            String s = "<li>" + "<input type = \"number\" name = \""+ key + "\" value = " + sc.getShoppingCart().get(key) +">" +
                    db.getProduct(key).getProductName() + ", " + sc.getItemCost(db.getProduct(key)) + "</li>";
            out.println(s);
        }
    %>

</ul>
    <p>Total:$<%=((ShoppingCart)request.getSession().getAttribute("shoppingCart")).getTotalCost()%></p>
    <input type = "submit" value = "Update Cart">
</form>
<a href="index.jsp">Continue Shopping</a>
</body>
</html>