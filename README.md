# Career Ensat

Career Ensat is a web platform designed to connect ENSAT students, alumni, and corporate partners. It allows students and graduates to create professional profiles and connect with organizations, while providing administrators and partners with dedicated dashboards for efficient user and opportunity management.

## 🚀 Features

- **Authentication & Authorization**: 
  - Role-based login (Admin, Student, Partner)
  - Secure login system with Spring Security
- **Admin Dashboard**:
  - View, approve, or reject student/partner registration requests
  - Manage user access and roles
  - Real-time updates using Server-Sent Events (SSE)
  - View registered partners and send credentials
- **Student/Alumni Interface**:
  - Profile creation and editing
  - Browse partner companies
  - Apply to listed opportunities (feature in progress)
- **Partner Dashboard**:
  - Access and manage their profile
  - View student profiles (upcoming feature)
- **Microservices Architecture**:
  - Separate services for Authentication, User Management, Partner Management, etc.
- **Modern Stack**:
  - Backend: Spring Boot
  - Frontend: Angular
  - Real-time: SSE for instant admin UI updates
  - Containerized: Docker + Docker Compose for local orchestration

## 🛠️ Technologies Used

- **Frontend**: Angular, TypeScript, HTML, CSS
- **Backend**: Spring Boot, Java, Spring Security, JPA
- **Database**: MongoDB
- **Real-time Communication**: Server-Sent Events (SSE)
- **Containerization**: Docker, Docker Compose
- **Version Control**: Git, GitHub

## 🧪 In Progress

- Adding job posting and application modules
- Implementing profile filtering and search features
- Enhancing partner functionalities

## 📦 Project Setup

### Prerequisites
- Node.js & npm
- Angular CLI
- Java 17+
- Maven
- Docker & Docker Compose
- MongoDB (optional if running with Docker)

### Run with Docker (Development)

```bash
# From the project root containing docker-compose.yml
docker-compose up --build