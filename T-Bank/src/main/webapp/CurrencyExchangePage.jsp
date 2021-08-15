<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: uruma
  Date: 8/7/2021
  Time: 2:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Currency Exchange</title>
    <link rel="stylesheet" href="HomePage.css">
</head>
<body>
<div class="navigation">
    <ul>
        <br>
        <li><a href="AccountServlet">Account Information </a></li>
        <li><a href="CardCreationServlet">Add a Card</a></li>
        <li><a href="CardsListServlet">View All Cards</a></li>
        <li><a class="accountInfoTag" href="CurrencyServlet">Currency Exchange</a></li>
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

<div >
    <h1 style="margin-left:340px;"> FROM: </h1>
    <form action="CurrencyServlet" method="post" name="tmp">
    <c:forEach items="${Currencies}" var="curr" varStatus="loop">

            <input style="margin-left:340px"  value="${loop.index}"  type="radio" id="currencyType" name="from">
            <label style="font-weight:bold" for="currencyType">${curr.currencyName} </label>
            <span style=" font-style: italic" > CALL:${curr.call} BID:${curr.bid}</span>
            <br>
    </c:forEach>

        <h1 style="margin-left:340px;"> TO: </h1>
            <c:forEach items="${Currencies}" var="curr" varStatus="loop">

                <input style="margin-left:340px" value="${loop.index}" type="radio" id="currencyType" name="to">
                <label style="font-weight:bold" for="currencyType">${curr.currencyName} </label>
                <span style=" font-style: italic" > CALL:${curr.call} BID:${curr.bid}</span>
                <br>
            </c:forEach>
        <br>
        <br>

    <label style="margin-left:340px;">Choose a Card: </label>
        <label>
            <select name="cards">
                <c:forEach items="${CardID}" var="card" varStatus="loop">
                    <option value="${loop.index}">
                        ${card}
                    </option>
                </c:forEach>

            </select>
        </label>
        <br>
    <br>
        <label style="margin-left:340px">Enter Amount of Money: </label>
        <label>
            <input type="number" name="amount"/>
        </label>
        <button type="submit" name="calculate">Calculate</button>
        <button type="submit" name="transfer">Transfer</button>
    </form>



    <h4 style="color: red; font-weight:bold; margin-left:320px" > ${Amount} ${to} ${error} </h4>
</div>


</body>
</html>
