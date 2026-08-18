from flask import Flask, jsonify, request
from flask_cors import CORS
from market_data import simulator
from datetime import datetime

try:
    from ai_agent import trading_assistant
    AI_AVAILABLE = trading_assistant.is_ready()
    print(f"AI Assistant status: {'Available' if AI_AVAILABLE else 'Not Available'}")
except Exception as e:
    AI_AVAILABLE = False
    print(f"AI assistant not available: {e}")

app = Flask(__name__)
CORS(app)

@app.route('/api/health', methods=['GET'])
def health():
    return jsonify({
        'status': 'healthy',
        'ai_available': AI_AVAILABLE,
        'timestamp': datetime.now().isoformat()
    })

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
    if not AI_AVAILABLE:
        return jsonify({
            'response': 'AI assistant is not available. Please ensure Ollama is running.',
            'ai_available': False
        }), 503
    
    data = request.get_json()
    if not data or 'message' not in data:
        return jsonify({'error': 'Missing message field'}), 400
    
    user_message = data['message'].strip()
    if not user_message:
        return jsonify({'error': 'Message cannot be empty'}), 400
    
    try:
        response = trading_assistant.get_response(user_message)
        return jsonify({
            'response': response,
            'ai_available': True,
            'timestamp': datetime.now().isoformat()
        })
    except Exception as e:
        return jsonify({
            'response': f'Error: {str(e)}',
            'ai_available': False
        }), 500

@app.route('/api/ai/status', methods=['GET'])
def ai_status():
    return jsonify({
        'ai_available': AI_AVAILABLE,
        'model': getattr(trading_assistant, 'model', 'Not configured') if AI_AVAILABLE else 'N/A'
    })

if __name__ == '__main__':
    print("OpenEx AI Service Starting...")
    print(f"AI Assistant: {'Available' if AI_AVAILABLE else 'Not Available'}")
    print("Server running at http://localhost:5000")
    app.run(host='0.0.0.0', port=5000, debug=True)
