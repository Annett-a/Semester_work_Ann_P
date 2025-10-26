<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="roomId" value="${requestScope.roomId}" />

<h2>Чат объявления #${roomId}</h2>

<ul id="chatList" style="list-style:none;padding-left:0">
    <c:forEach var="m" items="${messages}">
        <li>${m}</li>
    </c:forEach>
</ul>

<input id="msgInput" type="text" placeholder="Сообщение..." style="width:320px" />
<button id="sendBtn" type="button">Отправить</button>

<script>
    (function(){
        const ul = document.getElementById('chatList');
        const input = document.getElementById('msgInput');
        const btn = document.getElementById('sendBtn');

        function add(text){
            const li=document.createElement('li');
            li.textContent=text;
            ul.appendChild(li);
            ul.scrollTop=ul.scrollHeight;
        }

        const proto = location.protocol === 'https:' ? 'wss://' : 'ws://';
        const ws = new WebSocket(proto + location.host + '${ctx}/ws/chat?listing=${roomId}');

        ws.onmessage = e => add(e.data);
        ws.onopen    = _ => add('Подключено к комнате #' + '${roomId}');
        ws.onclose   = _ => add('Соединение закрыто');

        function send(){
            const t = input.value.trim();
            if(!t) return;
            ws.send(t);
            input.value='';
            input.focus();
        }
        btn.addEventListener('click', send);
        input.addEventListener('keydown', e => { if(e.key==='Enter') send(); });
    })();
</script>

<p><a href="${ctx}/listings/view?id=${roomId}">← Назад к объявлению</a></p>
