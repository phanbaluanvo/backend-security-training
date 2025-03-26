


# **Cybersecurity Training Platform - Backend**


## 📌 Overview
This is the backend service for the **Cybersecurity Training Platform**, responsible for user authentication, course management, and quiz functionality.

## 🛠 Tech Stack
- **Language & Framework:** Java, Spring Boot
- **Database:** MySQL (AWS RDS)
- **ORM:** Hibernate
- **Authentication:** Spring Security + JWT
- **Deployment:** Docker + AWS EC2

## ✨ Features
✅ **User authentication & role-based access control** (Admin, Instructor, Student)  
✅ **CRUD operations** for training materials and quizzes  
✅ **Progress tracking & reporting system**  
✅ **Content-based recommendation engine** to personalize learning  

## 🚀 Getting Started

### 1️⃣ Clone the repository
```bash
git clone https://github.com/phanbaluanvo/backend-security-training.git
```

### 2️⃣ Set up .env file for varibles and environment
```bash
DB_HOST=
DB_USERNAME=
DB_PASSWORD=
JWT_SECRET=
JWT_ACCESS_EXPIRATION=
JWT_REFRESH_EXPIRATION=
SERVER_PORT=
AWS_ACCESS_KEY=
AWS_SECRET_KEY=
AWS_REGION=
AWS_BUCKET=
TIME_ZONE=
```
### 3️⃣ Run the application
```bash
./gradlew bootRun
```
### 📦 Deployment
The backend is containerized with Docker and deployed on AWS EC2.
