<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %><%--
  Created by IntelliJ IDEA.
  User: sulfu
  Date: 04.06.2023
  Time: 14:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://example.com/functions" prefix="f" %>
<html>
<head>
    <title>Title</title>
    <style>
        .letters_true {
            color: red;
        }

        .letters_false {
            color: green;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
<table border="1">
    <thead>
        <tr>
            <td>Date</td>
            <td>Description</td>
            <td>Calories</td>
        </tr>
    </thead>
    <c:forEach var="meal" items="${list}">
        <%-- Добавляем в jsp указание на бин MealTo --%>
        <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.model.MealTo"/>
        <%-- Используем тернарный оператор вместо дублирования кода с if-else --%>
            <tr class="${meal.excess ? 'letters_true' : 'letters_false'}">
                <td>
                    <%-- Вызываем toString(), который отображает нашу дату согласно шаблону --%>
                    <%=TimeUtil.toString(meal.getDateTime())%>
                </td>
                <td>
                    <c:out value="${meal.description}"/>
                </td>
                <td>
                    <c:out value="${meal.calories}"/>
                </td>
            </tr>
    </c:forEach>
</table>
</body>
</html>
