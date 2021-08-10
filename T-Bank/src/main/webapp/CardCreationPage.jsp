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
    <link rel="stylesheet" href="TBank.css"/>
    <link rel="stylesheet" href="HomePage.css">
</head>
<body style="background-color: sandybrown">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="navigation">
    <ul>
        <li><a href="AccountServlet">Account Information </a></li>
        <li><a class="accountInfoTag" href="CardCreationServlet">Add or Remove a Card</a></li>
        <li><a href="CardsListServlet">View All Cards</a></li>
        <li><a href="CurrencyServlet">Currency Exchange</a></li>
        <li>Crowd Funding</li>
        <li>Deposits and Loans</li>
        <li><a href="TransfersServlet">Transfer by ID</a></li>
        <li><a href="IBANTransfersServlet">Transfer by IBAN</a></li>
        <li><a style="border:2px solid; color:red" href="LogoutServlet">Log Out</a></li>
    </ul>
    <p class="note">Welcome to TBank</p>
</div>

<div class="form">
    <h1 class="header">Add New Card</h1>
    <form action="CardCreationServlet" method="post">
        <h3>${successMessage}</h3>
        <label>Card Type:</label><br>
        <select style="width: 95%" name="dropdown">
            <c:forEach items="${cardTypes}" var="type" varStatus="loop">
                <option value="${loop.index}">
                        ${type.cardTypeName} - limit: ${type.maxLimit}
                </option>
            </c:forEach>
        </select><br><br>

        <h4>${errorMessage}</h4>

        <label>Card Name:</label><br>
        <input type="text" name="cardName" placeholder="Enter Card Name..."><br><br>

        <button type="submit">Create Card</button>
        <p>See all <a href="CardsListServlet">Cards.</a></p>
    </form>
</div>
</body>
</html>
