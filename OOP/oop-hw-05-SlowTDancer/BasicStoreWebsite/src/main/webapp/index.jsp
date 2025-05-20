<%@ page import="DB.BSWDatabase" %>
<%@ page import="DB.Product" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Student Store</title>
</head>
<body>
<h1>Student Store</h1>

<p>Items available:</p>
<ul>
    <% BSWDatabase db = (BSWDatabase) request.getServletContext().getAttribute("database");
        try {
            for(Product p : db.getProductList()){
                String s = "<li>" + "<a href=\"ShowProduct.jsp?id=" + p.getProductId() + "\"> "+ p.getProductName()+ " </a>" + "</li>";
                out.println(s);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    %>
</ul>
</body>
</html>