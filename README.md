# 🚌 Bus Ticket Reservation Management System

## 📌 Overview
The **Bus Ticket Reservation Management System** is a full-stack capstone project designed to digitize and streamline bus ticketing operations.  
It replaces manual booking systems that often lead to **overbooking, long queues, and poor customer experience**.  

This system provides **real-time seat availability, secure bookings, online payments, e-tickets, cancellations, and role-based access control** for **admins** and **customers**.  

---

## 🚀 Features

### 👤 User Roles
- **Admin**
  - Manage buses, routes, schedules, pricing, and reports.
- **Customer**
  - Search trips, choose seats, book & pay, download e-tickets, cancel bookings.

### 🔐 Security
- **JWT Authentication** for login and API access.
- **Role-Based Access Control** (Admin vs Customer).
- **BCrypt password hashing**.

---

## 🛠 Technology Stack

### Backend
- Spring Boot (REST APIs)
- Spring Security + JWT
- MySQL (Database)
- JPA/Hibernate (ORM)
- Swagger / OpenAPI (API Documentation)

### Frontend
- React.js (Hooks + Functional Components)
- React Router (Routing)
- Axios (API calls)
- Bootstrap + Custom CSS (Styling)

---

## 📂 Core Modules
1. **Authentication & User Management** – JWT login, registration, roles, profile.  
2. **Bus & Route Management** – Manage buses, seat layouts, routes, and stops.  
3. **Trip Scheduling & Seat Inventory** – Create date-wise trips, fares, and check live seat availability.  
4. **Booking & Payment Processing** – Seat hold, booking confirmation, payment capture.  
5. **Ticketing, Cancellations & Notifications** – E-tickets with PDF/QR code, cancellation, refunds, email/SMS.  
6. **Reports & Dashboards** – Sales, occupancy, route performance, daily settlement.  

---

## 🔗 API Guidelines
- **Base URL:** `/api/v1`
- **Auth Header:** `Authorization: Bearer <JWT>`
- **Swagger UI:** `http://localhost:8080/swagger-ui/`

### Sample Endpoints

| Module | Endpoint | Method | Access | Description |
|--------|----------|--------|--------|-------------|
| Auth | `/auth/login` | POST | Public | Authenticate & return token |
| Auth | `/auth/register` | POST | Public | Register new user |
| Bus Mgmt | `/buses` | POST | Admin | Create new bus |
| Route Mgmt | `/routes` | POST | Admin | Create new route |
| Trips | `/trips/search` | GET | All | Search trips by route & date |
| Trips | `/trips/:id/seats` | GET | All | Get seat availability |
| Booking | `/bookings/hold` | POST | Customer | Hold seats |
| Payment | `/payments/checkout` | POST | Customer | Process payment |
| Ticket | `/tickets/:id` | GET | Customer | Get ticket with QR/PDF |
| Reports | `/reports/sales` | GET | Admin | Sales summary |

---

## 🗄 Database Design

### Entities & Relationships
- **User** → One-to-Many → Bookings, Payments  
- **Bus** → One-to-Many → Trips  
- **Route** → One-to-Many → Trips  
- **Trip** → One-to-Many → Seats & Bookings  
- **Booking** → One-to-One → Payment, includes many seats  

---

## ⚙️ Installation & Setup

### Backend (Spring Boot + MySQL)
1. Clone repo:
   ```bash
   git clone <repo-url>
   cd bus-reservation-backend
2. Configure application.properties with MySQL credentials.

3. Run:
     mvn spring-boot:run
4. Access Swagger at http://localhost:8080/swagger-ui/.

Frontend (React.js)
1. Navigate to frontend folder:
    cd bus-reservation-frontend
2. Install dependencies:
     npm install
3. Run app:
    npm start
4. Access UI at http://localhost:3000/.

✅ Expected Outcomes
Real-time seat availability & conflict-free booking.

Smooth booking flow with secure payments.

Instant e-tickets with QR/PDF generation.

Admin dashboards for performance tracking.

Extensibility for agents, promo codes, and wallets.

🧪 Testing
Test authentication flows (login/register).

Validate seat hold & booking conflict prevention.

Test payments (success/failure cases).

Ensure cancellation & refunds work as per policy.

📊 Reports & Dashboards
Sales Reports (daily/monthly revenue).

Occupancy Analysis (seat utilization).

Route Profitability (top routes).

Daily Settlements.

👨‍💻 Contribution Guidelines
Fork the repository.

Create a feature branch:

git checkout -b feature/your-feature
Commit changes with proper messages.

Push to branch and create a Pull Request.

📜 License
This project is developed for educational and capston
