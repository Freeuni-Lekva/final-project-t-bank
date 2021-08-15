<%--
  Created by IntelliJ IDEA.
  User: naten
  Date: 8/7/2021
  Time: 15:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head onload="update()">
    <title>Transfers</title>
    <link rel="stylesheet" href="TBank.css">
    <link rel="stylesheet" href="HomePage.css">
</head>
<body style="background-image: url(https://www.colorbook.io/imagecreator.php?hex=fbb486&width=1920&height=1080&text=%201920x1080);">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="navigation">
    <ul>
        <li><a href="AccountServlet">Account Information </a></li>
        <li><a href="CardCreationServlet">Add or Remove a Card</a></li>
        <li><a href="CardsListServlet">View All Cards</a></li>
        <li><a href="CurrencyServlet">Currency Exchange</a></li>
        <li><a href="CrowdFundingServlet"> Crowd Funding - My Events </a> </li>
        <li>Deposits and Loans</li>
        <li><a href="TransfersServlet">Transfer by ID</a></li>
        <li><a class="accountInfoTag" href="IBANTransfersServlet">Transfer by IBAN</a></li>
        <li><a href="EventsListServlet">View All Events</a></li>
        <li><a style="border:2px solid; color:red" href="LogoutServlet">Log Out</a></li>
    </ul>
    <p class="note">Welcome to TBank</p>
</div>

<div class="form" style="border:5px solid black">
    <h1 class="header">Transfer Money</h1>
    <form action="IBANTransfersServlet" method="post">
        <label>Receiver IBAN</label><br>
        <input type="text" name="receiverIBAN" placeholder="Enter Receiver IBAN" value="${receiverIBAN}"><br><br>

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
