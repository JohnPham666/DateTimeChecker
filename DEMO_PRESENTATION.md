# 🚀 Kịch bản Demo Thực Tế: Sức Mạnh Của CI/CD Testing

> **Ngữ cảnh:** Bạn đã giải thích xong lý thuyết về CI/CD cho khán giả. Bây giờ là lúc "Show, don't tell" (Chứng minh bằng thực hành). Kịch bản này hướng dẫn bạn thực hiện thao tác live-code trên màn hình để minh họa.

---

## 🎤 Mở đầu Demo

"Chào mọi người, sau khi đã nắm rõ lý thuyết, bây giờ mình sẽ demo trực tiếp trên project **Date Time Checker** để các bạn thấy CI/CD hoạt động thực tế như thế nào, và tại sao nó lại cứu mạng lập trình viên trong những tình huống code sai.

Đầu tiên, hãy nhìn vào 'Bộ não' của hệ thống CI - đó chính là các file Unit Test."

---

## Phần 1: Giải phẫu File Test (`DateValidatorTest.java`)

*(Mở file `src/test/java/com/trieupk/dateTimeChecker/DateValidatorTest.java` trên màn hình)*

"Đây là file Test của chúng ta, được viết bằng thư viện JUnit 5. Hãy xem syntax (cú pháp) của nó nói gì nhé:

1. **`@BeforeEach` (Dòng 18):** 
   - Hàm `setUp()` sẽ tự động chạy *trước mỗi kịch bản test*. Nó khởi tạo đối tượng `DateValidator` mới tinh để đảm bảo các test không bị ảnh hưởng lẫn nhau.

2. **`@Test` và `@DisplayName` (Dòng 25-26):**
   - `@Test` đánh dấu đây là một kịch bản kiểm thử độc lập.
   - `@DisplayName` giúp đặt tên kịch bản dễ hiểu khi in ra màn hình. (Ví dụ: *"Valid leap year date: 29/02/2024"*).

3. **Assertions (Các phép khẳng định):**
   - **`assertTrue(validator.isValidDate(29, 2, 2024))`**: Lệnh này quả quyết rằng hàm `isValidDate` BẮT BUỘC phải trả về `true` (Ngày hợp lệ). Nếu nó trả về `false`, test này sẽ lập tức báo ĐỎ (Fail).
   - **`assertFalse(validator.isValidDate(30, 2, 2024))`**: Ngược lại, kiểm tra ngày 30/2 (không tồn tại), bắt buộc hàm phải trả về `false`.
   - **`assertEquals("...", result)`**: Kiểm tra thông báo lỗi trả về có khớp chuẩn xác từng chữ với thiết kế hay không.

👉 **Flow của CI:** Khi GitHub Actions chạy lệnh `mvn test`, nó sẽ lần lượt chạy 15 hàm `@Test` này. Chỉ cần 1 hàm sai kỳ vọng, toàn bộ quá trình CI bị đánh rớt."

---

## Phần 2: Demo Kịch Bản "Happy Path" (Code Đúng ➔ Deploy Thành Công)

"Bây giờ, giả sử mình là Developer, mình vừa code xong một tính năng mới (hoặc viết thêm test) và code của mình hoàn toàn chính xác. Mình sẽ push code này lên GitHub."

*(Thao tác trên màn hình)*
1. Mở Terminal, gõ: `git commit -m "feat: update logic"` và `git push`.
2. Mở trình duyệt, vào tab **Actions** trên GitHub.

"Các bạn nhìn xem chuyện gì đang xảy ra:
- **Job 1 (`build-and-test`):** Máy chủ GitHub đang kéo code về và chạy bộ Test chúng ta vừa xem. Mất vài giây... Xanh rồi! (PASS).
- **Flow qua Job 2 (`docker-build-and-push`):** Nhờ dòng lệnh `needs: build-and-test` trong file `cicd.yml`, Job 2 thấy Job 1 thành công nên lập tức tiến hành đóng gói Docker Image và đẩy lên Docker Hub.

