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
        <li><a href="AccountServlet">Account Information </a></li>
        <li><a href="CardCreationServlet">Add or Remove a Card</a></li>
        <li><a href="CardsListServlet">View All Cards</a></li>
        <li><a href="CurrencyServlet">Currency Exchange</a></li>
        <li>Crowd Funding</li>
        <li>Deposits and Loans</li>
        <li><a href="TransfersServlet">Transfer by ID</a></li>
        <li><a href="IBANTransfersServlet">Transfer by IBAN</a></li>
    </ul>
    <p class="note">Welcome to TBank</p>
</div>

<div class="container">
    <h1> Welcome ${username} ! </h1>
</div>


</body>
</html>
