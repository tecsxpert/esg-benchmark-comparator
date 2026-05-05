from flask import Flask
from flask_limiter import Limiter
from flask_limiter.util import get_remote_address
from routes.describe_routes import describe_bp

from routes.ai_routes import ai_bp
import werkzeug

werkzeug.serving.WSGIRequestHandler.server_version = ""
werkzeug.serving.WSGIRequestHandler.sys_version = ""

app = Flask(__name__)
@app.after_request
def secure_headers(response):
    response.headers["Content-Security-Policy"] = (
        "default-src 'self'; "
        "script-src 'self' 'unsafe-inline'; "
        "style-src 'self' 'unsafe-inline'; "
        "img-src 'self' data:; "
        "connect-src 'self'; "
        "frame-ancestors 'none';"
    )
    response.headers["X-Content-Type-Options"] = "nosniff"
    response.headers["X-Frame-Options"] = "DENY"
    response.headers["X-XSS-Protection"] = "1; mode=block"
    response.headers["Server"] = ""
    return response

# Configure rate limiter
limiter = Limiter(
    key_func=get_remote_address,
    default_limits=["30 per minute"]
)

limiter.init_app(app)

# Register AI routes
app.register_blueprint(ai_bp, url_prefix="/ai")
app.register_blueprint(describe_bp, url_prefix="/ai")


@app.route("/health")
def health():
    return {
        "status": "healthy",
        "message": "AI Service Running"
    }
@app.after_request
def secure_headers(response):
    # Strong CSP (fixes "no fallback" warning)
    response.headers["Content-Security-Policy"] = (
    "default-src 'self'; "
    "script-src 'self' 'unsafe-inline'; "
    "style-src 'self' 'unsafe-inline'; "
    "img-src 'self' data:; "
    "connect-src 'self'; "
    "frame-ancestors 'none';"
)


    # Other security headers
    response.headers["X-Content-Type-Options"] = "nosniff"
    response.headers["X-Frame-Options"] = "DENY"
    response.headers["X-XSS-Protection"] = "1; mode=block"

    # Remove/blank server header
    response.headers["Server"] = ""

    return response

if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=5000)
@app.route("/robots.txt")
def robots():
    return "", 200

@app.route("/sitemap.xml")
def sitemap():
    return "", 200