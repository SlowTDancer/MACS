<%--
  Created by IntelliJ IDEA.
  User: ikako
  Date: 6/11/2023
  Time: 1:57 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head><title>Create Account</title></head>

<body>

    <h1>The Name <%= request.getParameter("username") %> Is Already In Use</h1>

    <p>Please enter another name and password.</p>

    <form action = "RegisterServlet" method = "post">
        <label>Username:</label>
        <input type = "text" name = "username"><br>

        <label>Password:</label>
        <input type = "password"  name = "password">
        <input type = "submit"  name = "Login" value = "Login"><br><br>
    </form>
</body>
</html>
