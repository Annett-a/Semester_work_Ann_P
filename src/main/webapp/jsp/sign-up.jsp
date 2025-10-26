<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="errors" value="${sessionScope.errors}" />
<c:set var="form"   value="${sessionScope.form}" />
<c:remove var="errors" scope="session"/>
<c:remove var="form" scope="session"/>

<h2>Регистрация</h2>
<c:if test="${not empty errors.common}"><p style="color:red">${errors.common}</p></c:if>

<form action="${ctx}/sign-up" method="post" novalidate>
    <label>E-mail: <input type="email" name="email" required value="${form.email}"></label>
    <c:if test="${not empty errors.email}"><span style="color:red">${errors.email}</span></c:if>
    <br/>
    <label>Пароль: <input type="password" name="password" required></label>
    <c:if test="${not empty errors.password}"><span style="color:red">${errors.password}</span></c:if>
    <br/>
    <label>Имя: <input type="text" name="fullName" value="${form.fullName}"></label>
    <br/>
    <button type="submit">Создать</button>
</form>

<script>
    document.querySelector('form')?.addEventListener('submit', e=>{
        const f=e.target; if(!f.checkValidity()){e.preventDefault();return;}
        (f.querySelector('button[type=submit]')||{}).disabled=true;
    });
</script>
