<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:if test="${empty ctx}">
    <c:set var="ctx" value="${pageContext.request.contextPath}" />
</c:if>

<footer class="footer">
    <div class="container">
        <div>© 2025 Даром&Чиним</div>
        <div class="muted" style="margin-top:6px">
            Annett-a •
            <a href="${ctx}/listings">Объявления</a> •
            <a href="${ctx}/profile">Профиль</a>
        </div>
    </div>
</footer>
