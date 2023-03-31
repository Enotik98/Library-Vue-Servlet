<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
<%--    <%@include file="view/Registration.vue" %>--%>
</head>
<body>
<div>
    <form action="login" method="post">
        <input type="text" required placeholder="username" name="username">
        <input type="text" required placeholder="password" name="password">
        <input type="submit" name="submit">
    </form>
</div>
<div>
    <%@ include file="dist/index.html" %>
</div>
<div>
<%--    <%@ include file="view/addBook.html" %>--%>
</div>

<%--<h1><%= "Hello World!" %>--%>
<%--</h1>--%>
<%--<br/>--%>
<%--<a href="hello-servlet">Hello Servlet</a>--%>
</body>
</html>