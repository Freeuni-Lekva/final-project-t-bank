<%--
  Created by IntelliJ IDEA.
  User: naten
  Date: 8/6/2021
  Time: 20:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Transfers</title>
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
        <li><a class="accountInfoTag" href="TransfersServlet">Transfers</a></li>
    </ul>
    <p class="note">Welcome to TBank</p>
</div>

<script type="text/javascript">
    function display(i) {
        if (i.value === "0") {
            document.getElementById("IDForm").style.visibility = 'visible';
            document.getElementById("IBANForm").style.visibility = 'hidden';
        } else {
            document.getElementById("IDForm").style.visibility = 'hidden';
            document.getElementById("IBANForm").style.visibility = 'visible';
        }
    }
</script>

<div style="margin-left: 340px">
    <form>
        <label>
            <input value="0" type="radio" name="transferType" onClick="display(this)">
        </label>By ID
        <br>
        <label>
            <input value="1" type="radio" name="transferType" onClick="display(this)">
        </label>By IBAN
    </form>

    <div style="/*visibility:hidden;*/ position:absolute" id="IDForm">
        <form action="TransfersServlet" method="get">
            <label>Receiver ID</label><br>
            <input type="text" name="receiverID" placeholder="Enter Receiver ID" value="${receiverID}"><br>

            <h4>${errorMessage}</h4>
            <button type="submit">Check ID</button>
            <br><br>

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
            <input type="text" name="amount" placeholder="00.00"><br><br>

            <label for="fromCurrency">From:</label>
            <select name="fromCurrency" id="fromCurrency">
                <option value="GEL">GEL</option>
                <option value="USD">USD</option>
                <option value="EURO">EUR</option>
            </select>

            <label for="toCurrency">To:</label>
            <select name="toCurrency" id="toCurrency">
                <option value="GEL">GEL</option>
                <option value="USD">USD</option>
                <option value="EURO">EUR</option>
            </select>
        </form>

        <form action="TransfersServlet" method="post">
            <button type="submit">Transfer</button>
        </form>
    </div>

    <div style="visibility:hidden; position:absolute" id="IBANForm">
        IBAN
        <%--        <form>--%>
        <%--            <label>Receiver IBAN</label><br>--%>
        <%--            <input type="text" name="ID" placeholder="Enter Receiver IBAN"><br>--%>
        <%--        </form>--%>
    </div>
</div>
</body>
</html>
