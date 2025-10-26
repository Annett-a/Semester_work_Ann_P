<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<h2>Профиль</h2>
<p>Вы вошли как: <b>${sessionScope.email}</b></p>
<p><a href="${ctx}/logout">Выйти</a> | <a href="${ctx}/listings">Список объявлений</a></p>
