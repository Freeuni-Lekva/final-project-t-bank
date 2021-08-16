<%--
  Created by IntelliJ IDEA.
  User: naten
  Date: 8/14/2021
  Time: 12:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Deposits</title>
    <link rel="stylesheet" href="HomePage.css">
    <link rel="stylesheet" href="Loans.css"/>
</head>
<body>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
        <li><a class="accountInfoTag" href="DepositsServlet">Deposits</a></li>
        <li><a href="TransfersServlet">Transfer by ID</a></li>
        <li><a href="IBANTransfersServlet">Transfer by IBAN</a></li>
        <li><a href="TransactionsHistoryServlet">Transactions History</a></li>
        <li><a style="border:2px solid; color:red" href="LogoutServlet">Log Out</a></li>
    </ul>
    <p class="note">Welcome to TBank</p>
</div>

<div style="margin-left: 340px">
    <c:forEach items="${Deposits}" var="deposit" varStatus="loop">
        <form action="DepositsServlet" method="post">
            <div class="Loan">
                <h1>Deposit Name: ${deposit.depositName}</h1>
                <input name="depositId" value="${deposit.depositID}" hidden="true">
                <span style="font-size: 20px; font-weight: bold">Balance: ${deposit.balance}</span>
                <span> Start Date: ${deposit.startDate}</span><br><br>
                <%--<span> Last Updated: ${deposit.lastUpdate}</span><br><br>--%>
                <span style="font-size: 24px; font-weight: bold">${deposit.cardIdentifier}</span>
                <span>Periods: ${deposit.periods}</span>
                <span> <button name="action" value="delete" type="submit" >Close</button></span>
            </div>
        </form>
    </c:forEach>
</div>


<div style=" position: fixed; margin-left: 80%" class="form">
    <form action="DepositsServlet" method="post">
        <h1>Open Deposit</h1>

        <label>Deposit Account</label><br>
        <select style="width: 95%" name="accountDropdown">
            <c:forEach items="${Accounts}" var="account" varStatus="loop">
                <option value="${loop.index}">
                        ${account}
                </option>
            </c:forEach>
        </select><br><br>

        <label>Deposit Amount</label><br>
        <input type="number" step="0.01" name="amount" placeholder="00.00"><br><br>

        <label>Deposit Period</label><br>
        <input type="number" step="1" name="periods" placeholder="0"><br><br>

        <label>Deposit Name</label><br>
        <input type="text"  name="name" placeholder="Name"><br><br>

        <h3>${success}</h3>
        <h4 style="color: red">${depositError}</h4>
        <button type="submit">Open</button>
    </form>
</div>
</body>
</html>
