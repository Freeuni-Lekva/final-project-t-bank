<%--
  Created by IntelliJ IDEA.
  User: naten
  Date: 8/6/2021
  Time: 20:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head onload="update()">
    <title>Transfers</title>
    <link rel="stylesheet" href="TBank.css">
    <link rel="stylesheet" href="HomePage.css">
</head>
<body>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="navigation">
    <ul>
        <li><a href="AccountServlet">Account Information </a></li>
        <li><a href="CardCreationServlet">Add or Remove a Card</a></li>
        <li><a href="CardsListServlet">View All Cards</a></li>
        <li>Crowd Funding</li>
        <li>Deposits and Loans</li>
        <li><a class="accountInfoTag" href="TransfersServlet">Transfer by ID</a></li>
        <li><a href="IBANTransfersServlet">Transfer by IBAN</a></li>
        <li><a href="EventsListServlet">View All Events</a></li>
    </ul>
    <p class="note">Welcome to TBank</p>
</div>

<div class="form">
    <h1 class="header">Transfer Money</h1>
    <form action="TransfersServlet" method="get">
        <label>Receiver ID</label><br>
        <input type="text" name="receiverID" placeholder="Enter Receiver ID" value="${receiverID}"><br>

        <h4>${errorMessage}</h4>
        <button type="submit">Check ID</button>
        <br>
    </form>

    <form action="TransfersServlet" method="post">
        <label>Receiver IBAN</label><br>
        <select style="width: 95%" name="receiverDropdown">
            <c:forEach items="${receiverAccounts}" var="account" varStatus="loop1">
                <option value="${loop1.index}">
                        ${account}
                </option>
            </c:forEach>
        </select><br><br>

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

        <label for="fromCurrency">From:</label>

        <select name="fromCurrency" id="fromCurrency">
            <option value="0">GEL</option>
            <option value="1">USD</option>
            <option value="2">EUR</option>
        </select>

        <label for="toCurrency">To:</label>
        <select name="toCurrency" id="toCurrency">
            <option value="0">GEL</option>
            <option value="1">USD</option>
            <option value="2">EUR</option>
        </select>

        <h4>${transferError}</h4>
        <h3>${transferSuccess}</h3>
        <button type="submit">Transfer</button>
    </form>
</div>
</body>
</html>
