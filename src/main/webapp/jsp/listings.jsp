<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<h2>Объявления</h2>
<p><a href="${ctx}/listings/new">Создать</a> | <a href="${ctx}/listings/my">Мои</a></p>

<ul>
    <c:forEach var="x" items="${items}">
        <li>
            <b>${x.title}</b> [${x.type}] — ${x.status}
            <a href="${ctx}/listings/view?id=${x.id}">открыть</a>
            <a href="${ctx}/listings/edit?id=${x.id}">править</a>
            <form action="${ctx}/listings/delete" method="post" style="display:inline" onsubmit="return confirm('Удалить?')">
                <input type="hidden" name="id" value="${x.id}">
                <button type="submit">удалить</button>
            </form>
        </li>
    </c:forEach>
</ul>
