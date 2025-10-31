<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${it.title}"/></title>
    <link rel="stylesheet" href="${ctx}/assets/css/app.css"/>
    <script src="${ctx}/assets/js/app.js" defer></script>
</head>
<body>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<main class="container">
    <div class="card">
        <h2><c:out value="${it.title}"/></h2>
        <p class="muted">Тип: <c:out value="${it.type}"/> • Статус: <c:out value="${it.status}"/></p>

        <c:if test="${not empty tags}">
            <p class="mt-2">
                <c:forEach var="t" items="${tags}">
                    <span class="badge">#<c:out value="${t.name}"/></span>
                </c:forEach>
            </p>
        </c:if>

        <c:if test="${not empty photos}">
            <div class="carousel mt-2" id="carousel">
                <c:forEach var="p" items="${photos}" varStatus="st">
                    <img src="${ctx}/photos?id=${p.id}" class="${st.first ? 'active' : ''}" alt="photo ${st.index+1}">
                </c:forEach>
                <button class="nav prev" type="button" aria-label="prev">‹</button>
                <button class="nav next" type="button" aria-label="next">›</button>
            </div>
            <div class="dots" id="dots"></div>

            <script>
                (function(){
                    const root = document.getElementById('carousel');
                    if(!root) return;
                    const imgs = [...root.querySelectorAll('img')];
                    if(imgs.length===0) return;
                    let i = imgs.findIndex(x=>x.classList.contains('active')); if(i<0) i=0;
                    const prev=root.querySelector('.prev'), next=root.querySelector('.next');
                    const dots=document.getElementById('dots');

                    function setActive(k){
                        imgs[i].classList.remove('active'); dots.children[i]?.classList.remove('active');
                        i=k; imgs[i].classList.add('active'); dots.children[i]?.classList.add('active');
                    }
                    imgs.forEach((_,idx)=>{
                        const b=document.createElement('button');
                        if(idx===i) b.classList.add('active');
                        b.addEventListener('click', ()=>setActive(idx));
                        dots.appendChild(b);
                    });
                    prev?.addEventListener('click', ()=>setActive((i-1+imgs.length)%imgs.length));
                    next?.addEventListener('click', ()=>setActive((i+1)%imgs.length));
                })();
            </script>
        </c:if>

        <div class="actions mt-2">
            <a class="btn" href="${ctx}/chat?listing=${it.id}">Открыть чат по объявлению</a>
            <a class="btn" href="${ctx}/listings">← К списку</a>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
</body>
</html>
