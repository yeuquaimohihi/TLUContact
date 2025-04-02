Ứng dụng TLUContact:
Ứng dụng Android này cung cấp hệ thống quản lý liên hệ cho nhân viên và quản trị viên trường đại học.
Cho phép người dùng xem danh sách khoa/phòng và liên hệ nhân viên, kèm tính năng quản trị cho người dùng được ủy quyền.

Tính năng chính:
-Xác thực người dùng bằng Firebase (đăng nhập/đăng ký)
-Hệ thống xác minh email
-Kiểm soát truy cập theo vai trò (Admin/User)
-Xem và tìm kiếm khoa/phòng
-Xem và tìm kiếm nhân viên

Tính năng dành cho Admin:
-Thêm, sửa, xóa khoa/phòng
-Thêm, sửa, xóa thông tin nhân viên
-Quản lý người dùng

Chi tiết triển khai:
-Phát triển bằng Kotlin
-Sử dụng Firebase Authentication để quản lý người dùng
-Firebase Realtime Database lưu trữ liên hệ và vai trò người dùng
-View Binding cho các thành phần giao diện
-RecyclerView hiển thị danh sách liên hệ
-Sử dụng component Material Design

Bảo mật:
-Yêu cầu xác minh email trước khi đăng nhập
-Kiểm soát truy cập theo vai trò, chỉ admin có email được ủy quyền mới có quyền chỉnh sửa
-Xử lý mật khẩu an toàn thông qua Firebase Authentication

Giao diện phân tách:
Ứng dụng tách biệt giao diện cho người dùng thường và quản trị viên, đảm bảo chỉ nhân viên được ủy quyền mới có thể thay đổi thông tin liên hệ.
