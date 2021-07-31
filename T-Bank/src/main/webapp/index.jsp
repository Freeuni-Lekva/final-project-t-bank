<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>TBank Login</title>
    <link rel="stylesheet" href="TBank.css" />
</head>
<body>


<div class="form">
        <h1 class="header"> Welcome to TBank </h1>
        <h3 > Please Fill in Username and Password.</h3>
        <form action="LoginServlet" method="post">
        <label>Username: </label>
            <label>
                <input type="text" name="username" placeholder="Enter Username..."/>
            </label>
            <br>
        <h4> ${username}</h4>

        <label>Password: </label>
            <label>
                <input type="password" name="password" placeholder="Enter Password..."/>
            </label>
            <br>
            <br>
            <button type="submit">Login</button>
            <br>
            <p>New to TBank? <a href="RegisterPage.jsp">Register.</a> </p>
        <% //RegisterServlet should be added and reference should be added to that servlet as well. %>
    </form>
</div>






</body>
</html>