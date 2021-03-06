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
<body style="background-image: url(https://www.colorbook.io/imagecreator.php?hex=fbb486&width=1920&height=1080&text=%201920x1080);">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="navigation">
    <ul>
        <br>
        <li><a href="AccountServlet">Account Information </a></li>
        <li><a class="accountInfoTag" href="CardCreationServlet">Add a Card</a></li>
        <li><a href="CardsListServlet">View All Cards</a></li>
        <li><a href="CurrencyServlet">Currency Exchange</a></li>
        <li><a href="CrowdFundingServlet">My Events</a></li>
        <li><a href="EventsListServlet">View All Events</a></li>
        <li><a href="LoanServlet">Loans</a></li>
        <li><a href="DepositsServlet">Deposits</a></li>
        <li><a href="TransfersServlet">Transfer by ID</a></li>
        <li><a href="IBANTransfersServlet">Transfer by IBAN</a></li>
        <li><a href="TransactionsHistoryServlet">Transactions History</a></li>
        <li><a style="border:2px solid; color:red" href="LogoutServlet">Log Out</a></li>
    </ul>
    <p class="note">Welcome to TBank</p>
</div>

<div class="form" style="border:5px solid black">
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
