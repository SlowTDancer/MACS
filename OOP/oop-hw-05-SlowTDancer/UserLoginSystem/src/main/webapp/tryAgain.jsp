<%--
  Created by IntelliJ IDEA.
  User: ikako
  Date: 6/11/2023
  Time: 1:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head><title>Information Incorrect</title></head>

<body>
    <h1>Please Try Again</h1>

    <p>Either your username or password is incorrect. Please try again.</p>

    <form action = "LoginServlet" method = "post">
        <label>Username:</label>
        <input type = "text" name = "username"><br>

        <label>Password:</label>
        <input type = "password"  name = "password">
        <input type = "submit"  name = "Login" value = "Login"><br><br>

        <a href="createAccount.jsp">Create New Account</a>
    </form>
</body>
</html>