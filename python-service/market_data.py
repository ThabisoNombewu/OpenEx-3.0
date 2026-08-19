import numpy as np
import pandas as pd
from datetime import datetime, timedelta

class MarketSimulator:
    def __init__(self, initial_price=50000, volatility=0.02, drift=0.001):
        self.initial_price = initial_price
        self.volatility = volatility
        self.drift = drift
        self.data = []
        
    def generate_ticks(self, n_ticks=100, start_price=None):
        if start_price is None:
            start_price = self.initial_price
        prices = []
        current_price = start_price
        for _ in range(n_ticks):
            change = np.random.normal(self.drift, self.volatility)
            current_price = current_price * (1 + change)
            prices.append(current_price)
        return prices
    
    def generate_market_data(self, n_ticks=100, interval_seconds=5):
        prices = self.generate_ticks(n_ticks)
        now = datetime.now()
        timestamps = [(now - timedelta(seconds=(n_ticks - i) * interval_seconds)) 
                     for i in range(n_ticks)]
        df = pd.DataFrame({'timestamp': timestamps, 'price': prices})
        df['sma_20'] = df['price'].rolling(window=20).mean()
        df['sma_50'] = df['price'].rolling(window=50).mean()
        data = []
        for _, row in df.iterrows():
            data.append({
                'timestamp': row['timestamp'].isoformat(),
                'price': round(row['price'], 2),
                'sma_20': round(row['sma_20'], 2) if not pd.isna(row['sma_20']) else None,
                'sma_50': round(row['sma_50'], 2) if not pd.isna(row['sma_50']) else None
            })
        self.data = data
        return data
    
    def get_latest_price(self):
        if self.data:
            return self.data[-1]['price']
        return self.initial_price

simulator = MarketSimulator()
