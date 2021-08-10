<%--
  Created by IntelliJ IDEA.
  User: naten
  Date: 8/9/2021
  Time: 21:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Events</title>
    <link rel="stylesheet" href="HomePage.css">
    <link rel="stylesheet" href="Cards.css"/>
</head>
<body>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
    function copyID(id) {
        document.getElementById("eventID").value = id;
    }

    function copyName(name) {
        document.getElementById("eventName").innerHTML = name.value;
    }
</script>

<div class="navigation">
    <ul>
        <li><a href="AccountServlet">Account Information </a></li>
        <li><a href="CardCreationServlet">Add or Remove a Card</a></li>
        <li><a href="CardsListServlet">View All Cards</a></li>
        <li><a href="CurrencyServlet">Currency Exchange</a></li>
        <li>Crowd Funding</li>
        <li>Deposits and Loans</li>
        <li><a href="TransfersServlet">Transfer by ID</a></li>
        <li><a href="IBANTransfersServlet">Transfer by IBAN</a></li>
        <li><a class="accountInfoTag" href="EventsListServlet">View All Events</a></li>
    </ul>
    <p class="note">Welcome to TBank</p>
</div>

<div style="width: 80%">
    <div style="margin-left: 340px; text-align: center;">
        <form action="EventsListServlet" method="get">
            <input type="text" name="searchBar" placeholder="Enter personal ID...">
            <button type="submit">Search</button>
            <br>
            <h4 style="color: red">${eventError}</h4>
        </form>
    </div>

    <div style="margin-left: 340px">
        <c:forEach items="${events}" var="event" varStatus="loop">
            <div class="card">
                <h1>${event.eventName}</h1>
                <h2>${event.target} ${event.currency.currencyName}</h2>
                <p style=" word-wrap: normal; height: 140px ">${event.description}</p>
                <h2>${event.cardIdentifier}</h2>
                <button onclick="copyID(${event.eventId}); copyName(this);" value="${event.eventName}">Fund</button>
            </div>
        </c:forEach>
    </div>
</div>

<div style=" position: fixed; margin-left: 1540px">
    <form action="EventsListServlet" method="post">
        <h1>Fund an Event</h1>

        <input style="visibility: hidden" type="text" id="eventID" name="eventID"><br>
        <label>Funding Event : </label>
        <label type="text" id="eventName" name="eventName" placeholder="Enter Event Name"></label><br><br>

        <label>Sender IBAN</label><br>
        <select style="width: 95%" name="senderDropdown">
            <c:forEach items="${senderAccounts}" var="account" varStatus="loop">
                <option value="${loop.index}">
                        ${account}
                </option>
            </c:forEach>
        </select><br><br>

        <label>Amount</label><br>
        <input type="number" step="0.01" name="amount" placeholder="00.00"><br><br>

        <select name="fromCurrency" id="fromCurrency">
            <option value="0">GEL</option>
            <option value="1">USD</option>
            <option value="2">EUR</option>
        </select>

        <h2 >${transferSuccess}</h2>
        <h4 style="color: red">${transferError}</h4>
        <button type="submit">Transfer</button>
    </form>
</div>
</body>
</html>
