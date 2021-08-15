<%@ page import="com.example.T_Bank.Storage.CrowdFundingEvent" %>
<%@ page import="java.util.ArrayList" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Transaction History</title>
    <link rel="stylesheet" href="HomePage.css">
    <link rel="stylesheet" href="TransactionHistory.css">
</head>
<body class="body">
<div class="navigation">
    <ul class="list">
        <li><a href="AccountServlet">Account Information </a></li>
        <li><a href="CardCreationServlet">Add or Remove a Card</a></li>
        <li><a href="CardsListServlet">View All Cards</a></li>
        <li><a href="CurrencyServlet">Currency Exchange</a></li>
        <li><a href="CrowdFundingServlet"> Crowd Funding - My Events </a> </li>
        <li>Deposits and Loans</li>
        <li><a href="TransfersServlet">Transfer by ID</a></li>
        <li><a href="IBANTransfersServlet">Transfer by IBAN</a></li>
        <li><a href="EventsListServlet">View All Events</a></li>
        <li><a href="TransactionsHistoryServlet">View Transaction History</a></li>
        <li><a style="border:2px solid; color:red" href="LogoutServlet">Log Out</a></li>
    </ul>
    <p class="note">Welcome to TBank</p>
</div>

<div class="upper">
    <div class="tr">
        <form class="content" action="TransactionsHistoryServlet" method="get">
            <div class="checkboxes">
                <div class="content_elemets">
                    <%if (request.getAttribute("incomeCheckBox").equals("true")) { %>
                    <input type="checkbox" name="checkbox" value="checkboxincome" checked>
                    <%} else {%>
                    <input type="checkbox" name="checkbox" value="checkboxincome">
                    <%}%>
                    <label style="color:black; font-weight: bold">Income</label>
                </div>
                <div class="content_elements">
                    <%if (request.getAttribute("expenseCheckBox").equals("true")) { %>
                    <input type="checkbox" name="checkbox" value="checkboxexpense" checked>
                    <%} else {%>
                    <input type="checkbox" name="checkbox" value="checkboxexpense">
                    <%}%>
                    <label style="color:black; font-weight: bold">Expense</label>
                </div>
                <div class="content_elements">
                    <%if (request.getAttribute("currencyCheckBox").equals("true")) { %>
                    <input type="checkbox" name="checkbox" value="checkboxcurrency" checked>
                    <%} else {%>
                    <input type="checkbox" name="checkbox" value="checkboxcurrency">
                    <%}%>
                    <label style="color:black; font-weight: bold">Currency Exchange</label>
                </div>
            </div>
            <br>
            <button style="margin-left:10px" type="submit" name="filter">Filter</button>
            <br>
        </form>

        <div class = "transactions">
            <c:forEach items="${income}" var="income_transaction" varStatus="loop">
                <div class="transaction_info">
                    <div class="date_user">
                        <span style="margin: 5px">${income_transaction.logDate}</span>
                        <span style="margin: 5px">${tBankDAO.getAccountUsername(income_transaction.senderAccountId)}</span>
                    </div>
                    <div class="amount">
                        <span style="margin: 5px">${income_transaction.amount}</span>
                        <span style="margin: 5px">${tBankDAO.getCurrencyName(income_transaction.currencyId)}</span>
                    </div>
                </div>
            </c:forEach>
            <c:forEach items="${expense}" var="expense_transaction" varStatus="loop">
                <div class="transaction_info">
                    <div class="date_user">
                        <span style="margin: 5px">${expense_transaction.logDate}</span>
                        <span style="margin: 5px">${tBankDAO.getAccountUsername(expense_transaction.receiverAccountId)}</span>
                    </div>
                    <div class="amount">
                        <span style="margin: 5px">-${expense_transaction.amount}</span>
                        <span style="margin: 5px">${tBankDAO.getCurrencyName(expense_transaction.currencyId)}</span>
                    </div>
                </div>
            </c:forEach>
            <c:forEach items="${currency}" var="currency_transaction" varStatus="loop">
                <div class="transaction_info">
                    <div class="date_user">
                        <span style="margin: 5px">${currency_transaction.logDate}</span>
                        <span style="margin: 5px">${tBankDAO.getAccountUsername(currency_transaction.senderAccountId)}</span>
                    </div>
                    <div class="amount">
                        <span style="margin: 5px">${currency_transaction.amount}</span>
                        <span style="margin: 5px">${tBankDAO.getCurrencyName(currency_transaction.currencyId)}</span>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

</body>
</html>