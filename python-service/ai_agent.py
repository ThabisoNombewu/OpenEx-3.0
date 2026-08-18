from langchain_ollama import OllamaLLM
from langchain_classic.memory import ConversationBufferMemory
from langchain_classic.chains import ConversationChain
from langchain_classic.prompts import PromptTemplate
import json

class TradingAssistant:
    def __init__(self, model="llama3.2:3b"):
        self.model = model
        self.llm = None
        self.memory = None
        self.chain = None
        self._initialize()
    
    def _initialize(self):
        try:
            self.llm = OllamaLLM(
                model=self.model,
                temperature=0.7,
                base_url="http://localhost:11434"
            )
            print(f"LLM initialized with model: {self.model}")
            
            self.memory = ConversationBufferMemory()
            
            prompt = PromptTemplate(
                input_variables=["history", "input"],
                template="""You are Droid-EX, a professional trading assistant for OpenEx 3.0.

Your personality:
- Professional and concise
- Helpful and knowledgeable about crypto trading
- Use financial terminology correctly
- Never give financial advice - just provide information

Your capabilities:
- Answer questions about trading
- Explain market concepts
- Help users understand orders and trades

When you don't know something, say so clearly.
Keep responses clear and actionable.

Current conversation:
{history}
Human: {input}
Assistant:"""
            )
            
            self.chain = ConversationChain(
                llm=self.llm,
                memory=self.memory,
                prompt=prompt,
                verbose=False
            )
            print("Trading assistant initialized successfully")
            
        except Exception as e:
            print(f"Failed to initialize: {e}")
            self.chain = None
    
    def get_response(self, user_message):
        if self.chain is None:
            return "Assistant not initialized. Please ensure Ollama is running."
        
        try:
            response = self.chain.predict(input=user_message)
            return response
        except Exception as e:
            return f"Error: {str(e)}"
    
    def is_ready(self):
        return self.chain is not None

print("Initializing Trading Assistant...")
trading_assistant = TradingAssistant()
if trading_assistant.is_ready():
    print("Trading Assistant ready")
else:
    print("Trading Assistant not available. Please ensure Ollama is running.")
