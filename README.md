# AuthApp 🛡️

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.13-brightgreen)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-19-blue)](https://reactjs.org/)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.java.com/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5-blue)](https://www.typescriptlang.org/)
[![MySQL](https://img.shields.io/badge/MySQL-purple)](https://www.mysql.com/)

**AuthApp** is a full-stack authentication and authorization platform built with Spring Boot backend and modern React frontend. It supports JWT-based authentication, refresh tokens with secure cookies, social login via OAuth2 (Google & GitHub), user management, email verification, and role-based access control.

## ✨ Features

- 🔐 **JWT Authentication** with secure refresh tokens (HTTP-only cookies)
- 🌐 **OAuth2 Social Login** - Google & GitHub integration
- 👥 **User Management** - Registration, profile, roles
- 📧 **Email Verification** using Brevo (Sendinblue)
- 🛡️ **Comprehensive Security** - CSRF protection, CORS, input validation
- 📱 **Responsive UI** - Modern shadcn/ui + Tailwind CSS
- 🚀 **Production Ready** - Docker support, env config, logging
- ⚡ **State Management** - Zustand for React

## 🛠 Tech Stack

| Backend | Frontend | Database | Tools |
|---------|----------|----------|-------|
| Spring Boot 3.3.13 | React 19 + TypeScript | MySQL 8+ | Maven, Vite, Tailwind |
| Spring Security | React Router v7 | JPA/Hibernate | Axios, Zustand |
| JWT + OAuth2 Client | shadcn/ui | HikariCP | Framer Motion |
| ModelMapper | Tailwind CSS 4 | - | react-hot-toast |

## 🏗 Architecture

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   React SPA     │◄──►│   API Gateway    │◄──►│ Spring Boot API │
│ (localhost:5173)│    │ (CORS Enabled)   │    │ (localhost:8081)│
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                                        │
                                               ┌─────────────────┐
                                               │     MySQL       │
                                               │   auth_app DB   │
                                               └─────────────────┘
```

## 🚀 Quick Start

### Prerequisites
- Java 17+ (JDK)
- Node.js 20+
- MySQL 8+ 
- Maven 3.9+
- Git

### 1. Clone & Setup
```bash
git clone <your-repo>
cd auth-app
```

### 2. Database Setup
Create MySQL database:
```sql
CREATE DATABASE auth_app;
```

### 3. Environment Variables
Copy `.env.example` to `.env` in both backend/frontend (create if needed):

**Backend (.env in auth-app-backend/):**
```env
JWT_SECRET=your-super-secret-jwt-key-min-32-chars
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
GITHUB_CLIENT_ID=your-github-client-id
GITHUB_CLIENT_SECRET=your-github-client-secret
SMTP_KEY=your-brevo-smtp-key
DB_PASSWORD=your-mysql-password
```

**Frontend (.env in auth-app-frontend/):**
```env
VITE_API_URL=http://localhost:8081
VITE_APP_URL=http://localhost:5173
```

### 4. Backend
```bash
cd auth-app-backend
mvn clean spring-boot:run
```
> Backend runs on `http://localhost:8081`

### 5. Frontend
```bash
cd auth-app-frontend
npm install
npm run dev
```
> Frontend runs on `http://localhost:5173`

### 6. Test
- Visit `http://localhost:5173`
- Register/Login with email/password
- Try OAuth2 buttons (Google/GitHub)
- Access dashboard `/dashboard`

## 🔌 API Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/register` | User registration | No |
| POST | `/api/auth/login` | Email/password login | No |
| POST | `/api/auth/refresh` | Refresh JWT token | Refresh Token |
| POST | `/api/auth/reset-password` | Password reset request | No |
| GET | `/api/users/profile` | Get user profile | Yes |
| OAuth2 | `/oauth2/authorization/google` | Google login | No |

**Response Format:**
```json
{
  "success": true,
  "data": { ... },
  "message": "Success"
}
```

## 📁 Project Structure

```
auth-app/
├── README.md                 # This file
├── auth-app-backend/         # Spring Boot API
│   ├── pom.xml
│   ├── src/main/java/...     # Controllers, Services, Entities
│   └── src/main/resources/   # application.properties
├── auth-app-frontend/        # React SPA
│   ├── package.json
│   ├── vite.config.ts
│   └── src/                  # Components, Pages, Services
└── TODO.md                   # Development tasks
```

## 🏗 Build for Production

**Backend:**
```bash
cd auth-app-backend
mvn clean package -DskipTests
java -jar target/auth-app-backend-0.0.1-SNAPSHOT.jar
```

**Frontend:**
```bash
cd auth-app-frontend
npm run build
# Serve dist/ folder with nginx/apache
```

## 🌐 Deployment

- **Backend:** Heroku/Railway/DigitalOcean + RDS MySQL
- **Frontend:** Vercel/Netlify (set API_URL)
- **Docker:** Dockerfile in each folder (create as needed)

## 🧪 Testing

```bash
# Backend
mvn test

# Frontend
cd auth-app-frontend
npm run lint
npm run build
```

## 🤝 Contributing

1. Fork the project
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

## 📄 License

This project is licensed under the MIT License - see [LICENSE](LICENSE) file.

## 🙌 Acknowledgments

- Spring Boot Team
- React Community
- shadcn/ui contributors

---

⭐ **Star us on GitHub if this helps you!** ⭐
