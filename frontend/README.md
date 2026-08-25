# OpenEx 3.0 — Frontend

React + Vite frontend for the OpenEx 3.0 simulated crypto exchange and AI trading terminal.

## Tech Stack

- React 18 (Vite)
- React Router — client-side routing
- React Context — auth state management
- STOMP over SockJS (`@stomp/stompjs`, `sockjs-client`) — live order book updates
- Fetch API — REST calls to the backend

## Prerequisites

- Node.js 18+
- Backend running locally on `http://localhost:8080` (see backend README)

## Setup

```bash
npm install
cp .env.example .env
```

Edit `.env` and confirm the values match your backend setup:


## Running locally

```bash
npm run dev
```

App runs at `http://localhost:5173`.

## Features implemented

- **Authentication** — login and registration forms, JWT stored client-side, protected routes redirect to `/login` when unauthenticated
- **Wallet dashboard** — displays live account balances fetched from the backend, with loading/error/empty states
- **Trading terminal** — place limit/market buy/sell orders across 10 currency pairs (BTC, ETH, SOL, BNB, XRP, ADA, DOGE, DOT, LINK, MATIC), each order sent with a unique `Idempotency-Key` header
- **Order history** — table of past orders on the trading page, refreshes automatically after a new order is placed
- **Live order book** — WebSocket connection (STOMP/SockJS) subscribed to `/topic/orderbook` for real-time updates

## Known limitations / in progress

- Registration form collects `firstName`/`lastName`, but the backend doesn't yet persist these fields (User entity only has `username`/`password`/`role`) — pending backend update
- Order history table expects a `GET /api/orders` endpoint that doesn't exist yet — UI is built and will render correctly once the endpoint is added
- Currency pair selection on the trading form is currently cosmetic — the backend matching engine only supports a single implicit trading pair (multi-pair routing not yet implemented)
- Coin price charts are planned for a later stage, once the Python market-data service (Flask/Pandas) is available to supply real data

## Project structure