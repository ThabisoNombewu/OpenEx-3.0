const SYMBOLS = ['₿', 'Ξ', '◎', '$', '€'];

const COINS = Array.from({ length: 18 }, (_, i) => ({
  id: i,
  symbol: SYMBOLS[Math.floor(Math.random() * SYMBOLS.length)],
  left: Math.random() * 100,
  size: 16 + Math.random() * 28,
  duration: 12 + Math.random() * 14,
  delay: Math.random() * 10,
  opacity: 0.08 + Math.random() * 0.12,
}));

function AnimatedBackground() {
  return (
    <div
      style={{
        position: 'fixed',
        inset: 0,
        overflow: 'hidden',
        zIndex: 0,
        pointerEvents: 'none',
      }}
    >
      {COINS.map((coin) => (
        <span
          key={coin.id}
          style={{
            position: 'absolute',
            left: `${coin.left}%`,
            bottom: '-60px',
            fontSize: `${coin.size}px`,
            color: '#6366f1',
            opacity: coin.opacity,
            animation: `floatUp ${coin.duration}s linear ${coin.delay}s infinite`,
            userSelect: 'none',
          }}
        >
          {coin.symbol}
        </span>
      ))}

      <style>{`
        @keyframes floatUp {
          0% {
            transform: translateY(0) rotate(0deg);
          }
          100% {
            transform: translateY(-110vh) rotate(360deg);
          }
        }
      `}</style>
    </div>
  );
}

export default AnimatedBackground;