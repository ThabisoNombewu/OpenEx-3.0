import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const WS_URL = import.meta.env.VITE_WS_URL || 'http://localhost:8080/api/ws';

let stompClient = null;

export function connectOrderBook(onMessage, onConnect, onError) {
  stompClient = new Client({
    webSocketFactory: () => new SockJS(WS_URL),
    reconnectDelay: 5000,
    onConnect: () => {
      stompClient.subscribe('/topic/orderbook', (message) => {
        try {
          const data = JSON.parse(message.body);
          onMessage(data);
        } catch (err) {
          console.error('Failed to parse order book message', err);
        }
      });
      if (onConnect) onConnect();
    },
    onStompError: (frame) => {
      console.error('STOMP error', frame);
      if (onError) onError(frame);
    },
    onWebSocketError: (event) => {
      console.error('WebSocket error', event);
      if (onError) onError(event);
    },
  });

  stompClient.activate();
}

export function disconnectOrderBook() {
  if (stompClient) {
    stompClient.deactivate();
    stompClient = null;
  }
}