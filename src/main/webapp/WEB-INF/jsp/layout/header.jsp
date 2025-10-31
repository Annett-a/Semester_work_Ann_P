<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:if test="${empty ctx}">
    <c:set var="ctx" value="${pageContext.request.contextPath}" />
</c:if>

<header class="header">
    <div class="container inner">
        <a class="logo" href="${ctx}/" aria-label="Главная">
            <img src="${ctx}/assets/img/logo1.png" alt="" width="28" height="28" loading="lazy"/>
            <span>Даром&Чиним</span>
        </a>

        <nav class="nav">
            <a class="btn" href="${ctx}/listings">Объявления</a>
            <a class="btn" href="${ctx}/listings/my">Мои</a>

            <c:choose>
                <c:when test="${not empty sessionScope.userId}">
                    <a class="btn" href="${ctx}/profile">Профиль</a>
                    <a class="btn danger" href="${ctx}/logout">Выйти</a>
                </c:when>
                <c:otherwise>
                    <a class="btn" href="${ctx}/sign-in">Войти</a>
                    <a class="btn primary" href="${ctx}/sign-up">Регистрация</a>
                </c:otherwise>
            </c:choose>
        </nav>
    </div>
</header>
