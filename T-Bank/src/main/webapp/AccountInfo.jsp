<%--
  Created by IntelliJ IDEA.
  User: uruma
  Date: 8/2/2021
  Time: 9:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Account Information</title>
  <link rel="stylesheet" href="HomePage.css">
</head>
<body>


<div class="navigation">
  <ul>
    <br>
    <li> <a class="accountInfoTag" href="AccountServlet">Account Information </a> </li>
      <li><a href="CardCreationServlet">Add or Remove a Card</a></li>
      <li><a href="CardsListServlet">View All Cards</a></li>
      <li><a href="CurrencyServlet">Currency Exchange</a></li>
      <li><a href="CrowdFundingServlet"> Crowd Funding - My Events </a> </li>
      <li>Deposits and Loans</li>
      <li><a href="TransfersServlet">Transfer by ID</a></li>
      <li><a href="IBANTransfersServlet">Transfer by IBAN</a></li>
    <li><a href="EventsListServlet">View All Events</a></li>
      <li><a style="border:2px solid; color:red" href="LogoutServlet">Log Out</a></li>
  </ul>
  <p class="note">Welcome to TBank</p>
</div>

<div class="containerAccountInfo">
  <table>
    <tr>
      <th>User Name</th>
      <td>  ${UserName} </td>
    </tr>

    <tr>
      <th>First Name</th>
      <td> ${FirstName} </td>

    </tr>
      <th>Last Name</th>
      <td> ${LastName} </td>

    <tr>
      <th>Personal ID </th>
      <td>  ${PersonalID} </td>

    </tr>

    <tr>
      <th>Account ID</th>
      <td>  ${AccountID} </td>

    </tr>
    <tr>
      <th>Password</th>
      <td> <p style=color:sandybrown; onmouseover="this.style.color='black';" onmouseout="this.style.color='sandybrown';" >${password}</p>  </td>
    </tr>

  </table>
</div>

</body>
</html>
