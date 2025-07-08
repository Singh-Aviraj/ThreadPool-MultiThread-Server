# 🚀 High Performance Multi-Threaded Java Socket Server

A raw **multi-threaded Java socket server** (no frameworks) capable of handling thousands of concurrent client connections.

✅ Achieved **6,331 Requests/sec** sustained for 90 seconds  
✅ Processed **570,720 requests** in 1.5 minutes  
✅ Benchmarked with **10,000 concurrent clients** on a laptop using `wrk`

---

## ⚡ Features

- Pure Java Sockets (`java.net.ServerSocket`, `java.net.Socket`)
- Custom **ThreadPoolExecutor** for handling massive concurrent load
- Tunable parameters for:
  - Core pool size
  - Max pool size
  - Request queue size
  - Keep-alive time
- Graceful handling of client disconnects and timeouts
- Lightweight – no external libraries/frameworks

---

## 📊 Benchmark Results

| Metric                  | Value              |
|------------------------|--------------------|
| Concurrent Clients      | 10,000             |
| Sustained RPS           | 6,331 req/sec      |
| Total Requests          | 570,720 in 90 sec  |
| CPU Utilization         | 12-core maxed @ 100%|
| Environment             | Laptop (WSL Ubuntu)|
| Testing Tool            | `wrk`              |

---

## 🏃‍♂️ How to Run

### 1️⃣ Compile
```bash
javac MultiThreadServer.java
```

### 2 Run
```bash
java MultiThreadServer.java
```

### 3 Compile
```bash
javac Clientr.java
```
### 4 Run
```bash
java Client.java
```
### MultiThreadServer.java
/1751915627662.jpeg

### Client.java
/1751915627447.jpeg
