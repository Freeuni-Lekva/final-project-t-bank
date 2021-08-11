<%--
  Created by IntelliJ IDEA.
  User: Surface
  Date: 8/9/2021
  Time: 5:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Create event</title>
    <link rel="stylesheet" href="HomePage.css">
    <link rel="stylesheet" href="TBank.css">
</head>
<body>
<div class="navigation">
    <ul>
        <li><a href="AccountServlet">Account Information </a></li>
        <li><a href="CardCreationServlet">Add or Remove a Card</a></li>
        <li><a href="CardsListServlet">View All Cards</a></li>
        <li><a href="CurrencyServlet">Currency Exchange</a></li>
        <li><a class="accountInfoTag" style="font-size:18px" href="CrowdFundingServlet"> Crowd Funding - My Events </a> </li>
        <li>Deposits and Loans</li>
        <li><a href="TransfersServlet">Transfer by ID</a></li>
        <li><a href="IBANTransfersServlet">Transfer by IBAN</a></li>
        <li><a style="border:2px solid; color:red" href="LogoutServlet">Log Out</a></li>
    </ul>
    <p class="note">Welcome to TBank</p>
</div>

<div class="form">
    <form action="CreateEventServlet" method="post">
        <H1 class="header" name="title">Create event</H1><br>
        <label>Event name</label><br>
        <input type="text" name="eventName" placeholder="Enter event name"><br><br>
        <label>Event Description</label><br>
        <textarea style="width: 96%" rows="8" type="text" name="description"
                  placeholder="Enter event description"></textarea><br><br>
        <label>Target card</label><br>
        <select style="width: 60%" name="targetCards">
            <c:forEach items="${cards}" var="card" varStatus="loop">
                <option value="${loop.index}">
                        ${card}
                </option>
            </c:forEach>
        </select>
        <select   name="targetCurrencies">
            <c:forEach items="${currencies}" var="currency" varStatus="loop">
                <option value="${loop.index}">
                        ${currency.getCurrencyName()}
                </option>
            </c:forEach>
        </select><br><br>
        <label>Target</label><br>
        <input type="number" step="0.01" name="target" placeholder="0"><br><br>
        <h3>${success}</h3>
        <h4 style="color: red">${error}</h4>
        <button type="submit">Create</button>
    </form>

</div>
</body>
</html>

