<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title><spring:message code="meal.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
<section>
    <h3><a href="index.jsp"><spring:message code="app.home"/></a></h3>
    <hr>
    <c:if test="${param.action == 'create'}">
        <h2><spring:message code="meal.add"/></h2>
    </c:if>
    <c:if test="${param.action == 'update'}">
        <h2><spring:message code="meal.update"/></h2>
    </c:if>
    <%--<h2>${param.action == 'create' ? 'Create meal' : 'Edit meal'}</h2>--%>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form method="post" action="${pageContext.request.contextPath}/meals">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt><spring:message code="meal.date"/>:</dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.description"/>:</dt>
            <dd><input type="text" value="${meal.description}" size=40 name="description" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.calories"/>:</dt>
            <dd><input type="number" value="${meal.calories}" name="calories" required></dd>
        </dl>
        <button type="submit"><spring:message code="button.save"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="button.cancel"/></button>
    </form>
</section>
</body>
</html>