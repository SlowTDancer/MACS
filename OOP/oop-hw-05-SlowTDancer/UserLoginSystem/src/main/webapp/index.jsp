<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head><title>Welcome</title></head>

<body>

    <h1>Welcome To Homework 5</h1>

    <p>Please log in.</p>

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