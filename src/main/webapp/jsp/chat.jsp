<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="roomId" value="${not empty requestScope.roomId ? requestScope.roomId : param.listing}" />

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Чат</title>
    <link rel="stylesheet" href="${ctx}/assets/css/app.css"/>
    <script src="${ctx}/assets/js/app.js" defer></script>
</head>
<body>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<main class="container">
    <h2>
        <c:choose>
            <c:when test="${not empty roomId}">Чат объявления #<c:out value="${roomId}"/></c:when>
            <c:otherwise>Чат объявления</c:otherwise>
        </c:choose>
    </h2>

    <c:if test="${empty roomId}">
        <div class="alert error mt-2">Не указан идентификатор объявления. Откройте чат со страницы объявления.</div>
        <p class="mt-2"><a class="btn" href="${ctx}/listings">← Назад</a></p>
    </c:if>

    <c:if test="${not empty roomId}">
        <div class="card mt-2">
            <div id="chatLog" class="chat-log">
                <c:forEach var="m" items="${messages}">
                    <div class="msg"><c:out value="${m}"/></div>
                </c:forEach>
            </div>

            <div class="actions mt-2">
                <input id="msgInput" class="input" type="text" placeholder="Сообщение… (Enter — отправить)">
                <button id="sendBtn" class="btn primary" type="button">Отправить</button>
            </div>
        </div>

        <script>
            (function(){
                const log = document.getElementById('chatLog');
                const input = document.getElementById('msgInput');
                const btn = document.getElementById('sendBtn');

                function print(s, cl=''){
                    const el=document.createElement('div'); el.className = 'msg ' + cl; el.textContent = s;
                    log.appendChild(el); log.scrollTop = log.scrollHeight;
                }

                const proto = location.protocol === 'https:' ? 'wss://' : 'ws://';
                const ws = new WebSocket(proto + location.host + '${ctx}/ws/chat?listing=${roomId}');
                ws.addEventListener('close', ()=> print('[соединение закрыто]', 'sys'));
                ws.addEventListener('error', ()=> print('[ошибка соединения]', 'sys'));
                ws.addEventListener('message', ev => print(ev.data));

                function send(){ const t=(input.value||'').trim(); if(!t) return; ws.send(t); input.value=''; }
                btn.addEventListener('click', send);
                input.addEventListener('keydown', e => { if(e.key==='Enter'){ e.preventDefault(); send(); } });
            })();
        </script>

        <p class="mt-2"><a class="btn" href="${ctx}/listings">← Назад к списку</a></p>
    </c:if>
</main>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
</body>
</html>
