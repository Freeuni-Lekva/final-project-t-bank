<%@ page import="com.example.T_Bank.Storage.CrowdFundingEvent" %>
<%@ page import="java.util.ArrayList" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: uruma
  Date: 8/11/2021
  Time: 4:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Crowd Funding Page</title>
    <link rel="stylesheet" href="MyEvents.css">
    <link rel="stylesheet" href="HomePage.css">
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

<div style="margin-left:320px">

    <form style="margin-left:40px" action="CreateEventServlet" method="get">
        <button type="submit" name="create">Create a new Event</button>
    </form>



    <c:forEach items="${EventList}" var="event" varStatus="loop">
        <div class="event">
            <h1>${event.eventName}</h1>
            <p>${event.description} </p>
            <h2 style="margin-right: 5px">Target: ${event.target} ${event.currency.currencyName} </h2>
            <h2>Done: ${event.done} ${event.currency.currencyName} </h2>
            <h2>${event.cardIdentifier}</h2>
            <form style="margin-left:20px" action="CrowdFundingServlet" method="post">
                <input style="width:200px" type="number" name="targetAmount" placeholder="Enter new target Amount."/>
                <p style="color:red"> ${error} </p>
                <br>
                <br>
                <button type="submit" name="${loop.index}Modify">Modify Target</button>
                <br>
                <br>
                <button type="submit" name="${loop.index}Delete">Delete Event</button>
            </form>

        </div>

    </c:forEach>









</div>

</body>
</html>
