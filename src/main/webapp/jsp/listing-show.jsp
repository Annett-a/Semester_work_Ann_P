<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<h2>${it.title}</h2>
<p>Тип: ${it.type}</p>
<p>Статус: ${it.status}</p>

<c:if test="${not empty tags}">
    <p>Теги:
        <c:forEach var="t" items="${tags}">
            <span>#${t.name}</span>
        </c:forEach>
    </p>
</c:if>

<!-- КАРУСЕЛЬ ФОТО -->
<c:if test="${not empty photos}">
    <style>
        .carousel{position:relative;max-width:640px}
        .carousel img{width:100%;height:auto;display:none}
        .carousel img.active{display:block}
        .carousel .nav{position:absolute;top:50%;transform:translateY(-50%);background:#0008;color:#fff;border:none;padding:.4rem .6rem;cursor:pointer}
        .carousel .prev{left:6px}.carousel .next{right:6px}
        .dots{margin:.5rem 0}
        .dots button{border:none;background:#ccc;width:8px;height:8px;border-radius:50%;margin:0 3px;cursor:pointer}
        .dots button.active{background:#333}
    </style>
    <div class="carousel" id="carousel">
        <c:forEach var="p" items="${photos}" varStatus="st">
            <img src="${ctx}/photos?id=${p.id}" class="${st.first ? 'active' : ''}" alt="${p.fileName}">
        </c:forEach>
        <button class="nav prev" type="button">&#10094;</button>
        <button class="nav next" type="button">&#10095;</button>
    </div>
    <div class="dots" id="dots"></div>

    <script>
        (function(){
            const box=document.getElementById('carousel');
            if(!box) return;
            const imgs=[...box.querySelectorAll('img')];
            const prev=box.querySelector('.prev'), next=box.querySelector('.next');
            const dots=document.getElementById('dots');
            let i=0;
            function show(n){
                i=(n+imgs.length)%imgs.length;
                imgs.forEach((im,idx)=>im.classList.toggle('active', idx===i));
                dots.querySelectorAll('button').forEach((d,idx)=>d.classList.toggle('active', idx===i));
            }
            imgs.forEach((_,idx)=>{
                const b=document.createElement('button');
                b.addEventListener('click',()=>show(idx));
                dots.appendChild(b);
            });
            prev.addEventListener('click',()=>show(i-1));
            next.addEventListener('click',()=>show(i+1));
            if(imgs.length){ show(0); } else { prev.style.display=next.style.display='none'; }
        })();
    </script>
</c:if>

<!-- ССЫЛКА НА ЧАТ К ОБЪЯВЛЕНИЮ -->
<p style="margin-top:1rem">
    <a href="${ctx}/chat?listing=${it.id}">Открыть чат по объявлению</a>
</p>

<p>
    <a href="${ctx}/listings/edit?id=${it.id}">Править</a> |
    <a href="${ctx}/listings">Назад</a>
</p>
