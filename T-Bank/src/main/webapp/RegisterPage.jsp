<%-- Created by IntelliJ IDEA. User: naten Date: 7/29/2021 Time: 22:44 To change this template use File | Settings |
    File Templates. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <title>Register</title>
    <link rel="stylesheet" href="TBank.css"/>
</head>

<body>
<div class="form">
    <form action="RegisterServlet" method="post">
        <H1 class="header" name="title">Create New Account</H1>
        <br>
        <label>First name</label><br>
        <input type="text" name="firstName" placeholder="Enter your First Name..."><br><br>
        <label>Last name</label><br>
        <input type="text" name="lastName" placeholder="Enter your Last Name..."><br><br>
        <label>ID</label><br>
        <input type="text" name="id" placeholder="Enter your ID..."><br><br>
        <label>Birth Date</label><br>
        <input type="date" name="birthDate"><br><br>
        <label>User Name</label><br>
        <input type="text" name="username" placeholder="Enter your User Name..."><br><br>
        <label>Password</label><br>
        <input type="password" name="password" placeholder="Enter your Password..."><br><br>
        <label>Confirm Password</label><br>
        <input type="password" name="repeatPassword" placeholder="Confirm your Password..."><br>
        <h4 style="color: red">${errorMessage}</h4>
        <button type="submit">Create Account</button>
        <p>Already an user? <a href="index.jsp">Log in</a></p>
    </form>
</div>
</body>

</html>