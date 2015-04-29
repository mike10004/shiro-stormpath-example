<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Stormpath Example - Main</title>
    </head>
    <body data-user="<c:out value="${userJson}"/>">
        <h1>Stormpath Example</h1>
        <div>
            Hello, <c:out value="${account['givenName']}"/>.
        </div>
        <div>
            <pre><c:out value="${userJson}"/>
            </pre>
        </div>
        <a href="logout">Sign out</a>
        <script>
            var userJson = document.body.attributes['data-user'];
            var user = JSON.parse(userJson);
            console.log(user);
        </script>
    </body>
</html>
