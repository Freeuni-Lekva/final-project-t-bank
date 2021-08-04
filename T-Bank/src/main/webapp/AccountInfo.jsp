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
      <li>Crowd Funding</li>
      <li>Deposits and Loans</li>
      <li>Money Transfers</li>
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
      <td> ${password} </td>
    </tr>

  </table>
</div>

</body>
</html>
