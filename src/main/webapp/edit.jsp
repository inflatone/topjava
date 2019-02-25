<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit meal</title>
</head>
<body>
<section>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form method="post" action="meals" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt>Дата:</dt>
            <dd><input type="text" name="date" size="50" value="<%=TimeUtil.format(meal.getDate())%>"></dd>
        </dl>
        <dl>
            <dt>Время:</dt>
            <dd><input type="text" name="time" size="50" value="<%=TimeUtil.format(meal.getTime())%>"></dd>
        </dl>
        <dl>
            <dt>Описание:</dt>
            <dd><input type="text" name="description" size="50" value="${meal.description}"></dd>
        </dl>
        <dl>
            <dt>Калории:</dt>
            <dd><input type="number" name="calories" size="50" value="${meal.calories}"></dd>
        </dl>
        <button type="submit">Save</button>
        <button onclick="window.history.back()">Back</button>
    </form>
</section>
</body>
</html>