# 💍 VivahConnect – One Click Invitation Delivery System

VivahConnect is a Spring Boot–based wedding invitation management system that allows families to manage guests and send digital invitations effortlessly from a single platform.

---

## 🚀 Project Overview

VivahConnect simplifies wedding communication by combining **guest management** and **invitation delivery** into one application. Users can add guests, upload invitation cards, and send personalized emails with attachments using Gmail SMTP.

---

## ✨ Features

* ➕ Add guests with name and email
* 📋 View complete guest list
* ✏️ Update guest details
* ❌ Delete guest records
* 📤 Upload wedding invitation file
* 📧 Send invitation emails to all guests
* 🌐 Lightweight frontend for easy interaction

---

## 🛠️ Tech Stack

* **Backend:** Java 21, Spring Boot 3
* **Database:** MySQL
* **ORM:** Spring Data JPA (Hibernate)
* **Email Service:** Spring Mail (Gmail SMTP)
* **Frontend:** HTML, CSS, JavaScript
* **Build Tool:** Maven

---

## 📁 Project Structure

```
src/main/java/com/vivah/vivahconnect
├── controller
│   ├── GuestController.java
│   └── InvitationController.java
├── entity
│   └── Guest.java
├── repository
│   └── GuestRepository.java
├── service
│   └── EmailService.java
└── VivahconnectApplication.java

src/main/resources
├── static
│   ├── index.html
│   └── welcome.html
└── application.properties
```

---

## ⚙️ How It Works

1. Open the application in your browser
2. Add guest details (name & email)
3. Upload the wedding invitation file
4. Send invitation emails to all guests

---

## 🔧 Configuration

Update the following properties in:

```
src/main/resources/application.properties
```

### Database

```
spring.datasource.url=jdbc:mysql://localhost:3306/vivah_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Email (⚠️ Use App Password)

```
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
```

---

## ▶️ Running the Project

### Prerequisites

* Java 21+
* Maven
* MySQL Server

### Steps

1. Create database:

```
vivah_db
```

2. Update credentials in `application.properties`

3. Run project:

```bash
mvnw spring-boot:run
```

(Windows)

```bash
mvnw.cmd spring-boot:run
```

4. Open in browser:

```
http://localhost:8080/welcome.html
```

---

## 🔗 API Endpoints

### 👥 Guest Management

* `POST /guest/add` → Add guest
* `GET /guest/all` → Get all guests
* `GET /guest/{id}` → Get guest by ID
* `PUT /guest/update/{id}` → Update guest
* `DELETE /guest/delete/{id}` → Delete guest

---

### 📧 Invitation & Email

* `POST /invitation/upload` → Upload invitation
* `GET /guest/send` → Send emails to all guests
* `GET /guest/test-mail` → Send test email

---

## 📂 File Storage

* Uploaded invitation files are stored in:

```
/uploads/
```

---

## 🔐 Security Note (IMPORTANT)

❌ Do NOT expose your email/password in code
✔ Use environment variables instead:

```
spring.mail.username=${EMAIL}
spring.mail.password=${EMAIL_PASS}
```

---

## 📈 Future Improvements

* RSVP tracking system
* Admin authentication
* Input validation & error handling
* Invitation templates
* Dashboard analytics for invitations

---

## 👨‍💻 Author

**Nyusi Patel**

---

## ⭐ Support

If you like this project, give it a ⭐ on GitHub!
