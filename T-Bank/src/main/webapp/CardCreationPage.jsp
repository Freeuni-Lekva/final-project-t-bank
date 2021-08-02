<%@ page import="com.example.T_Bank.Storage.CardType" %><%--
  Created by IntelliJ IDEA.
  User: naten
  Date: 8/2/2021
  Time: 19:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create new Card</title>
</head>
<body>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="form">
    <h1>Add New Card</h1>
    <form action="CardCreationServlet" method="post">
        <label>Card Type</label><br>
        <select name="dropdown">
            <c:forEach items="${cardTypes}" var="type" varStatus="loop">
                <option value="${loop.index}">
                        ${type.cardTypeName} - limit: ${type.maxLimit}
                </option>
            </c:forEach>
        </select><br><br>

        <label>Card Name</label><br>
        <input type="text" name="cardName" placeholder="Enter Card Name..."><br><br>

        <h4>${errorMessage}</h4>

        <button type="submit">Create Card</button>
    </form>
</div>
</body>
</html>
