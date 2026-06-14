# ESG Benchmark Comparator
Akash koni java developer-2

🚀 Overview

The ESG Benchmark Comparator Frontend is a responsive web application built using React that allows users to view, search, and analyze ESG (Environmental, Social, Governance) data of companies. It connects seamlessly with the backend APIs to provide real-time insights and visualization.

✨ Features
🔐 Login Authentication (Basic)
Simple login page before accessing the dashboard

📋 ESG Dashboard
Displays company ESG data
Dynamic data fetched from backend APIs

🔍 Search Functionality
Real-time filtering of companies by name

🤖 AI Panel (UI Integration)
Input field for AI-based queries (extendable)

📈 Analytics Dashboard
Interactive bar charts using Recharts
Displays ESG scores visually

📱 Responsive Design
Works on mobile (375px), tablet (768px), and desktop (1280px)

🔄 Navigation
Smooth routing between Dashboard and Analytics pages

🛠️ Tech Stack
Frontend Framework: React (Vite)
HTTP Client: Axios
Charts Library: Recharts
Routing: React Router DOM
Styling: CSS (inline / basic styling)

📂 Project Structure
src/
 ├── pages/
 │    ├── Login.jsx
 │    ├── Dashboard.jsx
 │    └── Analytics.jsx
 ├── App.jsx
 └── main.jsx
🔗 API Integration

The frontend communicates with backend APIs:

GET /api/all → Fetch ESG data
GET /api/export → Export CSV
POST /api/upload → Upload files

▶️ Getting Started
1. Clone the repository
git clone <your-repo-url>
cd frontend
2. Install dependencies
npm install
3. Run the application
npm run dev
4. Open in browser
http://localhost:5173

🔐 Login Credentials (Demo)
Username: admin
Password: admin

📸 Screens Included
Login Page
ESG Dashboard
Analytics Chart

⚡ Key Highlights
Clean and simple UI for ESG data analysis
Integrated with backend APIs for real-time data
Interactive charts for better visualization
Beginner-friendly architecture and scalable design

🚧 Future Improvements
JWT-based authentication
Advanced AI integration
Status & date filters
Improved UI/UX design

👨‍💻 Author

Akash Koni
Java Developer – ESG Benchmark Comparator

📌 Conclusion

This frontend application provides a user-friendly interface for managing and visualizing ESG data, making it easier for users to analyze and compare company performance efficiently.
