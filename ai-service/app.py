from flask import Flask, request, jsonify
from flask_cors import CORS
import os

app = Flask(__name__)
CORS(app)

@app.route('/health', methods=['GET'])
def health_check():
    return jsonify({"status": "healthy", "service": "ai-service"})

@app.route('/api/ai/analyze-esg', methods=['POST'])
def analyze_esg():
    try:
        data = request.get_json()
        
        # Basic ESG analysis logic
        company_name = data.get('companyName', '')
        industry = data.get('industry', '')
        esg_score = data.get('esgScore', 0)
        
        # Simple AI analysis (in real scenario, this would use ML models)
        analysis = {
            "companyName": company_name,
            "industry": industry,
            "currentESGScore": esg_score,
            "analysis": {
                "environmental": "Good performance in sustainability initiatives" if esg_score > 70 else "Needs improvement in environmental practices",
                "social": "Strong social responsibility programs" if esg_score > 70 else "Social initiatives need enhancement",
                "governance": "Effective governance structure" if esg_score > 70 else "Governance practices require attention"
            },
            "recommendations": [
                "Increase renewable energy usage",
                "Enhance employee diversity programs",
                "Strengthen board oversight mechanisms"
            ],
            "benchmarkComparison": "Above industry average" if esg_score > 75 else "Below industry average"
        }
        
        return jsonify(analysis)
    
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/api/ai/benchmark-comparison', methods=['POST'])
def benchmark_comparison():
    try:
        data = request.get_json()
        companies = data.get('companies', [])
        
        # Simple benchmark comparison
        comparison = {
            "totalCompanies": len(companies),
            "averageESGScore": sum(comp.get('esgScore', 0) for comp in companies) / len(companies) if companies else 0,
            "topPerformer": max(companies, key=lambda x: x.get('esgScore', 0)) if companies else None,
            "bottomPerformer": min(companies, key=lambda x: x.get('esgScore', 0)) if companies else None,
            "industryInsights": {
                "technology": "Leading in environmental initiatives",
                "finance": "Strong governance practices",
                "healthcare": "Focus on social responsibility"
            }
        }
        
        return jsonify(comparison)
    
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    port = int(os.environ.get('PORT', 5000))
    app.run(host='0.0.0.0', port=port, debug=True)