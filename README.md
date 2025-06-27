# 🧾 khata-web

`khata-web` is a full-stack shopkeeper finance management system designed to streamline product rate management and receipt generation for individual customers. It enables business owners to maintain dynamic product rates per customer, handle order items, generate receipts, and manage delivery efficiently.

---

## 🧩 Tech Stack

| Layer       | Technology                     |
|-------------|--------------------------------|
| Frontend    | React.js                       |
| Backend     | Spring Boot, Spring Security   |
| Database    | MySQL                          |
| Deployment  | Docker                         |
| Upcoming    | Notification Service (Product & Customer Alerts) |

---

## 🔑 Key Features

- 🧍‍♂️ **Customer Management** – Add and manage customer profiles with details like address, email, contact, and creation date.
- 📦 **Product Catalog** – Maintain product details with base prices and quantities.
- 🧮 **Custom Pricing (Factory Type Rate Method)** –  
  The **core feature**: Set unique product rates for each customer (through `CustomerProductRate`). This is a real-world solution where businesses often deal with multiple clients having individual deals or pricing terms.

  > 💡 *Example*: Customer A may get milk at ₹50/litre, while Customer B gets it at ₹48/litre – no need for manual calculations every time!

- 🧾 **Order Items & Receipts** –
    - Add multiple items per receipt with dynamic rates.
    - Automatic total price calculation based on quantity and custom rate.
    - Track delivery status.
    - Timestamped receipts for audit/history.

- 🔐 **Role-Based Access Control (RBAC)** –  
  Spring Security provides secure login with roles like `admin`, `user`, or `shopkeeper` to limit access to specific modules.

- 📣 **Upcoming**:
    - Notification system for low-stock products or important customer updates.

---


## 🔄 Project Flow

### 1. 🧍 Add a Customer
Create a customer profile with details like name, contact, and address.  
➡️ **Must be added first** before creating receipts or assigning custom rates.

---

### 2. 📥 Add a Receipt for the Customer
Each customer can have **multiple receipts**:
- Each receipt includes a list of products (`OrderItems`)
- Quantity, rate, and total price are auto-calculated
- Timestamp and delivery status recorded

---

### 3. 📉 Rate Determination Logic

When a product is added to a receipt:

- ✅ If a custom rate exists in `CustomerProductRate`, use it.
- ❌ Otherwise, fallback to the product's base price.
- 💡This dynamic rate resolution is the core feature of the system and models real-life factory/shopkeeper scenarios where pricing can vary by client.



