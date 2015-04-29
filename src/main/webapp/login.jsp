<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Stormpath Example - Login</title>
    </head>
    <body>
        <h1>Login</h1>
        <div>
            <form name="loginform" action="" method="POST" accept-charset="UTF-8" role="form">
                <fieldset>
                    <div class="form-group">
                        <input class="form-control" placeholder="Username or Email" name="username" type="text">
                    </div>
                    <div class="form-group">
                        <input class="form-control" placeholder="Password" name="password" type="password" value="">
                    </div>
                    <div class="checkbox">
                        <label>
                            <input name="rememberMe" type="checkbox" value="true"> Remember Me
                        </label>
                    </div>
                    <input type="submit" value="Login">
                </fieldset>
            </form>
        </div>
        <div>
            <c:out value="${shiroLoginFailure}"/>
        </div>
    </body>
</html>
