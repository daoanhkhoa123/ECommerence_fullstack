# Use Case

## 1. Actors
- **Guest**: Unregistered user browsing the platform.
- **Customer**: Registered user who can purchase products.
- **Vendor**: Registered user who can create and manage products.

---

## 2. Use Cases

### 2.1 Guest
- **Register Account**  
  - Actor: Guest  
  - Goal: Create an account as either a Customer or Vendor.  
  - Flow:  
    1. Guest provides registration details.  
    2. System validates and stores user profile.  
    3. User role (Customer or Vendor) is assigned.  

- **Browse Products**  
  - Actor: Guest  
  - Goal: View available products.  
  - Flow:  
    1. Guest opens product catalog.  
    2. System displays products with filters (category, name, vendor).  

---

### 2.2 Customer (inherits Guest)
- **Add Product to Cart**  
  - Actor: Customer  
  - Goal: Select products for purchase.  
  - Flow:  
    1. Customer clicks “Add to Cart.”  
    2. System updates cart with selected product.  

- **Checkout & Payment**  
  - Actor: Customer  
  - Goal: Complete purchase of items in cart.  
  - Flow:  
    1. Customer reviews cart.  
    2. Customer selects payment method.  
    3. System processes payment and confirms order.  

---

### 2.3 Vendor (inherits Guest)
- **Create Product**  
  - Actor: Vendor  
  - Goal: Add new products to catalog.  
  - Flow:  
    1. Vendor provides product details (name, description, price, category).  
    2. System validates and stores product.  
    3. Product becomes visible to Guests/Customers.  

- **Create Category**  
  - Actor: Vendor  
  - Goal: Define new product categories.  
  - Flow:  
    1. Vendor submits category name and description.  
    2. System validates and stores category.  

---

## 3. System Architecture Overview
- **Frontend**: Web/mobile interface for Guests, Customers, and Vendors.  
- **Backend Services**:  
  - User management (authentication, roles).  
  - Product & category management.  
  - Cart & order management.  
  - Payment gateway integration.  
- **Kafka Integration**:  
  - All product/category/order updates are published as events.  
  - Ensures real-time synchronization across services.  
- **AI Module**:  
  - Subscribes to Kafka topics.  
  - Pulls updates and syncs its own database for analytics, recommendations, or search optimization.  

---

## 4. Example User Journey
1. A **Guest** browses products and decides to register.  
2. They register as a **Customer**.  
3. They add items to their cart and pay for the order.  
4. Meanwhile, a **Vendor** creates a new product and assigns it to a category.  
5. Kafka publishes the update → AI module consumes it → AI database syncs → Customers see updated recommendations.  

# How to Code

## Basic flow:
Entity -> Functionality Ideas -> Repository -> Dto -> Controller Interface -> Full Service -> Full Controller -> Done.