<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx}/assets/css/app.css"/>
<script src="${ctx}/assets/js/app.js" defer></script>

<html>
<head><title>Cookie demo</title></head>
<body style="background-color: ${requestScope.color}">
<main class="container">
    <h1>Hello world from java</h1>

    <form class="form card mt-2" action="/cookie" method="post">
        <label>Цвет</label>
        <input type="text" name="color"/>
        <div class="actions">
            <input class="btn primary" type="submit" value="Save">
        </div>
    </form>
</main>
</body>
</html>
