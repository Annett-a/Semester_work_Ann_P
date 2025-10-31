<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx}/assets/css/app.css"/>
<script src="${ctx}/assets/js/app.js" defer></script>

<main class="container">
    <h1>Даром&Чиним</h1>
    <p class="muted">Добро пожаловать на платформу объявлений и ремонта.</p>
    <hr/>
    <div class="actions">
        <a class="btn primary" href="${ctx}/sign-up">Sign up</a>
        <a class="btn" href="${ctx}/sign-in">Sign in</a>
        <a class="btn" href="${ctx}/profile">Profile</a>
        <a class="btn" href="${ctx}/listings">Listings</a>
    </div>
</main>
