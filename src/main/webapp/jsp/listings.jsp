<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="scope" value="${requestScope.scope != null ? requestScope.scope : 'all'}" />

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${scope == 'my' ? 'Мои объявления' : 'Объявления'}"/></title>
    <link rel="stylesheet" href="${ctx}/assets/css/app.css"/>
    <script src="${ctx}/assets/js/app.js" defer></script>
</head>
<body>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<main class="container">
    <h2>
        <c:choose>
            <c:when test="${scope == 'my'}">Мои объявления</c:when>
            <c:otherwise>Объявления</c:otherwise>
        </c:choose>
    </h2>

    <div class="toolbar card mt-2">
        <input id="listingSearch" class="input" type="search" placeholder="Поиск по названию…">
        <div class="actions">
            <a class="btn primary" href="${ctx}/listings/new">Создать</a>
            <a class="btn" href="${ctx}/listings/my">Мои</a>
            <a class="btn" href="${ctx}/listings">Все</a>
        </div>
    </div>

    <ul id="listingGrid" class="clean listings mt-2">
        <c:forEach var="x" items="${items}">
            <li class="card listing" data-title="${x.title}">
                <div class="card-cover">
                    <img src="${ctx}/assets/img/logo1.png" alt="нет фото">
                </div>
                <div class="title"><c:out value="${x.title}"/></div>
                <div class="meta">
                    <c:if test="${not empty x.type}">Тип: <c:out value="${x.type}"/></c:if>
                    <c:if test="${not empty x.status}">&nbsp;• Статус: <c:out value="${x.status}"/></c:if>
                </div>
                <div class="actions mt-1">
                    <a class="btn" href="${ctx}/listings/view?id=${x.id}">открыть</a>
                    <c:if test="${sessionScope.userId == x.authorId}">
                        <a class="btn" href="${ctx}/listings/edit?id=${x.id}">править</a>
                        <form action="${ctx}/listings/delete" method="post" style="display:inline" onsubmit="return confirm('Удалить?')">
                            <input type="hidden" name="id" value="${x.id}">
                            <button class="btn danger" type="submit">удалить</button>
                        </form>
                    </c:if>
                </div>
            </li>
        </c:forEach>
    </ul>
</main>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
</body>
</html>
