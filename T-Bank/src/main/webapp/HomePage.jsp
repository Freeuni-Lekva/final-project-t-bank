<%--
  Created by IntelliJ IDEA.
  User: uruma
  Date: 7/29/2021
  Time: 9:44 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Welcome to TBank</title>
    <link rel="stylesheet" href="HomePage.css">
</head>
<body>

<div class="navigation">
    <ul>
        <br>
        <li><a href="AccountServlet">Account Information </a></li>
        <li><a href="CardCreationServlet">Add a Card</a></li>
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

<div class="container">
    <h1> Welcome ${username} ! </h1>
</div>


</body>
</html>
