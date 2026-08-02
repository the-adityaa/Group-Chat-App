# 💬 Group Chat App

A real-time group chat application built using **Spring Boot**, **React**, **WebSocket (STOMP + SockJS)**, and **MySQL**. Users can create or join chat rooms, exchange messages instantly, share images and PDF files, and see online users in real time.

---

## 🚀 Live Demo

**Frontend:**  
https://group-chat-app-phi.vercel.app

> **Note:** The backend is hosted using a Cloudflare Quick Tunnel. It is available only while the host PC is running.

---

## ✨ Features

- 🔐 Create and Join Chat Rooms
- 💬 Real-time Messaging
- 👥 Online Users List
- 😊 Emoji Support
- 🖼️ Image Sharing
- 📄 PDF Sharing
- 🖼️ Image Preview
- ⏰ Message Timestamp
- 🎨 Unique User Colors
- 📱 Responsive UI
- 🌐 Cross-platform (Mobile & Desktop)

---

## 🛠️ Tech Stack

### Frontend

- React.js
- Vite
- Tailwind CSS
- Axios
- SockJS
- STOMP.js
- React Hot Toast
- Emoji Picker React

### Backend

- Spring Boot
- Spring Web
- Spring WebSocket
- Spring Data JPA
- MySQL
- Hibernate
- Lombok

### Deployment

- Vercel (Frontend)
- Cloudflare Tunnel (Backend)
- GitHub

---

## 📂 Project Structure

```text
Group-Chat-App/
│
├── live-chat-backend/
│   ├── controller/
│   ├── entities/
│   ├── repository/
│   ├── service/
│   ├── payload/
│   ├── config/
│   └── resources/
│
└── live-chat-frontend/
    ├── src/
    ├── components/
    ├── services/
    ├── context/
    ├── config/
    └── assets/
```

---

## ⚙️ Installation

### Clone Repository

```bash
git clone https://github.com/the-adityaa/Group-Chat-App.git
cd Group-Chat-App
```

---

### Backend

```bash
cd live-chat-backend
```

Configure your database in:

```properties
application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/livechat
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
```

Run:

```bash
mvn spring-boot:run
```

---

### Frontend

```bash
cd live-chat-frontend
npm install
npm run dev
```

---

## 📸 Screenshots

### Home Page

_Add Screenshot_

### Chat Room

_Add Screenshot_

### Image Sharing

_Add Screenshot_

### PDF Sharing

_Add Screenshot_

### Mobile View

_Add Screenshot_

---

## 🔄 How It Works

1. User creates or joins a room.
2. Frontend connects to Spring Boot using WebSocket.
3. Messages are broadcast instantly to all users in the room.
4. Images and PDFs are uploaded through REST APIs.
5. Uploaded files are stored on the server.
6. Online users are updated in real time.

---

## 📦 API Endpoints

### Rooms

| Method | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/v1/rooms` | Create Room |
| GET | `/api/v1/rooms/{roomId}` | Join Room |
| GET | `/api/v1/rooms/{roomId}/messages` | Get Messages |

### File Upload

| Method | Endpoint |
|---------|----------|
| POST | `/api/files/upload` |

### WebSocket

```
/chat
```

Topics

```
/topic/room/{roomId}
/topic/room/{roomId}/users
```

Application Endpoints

```
/app/sendMessage/{roomId}
/app/join
/app/leave
```

---

## 🎯 Future Improvements

- Message Seen Status
- Typing Indicator
- User Authentication (JWT)
- Delete Messages
- Edit Messages
- Cloud Storage (Cloudinary/AWS S3)
- Permanent Cloudflare Tunnel
- Push Notifications

---

## 👨‍💻 Author

**Aditya**

GitHub:
https://github.com/the-adityaa

---

## 📄 License

This project is licensed under the MIT License.
