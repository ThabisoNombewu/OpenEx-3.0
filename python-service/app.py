from flask import Flask, jsonify, request
from flask_cors import CORS
from market_data import simulator
from datetime import datetime

app = Flask(__name__)
CORS(app)

@app.route('/api/market/history', methods=['GET'])
def get_market_history():
    n_ticks = request.args.get('ticks', default=100, type=int)
    data = simulator.generate_market_data(n_ticks=n_ticks)
    return jsonify(data)

@app.route('/api/market/current', methods=['GET'])
def get_current_price():
    return jsonify({
        'price': simulator.get_latest_price(),
        'timestamp': datetime.now().isoformat()
    })

@app.route('/api/chat', methods=['POST'])
def chat():
    return jsonify({'response': 'AI chat coming soon!'})

@app.route('/api/health', methods=['GET'])
def health():
    return jsonify({'status': 'healthy'})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
