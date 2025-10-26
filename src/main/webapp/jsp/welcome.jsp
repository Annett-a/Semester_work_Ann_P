<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<h1>Даром&Чиним</h1>
<p>
    <a href="${ctx}/sign-in">Sign in</a> |
    <a href="${ctx}/sign-up">Sign up</a> |
    <a href="${ctx}/profile">Profile</a> |
    <a href="${ctx}/listings">Listings</a>
</p>
<hr/>
<p>Добро пожаловать на платформу объявлений и ремонта.</p>
