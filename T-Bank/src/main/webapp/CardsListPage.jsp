<%--
  Created by IntelliJ IDEA.
  User: naten
  Date: 8/2/2021
  Time: 23:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cards</title>
    <link rel="stylesheet" href="HomePage.css">
    <link rel="stylesheet" href="Cards.css"/>
</head>
<body>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div>
    <div class="navigation">
        <ul>
            <li><a href="AccountServlet">Account Information </a></li>
            <li><a href="CardCreationServlet">Add or Remove a Card</a></li>
            <li><a class="accountInfoTag" href="CardsListServlet">View All Cards</a></li>
            <li><a href="CurrencyServlet">Currency Exchange</a></li>
            <li>Crowd Funding</li>
            <li>Deposits and Loans</li>
            <li><a href="TransfersServlet">Transfer by ID</a></li>
            <li><a href="IBANTransfersServlet">Transfer by IBAN</a></li>
            <li><a href="EventsListServlet">View All Events</a></li>
        </ul>
        <p class="note">Welcome to TBank</p>
    </div>

    <div style="margin-left: 340px">
        <div class="balance">
            <div id="GELTotal" style="font-size: xx-large; font-weight: bold">Total amount: ${GELsum} GEL</div>
            <span style="margin-right: 10px; font-weight: bold">${USDsum} USD</span>
            <span style="font-weight: bold">${EURsum} EUR</span>
        </div>

        <c:forEach items="${cards}" var="card" varStatus="loop">
            <div class="card">
                <h1>${card.cardName}</h1>
                <h2>${card.gelBalance} GEL</h2>
                <span style="margin-right: 5px">${card.usdBalance} USD</span>
                <span>${card.euroBalance} Euro</span>
                <h2>${card.cardIdentifier}</h2>
            </div>
        </c:forEach>
    </div>
</div>
<br class="clear"/>

</body>
</html>
