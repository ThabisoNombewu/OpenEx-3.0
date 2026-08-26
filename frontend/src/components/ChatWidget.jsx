import { useState } from 'react';

function ChatWidget() {
  const [isOpen, setIsOpen] = useState(false);
  const [messages, setMessages] = useState([
    { role: 'assistant', text: 'Hi! I can help answer questions about your trading and balances once I\'m fully connected.' },
  ]);
  const [input, setInput] = useState('');

  const handleSend = (e) => {
    e.preventDefault();
    if (!input.trim()) return;

    setMessages((prev) => [
      ...prev,
      { role: 'user', text: input },
      { role: 'assistant', text: 'AI assistant is not connected yet — coming soon.' },
    ]);
    setInput('');
  };

  return (
    <div style={{ position: 'fixed', bottom: '24px', right: '24px', zIndex: 50 }}>
      {isOpen && (
        <div
          style={{
            width: '320px',
            height: '420px',
            background: '#1e293b',
            border: '1px solid #334155',
            borderRadius: '12px',
            display: 'flex',
            flexDirection: 'column',
            marginBottom: '12px',
            boxShadow: '0 8px 24px rgba(0,0,0,0.4)',
          }}
        >
          <div
            style={{
              padding: '14px 16px',
              borderBottom: '1px solid #334155',
              color: '#fff',
              fontWeight: 700,
              fontSize: '14px',
            }}
          >
            AI Trading Assistant
          </div>

          <div style={{ flex: 1, overflowY: 'auto', padding: '12px 16px' }}>
            {messages.map((msg, i) => (
              <div
                key={i}
                style={{
                  marginBottom: '10px',
                  textAlign: msg.role === 'user' ? 'right' : 'left',
                }}
              >
                <span
                  style={{
                    display: 'inline-block',
                    padding: '8px 12px',
                    borderRadius: '10px',
                    background: msg.role === 'user' ? '#6366f1' : '#334155',
                    color: '#fff',
                    fontSize: '13px',
                    maxWidth: '85%',
                  }}
                >
                  {msg.text}
                </span>
              </div>
            ))}
          </div>

          <form
            onSubmit={handleSend}
            style={{ display: 'flex', borderTop: '1px solid #334155', padding: '10px' }}
          >
            <input
              type="text"
              value={input}
              onChange={(e) => setInput(e.target.value)}
              placeholder="Ask something..."
              style={{
                flex: 1,
                padding: '8px 10px',
                borderRadius: '8px',
                border: '1px solid #334155',
                background: '#0f172a',
                color: '#fff',
                fontSize: '13px',
                marginRight: '8px',
              }}
            />
            <button
              type="submit"
              style={{
                padding: '8px 14px',
                borderRadius: '8px',
                background: '#6366f1',
                color: '#fff',
                border: 'none',
                fontSize: '13px',
                fontWeight: 700,
                cursor: 'pointer',
              }}
            >
              Send
            </button>
          </form>
        </div>
      )}

      <button
        onClick={() => setIsOpen((prev) => !prev)}
        style={{
          width: '56px',
          height: '56px',
          borderRadius: '50%',
          background: '#6366f1',
          color: '#fff',
          border: 'none',
          fontSize: '24px',
          cursor: 'pointer',
          boxShadow: '0 4px 12px rgba(0,0,0,0.4)',
        }}
      >
        {isOpen ? '✕' : '💬'}
      </button>
    </div>
  );
}

export default ChatWidget;