<%--
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
    <title>Meals</title>
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

<p><a href="meals?action=create">Add Meal</a></p>

<table border="1">
    <tr>
        <td>Date</td>
        <td>Description</td>
        <td>Calories</td>
    </tr>
    <c:forEach var="meal" items="${meals}">
        <c:if test="${meal.excess eq true}">
            <tr class="letters_true">
                <td>
                        ${f:formatLocalDateTime(meal.dateTime, 'dd-MM-yyyy HH:mm')}
                </td>
                <td>
                    <c:out value="${meal.description}"/>
                </td>
                <td>
                    <c:out value="${meal.calories}"/>
                </td>
                <td>
                    <a href="meals?action=edit&mealId=<c:out value="${meal.id}"/>">Update</a>
                </td>
                <td>
                    <a href="meals?action=delete&mealId=<c:out value="${meal.id}"/>">Delete</a>
                </td>
            </tr>
        </c:if>
        <c:if test="${meal.excess eq false}">
            <tr class="letters_false">
                <td>
                        ${f:formatLocalDateTime(meal.dateTime, 'dd.MM.yyyy')}
                </td>
                <td>
                    <c:out value="${meal.description}"/>
                </td>
                <td>
                    <c:out value="${meal.calories}"/>
                </td>
                <td>
                    <a href="meals?action=edit&mealId=<c:out value="${meal.id}"/>">Update</a>
                </td>
                <td>
                    <a href="meals?action=delete&mealId=<c:out value="${meal.id}"/>">Delete</a>
                </td>
            </tr>
        </c:if>
    </c:forEach>
</table>
</body>
</html>
