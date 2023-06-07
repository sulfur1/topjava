<%--
  Created by IntelliJ IDEA.
  User: sulfu
  Date: 05.06.2023
  Time: 18:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <link rel="stylesheet" type="text/css" href="js/jquery.datetimepicker.css"/>
    <script type="text/javascript" src="js/jquery_1.7_jquery.js"></script>
    <script type="text/javascript" src="js/jquery-datetimepicker_2.5.20_jquery.datetimepicker.full.js"></script>
    <title>Add Meal</title>
</head>
<h3><a href="index.html">Home</a></h3>

<h2>Add Meal</h2>

<body>
<form method="POST" action='meals' name="frmAddMeal">

    DateTime: <input id="datetimepicker"
        type="text" name="dateTime"/> <br />
    Description: <input
        type="text" name="description"/> <br />
    Calories: <input
        type="text" name="calories"/> <br />
    <input type="submit" value="Save" />
    <button onclick="window.history.back()" type="button">Cancel</button>

</form>
<script>
    jQuery('#datetimepicker').datetimepicker({
        format:'d/m/Y H:i',
        inline:false,
        closeOnWithoutClick :false,
        lang:'ru'
    });
</script>
</body>
</html>