👉 Đây là quy trình **Continuous Deployment (CD) hoàn hảo**. Mọi thứ tự động từ A-Z."

---

## Phần 3: Demo Kịch Bản "Disaster" (Code Sai ➔ CI Chặn Lại) 🔥

"Nhưng thực tế lập trình viên không hoàn hảo. Giả sử đêm qua mình buồn ngủ, mình sửa nhầm logic kiểm tra năm nhuận."

*(Thao tác trên màn hình: Mở file `src/main/java/com/trieupk/dateTimeChecker/DateValidator.java`)*
"Tại hàm kiểm tra ngày, mình vô tình gõ sai làm hỏng logic của ngày 29/2."

**Cách làm để demo:**
Sửa hàm `isValidDate` trong file `DateValidator.java`, thêm dòng code phá hoại này vào đầu hàm:
```java
public boolean isValidDate(int day, int month, int year) {
    // DEV NGÁO NGƠ THÊM DÒNG NÀY: Cứ ngày 29 là báo sai!
    if (day == 29) {
        return false; 
    }
    // ... code cũ giữ nguyên
```

*(Tiếp tục thao tác trên màn hình)*
1. "Bây giờ, mình ngây thơ nghĩ code vẫn đúng, và tự tin push thẳng lên mạng."
2. Gõ `git add .`, `git commit -m "fix: refactor date logic"`, `git push`.
3. Chuyển sang trình duyệt, mở tab **Actions**.

"Hãy cùng xem 'Tấm khiên bảo vệ' CI/CD hoạt động nhé.
- Các bạn thấy không, **Job 1 (`build-and-test`) vừa chạy đã báo ❌ ĐỎ CHÓT (FAILED)!**
- Tại sao? Bấm vào xem log: Bộ Unit Test báo lỗi ở test case `testValidLeapYearDate()`. Test kỳ vọng 29/02/2024 là hợp lệ (`true`), nhưng code lỗi của mình lại trả về `false`.

**👉 VÀ ĐÂY LÀ ĐIỂM ĂN TIỀN CỦA CD:**
- Hãy nhìn sang **Job 2 (`docker-build-and-push`)**. Nó hiển thị icon 🚫 (SKIPPED - Bị bỏ qua). 
- Vì Job 1 đã Fail, Job 2 tuyệt đối không được phép chạy. Nhờ vậy, phiên bản phần mềm chứa lỗi ngớ ngẩn của mình **KHÔNG BAO GIỜ** bị đóng gói thành Docker Image, và **KHÔNG BAO GIỜ** đến tay người dùng.

Mình vừa được CI/CD cứu một bàn thua trông thấy trước khi sếp phát hiện!"

---

## Phần 4: Sửa lỗi và phục hồi hệ thống

"Để chuộc lỗi, mình chỉ cần quay lại code, xóa dòng code ngớ ngẩn kia đi."

*(Thao tác xóa đoạn code bị lỗi đi, lưu lại)*
"Sau đó, mình commit và push lại: `git commit -m "fix: correct leap year logic"`.

Các bạn xem GitHub Actions lại chạy. Lần này, Test lại XANH ✅, và Job Docker lại tự động đẩy phiên bản hoàn thiện lên Docker Hub."

---

## 🎯 Lời kết (Tổng kết thông điệp)

"Qua bản demo thực tế vừa rồi, các bạn có thể thấy rõ ràng:
1. **File Test** không phải là thứ viết cho vui, nó là **bộ luật** để hệ thống CI đối chiếu.
2. **Cú pháp trong `cicd.yml`** như `needs: build-and-test` chính là **chốt chặn an toàn (Safety Net)**. Nó chặn đứng các bản cập nhật rác (bug) rò rỉ ra môi trường thực tế (CD).
3. CI/CD loại bỏ hoàn toàn yếu tố con người (thứ luôn có sai sót) ra khỏi khâu kiểm tra và phân phối. Chỉ có máy móc tự động kiểm tra lẫn nhau.

Cảm ơn các bạn đã theo dõi!"
