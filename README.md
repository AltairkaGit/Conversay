# Conversay Backend

## REST API

### Сваггер: http://94.45.223.241:46877/swagger-ui/index.html#/

Используем *-rest-controllers-v2(/api/v2/*)

*-rest-controllers-v1 deprecated, оставлены для обратной совместимости

## WebSockets

### Подключение

1. Подключение по ws://94.45.223.241:46876/ws (также есть поддержка SockJS)
2. При подключении нужно послать либо Authorization header в виде Authorization: token, либо cookie в виде Authorization=token; path=/
3. Далее используется протокол STOMP для обмена сообщениями

В приложении для топиков и действий испольуется единый префикс /app

Пример на TypeScript:

    function connect() {
        document.cookie = 'Authorization=' + token + '; path=/'
        var socket = new WebSocket('ws://localhost:8080/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function(frame) {
            stompClient.subscribe(`destination_1`, function(data: String) {
                ...
            });
            stompClient.subscribe(`destination_2`, function(data: String) {
            ...
            });
            ...
        });
    }

### Chat

Чтобы подписаться на сообщения чата нужно использовать метод subscribe STOMP-клиента по пути /app/queue/chat/{chatId}/messages

Пример на TypeScript:

    stompClient.subscribe(`/app/queue/chat/${chatId}/messages`, function(message: String) {
        showMessageOutput(JSON.parse(message));
    });

Чтобы отправить сообщение в чат нужно использовать метод send STOMP-клиента по пути /app/chat/{chatId}

Пример на TypeScript:

    function sendMessage(message: MessageWebSocketDto) {
        stompClient.send(`/app/chat/${chatId}`, {}, JSON.stringify(message));
    }

Чтобы подписаться на уведомления о новых сообщениях нужно использовать метод subscribe STOMP-клиента по пути /app/queue/chat/messages/new

Формат сообщений: "{chatId}:{messageId}"

Пример на TypeScript:

    stompClient.subescribe(`/app/queue/chat/messages/new`,function(message: String) {
        const [chatId, messageId] = message.split(':')
        //do some stuff...
    });

Чтобы подписаться на уведомления о новых сообщениях нужно использовать метод subscribe STOMP-клиента по пути /app/queue/chat/messages/new

Формат сообщений: "{chatId}:{messageId}"

Пример на TypeScript:

    stompClient.subescribe(`/app/queue/chat/messages/new`,function(message: String) {
        const [chatId, messageId] = message.split(':')
        //do some stuff...
    });

### WebRTC

Чтобы подписаться на получение SDP собеседников нужно использовать метод subscribe STOMP-клиента по пути /app/queue/conversation/{conversationId}

Пример на TypeScript:

    stompClient.subscribe(`/app/queue/conversation/${conversationId}`, function(message: String) {
        //handle SDP from a new participant
    });

Чтобы отправить SDP собеседникам нужно использовать метод send STOMP-клиента по пути /app/conversation/{conversationId}

Пример на TypeScript:

    function sendMessage(message: String) {
        stompClient.send(`/app/conversation/{conversationId}`, {}, JSON.stringify(sdp));
    }
