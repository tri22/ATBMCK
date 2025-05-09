<%--
  Created by IntelliJ IDEA.
  User: nghia
  Date: 3/25/2025
  Time: 2:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<% String userId = request.getParameter("user_id"); %>
<% if (userId == null || userId.isEmpty()) { %>
<p style="color: red;">Error: Missing user_id parameter.</p>
<% } %>

<form action="verify-otp" method="post">
    <input type="hidden" name="user_id" value="<%= userId %>">
    OTP: <input type="text" name="otp" required><br>
    <button type="submit">Verify OTP</button>
</form>
</body>
</html>
