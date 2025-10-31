<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx}/assets/css/app.css"/>
<script src="${ctx}/assets/js/app.js" defer></script>

<c:set var="errors" value="${sessionScope.errors}" />
<c:set var="form"   value="${sessionScope.form}" />
<c:remove var="errors" scope="session"/>
<c:remove var="form" scope="session"/>

<main class="container">
    <h2>Регистрация</h2>

    <c:if test="${not empty errors.common}">
        <div class="alert error mt-2"><c:out value="${errors.common}"/></div>
    </c:if>

    <form class="form card mt-3" action="${ctx}/sign-up" method="post" novalidate>
        <label>E-mail</label>
        <input type="email" name="email" required value="<c:out value='${form.email}'/>">
        <c:if test="${not empty errors.email}">
            <div class="alert error mt-1"><c:out value="${errors.email}"/></div>
        </c:if>

        <label class="mt-2">Пароль</label>
        <input type="password" name="password" required>
        <c:if test="${not empty errors.password}">
            <div class="alert error mt-1"><c:out value="${errors.password}"/></div>
        </c:if>

        <label class="mt-2">Имя</label>
        <input type="text" name="fullName" value="<c:out value='${form.fullName}'/>">

        <div class="actions mt-3">
            <button class="btn primary" type="submit">Создать</button>
            <a class="btn ghost" href="${ctx}/sign-in">Войти</a>
        </div>
    </form>
</main>

<script>
    document.querySelector('form')?.addEventListener('submit', e=>{
        const f=e.target; if(!f.checkValidity()){e.preventDefault();return;}
        (f.querySelector('button[type=submit]')||{}).disabled=true;
    });
</script>
