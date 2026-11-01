# LAB 07A — Room Entity & DAO Construction

Project Android Studio cho bài cá nhân Session 07A.

## Thông số project

- Project: `ProductRoomApp`
- Package: `edu.vlu.week07.productroom`
- Kotlin, Kotlin DSL, min SDK 24, compile/target SDK 34
- Room `2.6.1`, KSP `1.9.20-1.0.14`, LiveData KTX `2.8.0`

## Nội dung đã thực hiện

- `ProductEntity` ánh xạ vào bảng `products` với khóa chính tự tăng.
- Các cột SQLite: `id`, `product_name`, `product_price`, `stock_quantity`.
- `ProductDao` có thêm, xóa, quan sát toàn bộ sản phẩm bằng `LiveData`, và tìm theo ID bằng hàm `suspend`.

Mở thư mục gốc bằng Android Studio, chờ Gradle Sync hoàn tất, sau đó chạy **Build > Make Project**.
