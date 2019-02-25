<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals</title>
    <style>
        .normal {
            color: green;
        }

        .exceeded {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <a href="meals?action=add"><img src="img/add.png" alt="Add"> </a>
    <table cellpadding="8" cellspacing="0">
        <tr>
            <th>Дата</th>
            <th>Время</th>
            <th>Описание</th>
            <th>Калории</th>
            <th></th>
            <th></th>
        </tr>
        <jsp:useBean id="meals" scope="request" type="java.util.List"/>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealWithExceed"/>
            <tr class="${meal.exceed ? "exceeded" : "normal"}">
                <td><%=TimeUtil.format(meal.getDate())%>
                </td>
                <td><%=TimeUtil.format(meal.getTime())%>
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?id=${meal.id}&action=edit"><img src="img/pencil.png" alt="Edit"></a></td>
                <td><a href="meals?id=${meal.id}&action=remove"><img src="img/delete.png" alt="Delete"></a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>