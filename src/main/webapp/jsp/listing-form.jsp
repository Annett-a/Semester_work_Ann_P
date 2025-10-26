<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="errors" value="${sessionScope.errors}" />
<c:set var="form"   value="${sessionScope.form}" />
<c:remove var="errors" scope="session"/>
<c:remove var="form" scope="session"/>

<c:set var="isEdit" value="${not empty it}" />

<h2><c:out value="${isEdit ? 'Редактирование' : 'Создание'}"/> объявления</h2>

<form action="${ctx}${isEdit ? '/listings/update' : '/listings/create'}"
      method="post"
      enctype="${!isEdit ? 'multipart/form-data' : 'application/x-www-form-urlencoded'}"
      novalidate>
    <c:if test="${isEdit}">
        <input type="hidden" name="id" value="${it.id}"/>
    </c:if>

    <label>Название:
        <input name="title" required value="${isEdit ? it.title : form.title}"/>
    </label>
    <c:if test="${not empty errors.title}"><span style="color:red">${errors.title}</span></c:if>
    <br/>

    <label>Тип:
        <input name="type" required value="${isEdit ? it.type : form.type}"/>
    </label>
    <c:if test="${not empty errors.type}"><span style="color:red">${errors.type}</span></c:if>
    <br/>

    <label>Статус:
        <input name="status" required value="${isEdit ? it.status : form.status}"/>
    </label>
    <c:if test="${not empty errors.status}"><span style="color:red">${errors.status}</span></c:if>
    <br/>

    <h3>Теги</h3>
    <ul style="list-style:none;padding-left:0">
        <c:forEach var="t" items="${allTags}">
            <c:set var="checked" value="${isEdit and selectedTagIds != null and selectedTagIds.contains(t.id)}"/>
            <li>
                <label>
                    <input type="checkbox" name="tagIds" value="${t.id}" <c:if test="${checked}">checked</c:if>>
                        ${t.name}
                </label>
            </li>
        </c:forEach>
    </ul>

    <!-- Загрузка фото сразу при создании -->
    <c:if test="${!isEdit}">
        <h3>Фотографии</h3>
        <input type="file" name="photos" accept="image/*" multiple>
        <div style="font-size:.9em;color:#666">Можно выбрать несколько файлов (до 5 МБ каждый).</div>
    </c:if>

    <button type="submit"><c:out value="${isEdit ? 'Сохранить' : 'Создать'}"/></button>
</form>

<!-- Блок фотографий и загрузки при редактировании (остаётся как был) -->
<c:if test="${isEdit}">
    <hr/>
    <h3>Фотографии</h3>

    <form action="${ctx}/listings/photos/upload" method="post" enctype="multipart/form-data" style="margin-bottom:1rem">
        <input type="hidden" name="listingId" value="${it.id}">
        <input type="file" name="photos" accept="image/*" multiple>
        <button type="submit">Загрузить</button>
    </form>

    <c:if test="${not empty photos}">
        <ul style="list-style:none;padding-left:0;display:flex;flex-wrap:wrap;gap:8px">
            <c:forEach var="p" items="${photos}">
                <li style="border:1px solid #ddd;padding:6px">
                    <img src="${ctx}/photos?id=${p.id}" alt="${p.fileName}" style="height:80px;display:block">
                    <form action="${ctx}/listings/photos/delete" method="post" style="text-align:center;margin-top:4px">
                        <input type="hidden" name="listingId" value="${it.id}">
                        <input type="hidden" name="photoId" value="${p.id}">
                        <button type="submit">Удалить</button>
                    </form>
                </li>
            </c:forEach>
        </ul>
    </c:if>
</c:if>

<p><a href="${ctx}/listings">Назад</a></p>

<script>
    document.querySelectorAll('form').forEach(f=>{
        f.addEventListener('submit', e=>{
            if(!f.checkValidity()){ e.preventDefault(); return; }
            const btn=f.querySelector('button[type=submit],input[type=submit]');
            if(btn) btn.disabled=true;
        });
    });
</script>
