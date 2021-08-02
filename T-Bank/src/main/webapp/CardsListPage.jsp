<%--
  Created by IntelliJ IDEA.
  User: naten
  Date: 8/2/2021
  Time: 23:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cards</title>
    <link rel="stylesheet" href="Cards.css"/>
</head>
<body>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:forEach items="${cards}" var="card" varStatus="loop">
    <div class="card">
        <h1>${card.cardName}</h1>
        <h2>${card.gelBalance} GEL</h2>
        <span style="margin-right: 5px">${card.usdBalance} USD</span>
        <span>${card.euroBalance} Euro</span>
        <h2>${card.cardIdentifier}</h2>
    </div>
</c:forEach>
<br class="clear"/>

</body>
</html>
