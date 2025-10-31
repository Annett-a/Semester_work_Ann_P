<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx}/assets/css/app.css"/>
<script src="${ctx}/assets/js/app.js" defer></script>

<main class="container">
    <div class="card">
        <h2>Профиль</h2>
        <p>Вы вошли как: <b><c:out value="${sessionScope.email}"/></b></p>
        <div class="actions mt-2">
            <a class="btn" href="${ctx}/listings">Список объявлений</a>
            <a class="btn danger" href="${ctx}/logout">Выйти</a>
        </div>
    </div>
</main>
