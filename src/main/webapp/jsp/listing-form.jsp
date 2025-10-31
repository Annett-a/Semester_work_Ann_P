<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="errors" value="${sessionScope.errors}" />
<c:set var="form"   value="${sessionScope.form}" />
<c:remove var="errors" scope="session"/>
<c:remove var="form" scope="session"/>

<c:set var="isEdit" value="${not empty it}" />

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${isEdit ? 'Редактирование объявления' : 'Создание объявления'}"/></title>
    <link rel="stylesheet" href="${ctx}/assets/css/app.css"/>
    <script src="${ctx}/assets/js/app.js" defer></script>
</head>
<body>

<jsp:include page="/WEB-INF/jsp/layout/header.jsp"/>

<main class="container">
    <h2><c:out value="${isEdit ? 'Редактирование' : 'Создание'}"/> объявления</h2>

    <div class="grid cols-2 mt-2">
        <div class="card">
            <form id="listingForm"
                  class="form"
                  action="${ctx}${isEdit ? '/listings/update' : '/listings/create'}"
                  method="post"
                  enctype="${!isEdit ? 'multipart/form-data' : 'application/x-www-form-urlencoded'}"
                  novalidate>
                <c:if test="${isEdit}">
                    <input type="hidden" name="id" value="${it.id}"/>
                </c:if>

                <label>Название</label>
                <input name="title" required value="${isEdit ? it.title : form.title}"/>
                <c:if test="${not empty errors.title}">
                    <div class="alert error mt-1"><c:out value="${errors.title}"/></div>
                </c:if>

                <div class="grid" style="grid-template-columns:1fr 1fr; gap:12px">
                    <div>
                        <label class="mt-2">Тип</label>
                        <input name="type" required value="${isEdit ? it.type : form.type}"/>
                        <c:if test="${not empty errors.type}">
                            <div class="alert error mt-1"><c:out value="${errors.type}"/></div>
                        </c:if>
                    </div>
                    <div>
                        <label class="mt-2">Статус</label>
                        <input name="status" required value="${isEdit ? it.status : form.status}"/>
                        <c:if test="${not empty errors.status}">
                            <div class="alert error mt-1"><c:out value="${errors.status}"/></div>
                        </c:if>
                    </div>
                </div>

                <h3 class="mt-3">Теги</h3>
                <ul id="tagsWrap" class="clean grid" style="grid-template-columns:repeat(auto-fill,minmax(180px,1fr));gap:8px">
                    <c:forEach var="t" items="${allTags}">
                        <c:set var="checked" value="${isEdit and selectedTagIds != null and selectedTagIds.contains(t.id)}"/>
                        <li class="card" style="padding:8px 10px">
                            <label>
                                <input type="checkbox" name="tagIds" value="${t.id}" <c:if test="${checked}">checked</c:if>>
                                <span style="margin-left:8px"><c:out value="${t.name}"/></span>
                            </label>
                        </li>
                    </c:forEach>
                </ul>

                <c:if test="${!isEdit}">
                    <h3 class="mt-3">Фотографии</h3>
                    <input type="file" name="photos" accept="image/*" multiple>
                    <div class="muted" style="font-size:.9em">Можно выбрать несколько файлов (до 5 МБ каждый).</div>
                </c:if>

                <div class="actions mt-3">
                    <button class="btn primary" type="submit"><c:out value="${isEdit ? 'Сохранить' : 'Создать'}"/></button>
                    <a class="btn" href="${ctx}/listings">Отмена</a>
                </div>
            </form>
        </div>



        <aside class="card">
            <div class="photo preview" id="previewBox">
                <img id="pImg" src="${ctx}/assets/img/logo1.png" alt="нет фото">
                <span id="pPh" class="muted">превью</span>
            </div>

            <p class="mt-1"><b>Название:</b> <span id="pTitle"><c:out value="${isEdit ? it.title : form.title}"/></span></p>
            <p class="muted">Тип: <span id="pType"><c:out value="${isEdit ? it.type : form.type}"/></span> • Статус:
                <span id="pStatus"><c:out value="${isEdit ? it.status : form.status}"/></span></p>
            <div id="pTags" class="mt-1"></div>
        </aside>
    </div>



    <c:if test="${isEdit}">
        <div class="card mt-3">
            <h3>Фотографии</h3>

            <form action="${ctx}/listings/photos/upload" method="post" enctype="multipart/form-data" style="margin-bottom:1rem">
                <input type="hidden" name="listingId" value="${it.id}">
                <input type="file" name="photos" accept="image/*" multiple>
                <button class="btn" type="submit">Загрузить</button>
            </form>

            <c:if test="${not empty photos}">
                <ul class="clean grid" style="grid-template-columns:repeat(auto-fill,minmax(180px,1fr));gap:12px">
                    <c:forEach var="p" items="${photos}">
                        <li class="card center" style="flex-direction:column">
                            <img src="${ctx}/photos?id=${p.id}" alt="${p.fileName}" style="height:120px;max-width:100%;border-radius:12px;display:block">
                            <form action="${ctx}/listings/photos/delete" method="post" style="margin-top:8px">
                                <input type="hidden" name="listingId" value="${it.id}">
                                <input type="hidden" name="photoId" value="${p.id}">
                                <button class="btn danger" type="submit" onclick="return confirm('Удалить фото?')">Удалить</button>
                            </form>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
        </div>
    </c:if>

    <p class="mt-2"><a class="btn" href="${ctx}/listings">← Назад</a></p>
</main>

<jsp:include page="/WEB-INF/jsp/layout/footer.jsp"/>

<script>
    (function(){
        document.getElementById('listingForm')?.addEventListener('submit', e=>{
            const f=e.target; if(!f.checkValidity()){e.preventDefault();return;}
            (f.querySelector('button[type=submit]')||{}).disabled=true;
        });

        const pT=document.getElementById('pTitle');
        const pTy=document.getElementById('pType');
        const pS=document.getElementById('pStatus');
        const pTags=document.getElementById('pTags');
        const wrap=document.getElementById('tagsWrap');

        function refreshText(){
            const title=document.querySelector('[name=title]')?.value||'';
            const type=document.querySelector('[name=type]')?.value||'';
            const status=document.querySelector('[name=status]')?.value||'';
            if(pT) pT.textContent=title.trim()||'Без названия';
            if(pTy) pTy.textContent=type;
            if(pS) pS.textContent=status;
            if(pTags){
                pTags.innerHTML='';
                wrap?.querySelectorAll('input[type=checkbox]:checked')?.forEach(cb=>{
                    const label=cb.closest('label');
                    const chip=document.createElement('span');
                    chip.className='badge';
                    chip.textContent='#'+(label?label.textContent.trim():cb.value);
                    chip.style.marginRight='6px';
                    pTags.appendChild(chip);
                });
            }
        }
        document.addEventListener('input', e=>{
            if(['title','type','status','tagIds'].includes(e.target?.name)) refreshText();
        }, {passive:true});
        refreshText();
    })();
</script>

</body>
</html>
