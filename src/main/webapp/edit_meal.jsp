<%--
  Created by IntelliJ IDEA.
  User: sulfu
  Date: 06.06.2023
  Time: 19:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://example.com/functions" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <link rel="stylesheet" type="text/css" href="js/jquery.datetimepicker.css"/>
    <script type="text/javascript" src="js/jquery_1.7_jquery.js"></script>
    <script type="text/javascript" src="js/jquery-datetimepicker_2.5.20_jquery.datetimepicker.full.js"></script>
    <title>Edit</title>

</head>
<h3><a href="index.html">Home</a></h3>

<h2>Edit</h2>

<body>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form method="POST" action="meals" name="frmEditMeal">
        <input type="hidden" name="mealId" value="${meal.id}">

        DateTime: <input id="datetimepicker"
            type="text" name="dateTime"
            value="${f:formatLocalDateTime(meal.dateTime, 'dd/MM/yyyy HH:mm')}" /> <br />
        Description: <input
            type="text" name="description"
            value="<c:out value="${meal.description}" />" /> <br />
        Calories: <input
            type="text" name="calories"
            value="<c:out value="${meal.calories}" />" /> <br />
        <input type="submit" value="Save" />
        <button onclick="window.history.back()" type="button">Cancel</button>

    </form>
<script>
    jQuery('#datetimepicker').datetimepicker({
        format:'m/d/Y H:i',
        inline:false,
        closeOnWithoutClick :false,
        lang:'ru'
    });
</script>
</body>
</html>