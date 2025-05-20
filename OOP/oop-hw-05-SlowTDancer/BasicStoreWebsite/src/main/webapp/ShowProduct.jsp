<%@ page import="DB.BSWDatabase" %>
<%@ page import="DB.Product" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%  String productId = request.getParameter("id");
    BSWDatabase db = (BSWDatabase) request.getServletContext().getAttribute("database");
    Product curr = db.getProduct(productId);
%>
<head>
    <title> <%=curr.getProductName()%></title>
</head>
<body>
<h1> <%=curr.getProductName()%></h1>
<img src="store-images/<%=curr.getImageFileName()%>" alt="Not Found">
<form action = "ShoppingCartServlet" method = "post">
    <p> <%=Double.toString(curr.getProductPrice()) + "$"%></p>
    <input type="hidden" id="id" name="id" value="<%=curr.getProductId()%>">
    <input type = "submit" value = "Add to Cart">
</form>
</body>
</html>