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
    <title>Loan</title>
    <link rel="stylesheet" href="HomePage.css">
</head>
<body>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
        <li><a href="EventsListServlet">View All Events</a></li>
        <li><a class="accountInfoTag" href="LoanServlet">Loan</a></li>
    </ul>
    <p class="note">Welcome to TBank</p>
</div>

<div style="margin-left: 340px">
    <c:forEach items="${Loans}" var="loan" varStatus="loop">
        <div class="Loan">
            <h1>${loan.activeLoan}</h1>
            <h3>${loan.monthlyPayment}</h3>
            <h2>${loan.cardIdentifier}</h2>
        </div>
    </c:forEach>
</div>

<div style=" position: fixed; margin-left: 80%" class="form">
    <form action="LoanServlet" method="post">
        <h1>Take Loan - 15%</h1>

        <label>Loan Account</label><br>
        <select style="width: 95%" name="accountDropdown">
            <c:forEach items="${Accounts}" var="account" varStatus="loop">
                <option value="${loop.index}">
                        ${account}
                </option>
            </c:forEach>
        </select><br><br>

        <label>Loan Amount</label><br>
        <input type="number" step="0.01" name="amount" placeholder="00.00"><br><br>

        <label>Loan Period</label><br>
        <input type="number" step="1" name="period" placeholder="0"><br><br>


        <h2>${loanSuccess}</h2>
        <h4>${loanError}</h4>
        <button type="submit">Get Loan</button>
    </form>
</div>
</body>
</html>
