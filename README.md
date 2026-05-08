# Virality Engine – Spring Boot Backend Assignment

## Overview

Virality Engine is a Spring Boot microservice built for handling posts, comments, likes, Redis-based virality scoring, concurrency guardrails, and notification batching.

The project demonstrates how Redis can be used for:

* Real-time scoring
* Atomic concurrency control
* Cooldown systems
* Notification throttling
* Scheduled aggregation jobs

---

# Tech Stack

* Java 17
* Spring Boot 3
* PostgreSQL
* Redis
* Docker
* Maven

---

# Features

## Phase 1 – Core API & Database

### Entities

* User
* Bot
* Post
* Comment

### REST APIs

### Create Post

```http
POST /api/posts
```

### Add Comment

```http
POST /api/posts/{postId}/comments
```

### Like Post

```http
POST /api/posts/{postId}/like
```

---

# Phase 2 – Redis Virality Engine & Guardrails

## Virality Score Logic

Redis is used for real-time virality scoring.

### Scoring Rules

| Interaction   | Score |
| ------------- | ----- |
| Bot Reply     | +1    |
| Human Like    | +20   |
| Human Comment | +50   |

### Redis Key

```redis
post:{id}:virality_score
```

### Example

```redis
post:2:virality_score = 51
```

---

# Atomic Guardrails

## 1. Horizontal Cap

A post cannot receive more than **100 bot replies**.

### Redis Key

```redis
post:{id}:bot_count
```

### Logic

Redis `INCR` operation is used atomically.

If count exceeds 100:

* API throws `429 TOO MANY REQUESTS`

This prevents race conditions during concurrent bot requests.

---

## 2. Vertical Cap

Comment depth level cannot exceed **20**.

### Validation

```java
if(depthLevel > 20)
```

Request gets rejected.

---

## 3. Cooldown Cap

A bot cannot interact with the same human more than once in **10 minutes**.

### Redis Key

```redis
cooldown:bot_{botId}:user_{userId}
```

### Redis TTL

```redis
600 seconds
```

If key exists:

* interaction is blocked

---

# Phase 3 – Notification Engine

## Notification Throttling

To avoid notification spam:

If notification cooldown exists:

* Notification is stored in Redis List

### Redis List Key

```redis
user:{id}:pending_notifs
```

If cooldown does not exist:

* Console logs:

```text
Push Notification Sent to User
```

* Cooldown key created for 15 minutes

---

# CRON Sweeper

A scheduled task runs every 5 minutes.

## Responsibilities

* Scan pending notification lists
* Aggregate notifications
* Print summarized notifications

### Example

```text
Summarized Push Notification:
Bot X and 5 others interacted with your posts
```

---

# Thread Safety & Concurrency Handling

## How Race Conditions Were Prevented

Redis atomic operations were used for all counters and locks.

### Atomic Redis Operations Used

* INCR
* SETNX
* EXISTS
* EXPIRE

### Why This Works

Redis operations are atomic by default.

Even if 200 concurrent requests hit the API simultaneously:

* bot count increments safely
* maximum limit remains exactly 100

This guarantees:

* no duplicate bot replies beyond limit
* no inconsistent state

---

# Stateless Architecture

The Spring Boot application remains fully stateless.

No in-memory storage was used such as:

* HashMap
* static variables
* local caches

All runtime state is stored inside Redis.

---

# Database Responsibility

## PostgreSQL

Acts as source of truth for:

* posts
* comments
* users
* bots

## Redis

Acts as:

* guardrail engine
* cooldown manager
* virality calculator
* notification queue

---

# Project Structure

```text
com/example/viralityengine/
├── controller/
├── service/
├── repository/
├── entity/
├── dto/
├── mapper/
├── redis/
├── scheduler/
├── exception/
└── ViralityEngineApplication.java
```

---

# Running the Project

## 1. Start Docker Containers

```bash
docker start pg_db
docker start redis_db
```

## PostgreSQL

```text
Database: virality_db
Port: 5432
```

## Redis

```text
Port: 6379
```

---

## 2. Run Spring Boot

```bash
mvn spring-boot:run
```

or run directly from IDE.

---

# API Testing

Tested using:

* Postman
* Redis CLI
* PostgreSQL CLI

---

# Sample Redis Verification

```redis
GET post:2:virality_score
GET post:2:bot_count
TTL cooldown:bot_1:user_1
```

---

# Deliverables Included

* Spring Boot source code
* Redis integration
* PostgreSQL integration
* Atomic Redis guardrails
* Scheduled notification batching
* Postman API collection
* README documentation

---

# Author

**Tushar Chourasiya**
