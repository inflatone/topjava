<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Add meal</title>
</head>
<body>
<section>
    <form method="post" action="meals" enctype="application/x-www-form-urlencoded">
        <jsp:useBean id="date" type="java.time.LocalDateTime" scope="request"/>
        <dl>
            <dt>Дата:</dt>
            <dd><input type="datetime-local" name="date" size="50" value="${date}"></dd>
        </dl>
        <dl>
            <dt>Описание:</dt>
            <dd><input type="text" name="description" size="50" value="Описание"></dd>
        </dl>
        <dl>
            <dt>Калории:</dt>
            <dd><input type="number" name="calories" size="50" value="0"></dd>
        </dl>
        <button type="submit">Save</button>
        <button onclick="window.history.back()">Back</button>
    </form>
</section>
</body>
</html>