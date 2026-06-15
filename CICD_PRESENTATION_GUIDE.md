# 🎬 Kịch bản Thuyết trình: Tường tận CI/CD & Docker qua Project Date Time Checker

> Tài liệu này được thiết kế để bạn thuyết trình báo cáo hoặc làm video YouTube. Nó đi từ bức tranh toàn cảnh (Overview) cho đến giải thích chi tiết từng dòng code (Details).

---

## Phần 1: Bức tranh toàn cảnh (Overview)

### 1. Vấn đề của lập trình truyền thống là gì?
Hãy tưởng tượng kịch bản sau:
- Developer code xong, chạy trên máy mình thấy ngon lành, bèn nén file gửi cho Khách hàng.
- Khách hàng tải về chạy thì báo lỗi. Lý do? Máy khách hàng dùng Java bản cũ, chưa cài thư viện, hoặc dùng hệ điều hành khác. Đây là hội chứng kinh điển: **"It works on my machine" (Trên máy tôi chạy bình thường mà!)**.
- Tiếp theo, mỗi lần có tính năng mới, Dev lại phải: tự chạy test bằng tay ➔ build file ➔ copy lên server ➔ restart server. Mất rất nhiều thời gian và dễ sai sót.

### 2. Giải pháp: CI/CD + Docker
Để giải quyết, chúng ta áp dụng **CI/CD** kết hợp với **Docker**:
- **CI (Continuous Integration - Tích hợp liên tục):** Mỗi khi Dev đẩy code lên GitHub, một con Bot tự động tải code về, kiểm tra xem code có lỗi biên dịch không, và tự động chạy tất cả các bài Test (Unit Test). Nếu Test thất bại, code này sẽ bị chặn lại.
- **Docker:** Thay vì đưa file code cho khách hàng, ta đưa toàn bộ ứng dụng + môi trường chạy (Java) vào một cái hộp gọi là "Docker Container". Hộp này mang đi máy nào cũng chạy được giống hệt nhau.
- **CD (Continuous Delivery - Phân phối liên tục):** Khi CI (Test) đã xanh (thành công), Bot tự động đóng gói ứng dụng vào hộp Docker, và đẩy hộp đó lên một cái chợ ứng dụng (Docker Hub). Bất kỳ ai muốn dùng chỉ cần lên đó tải về.

👉 **Tóm lại Flow của project chúng ta:**
`Dev Push Code` ➔ `GitHub Actions chạy Test (CI)` ➔ `Build Docker Image (CD)` ➔ `Push lên Docker Hub` ➔ `User tải về chạy.`

---

## Phần 2: Đào sâu vào Chi Tiết (Deep Dive into Details)

Trong project này, có **4 thành phần/file cốt lõi** tạo nên hệ thống CI/CD. Chúng ta sẽ "mổ xẻ" từng file.

### 1. Bộ Unit Test: `DateValidatorTest.java` (Trái tim của CI)

> **Lý thuyết:** CI vô dụng nếu không có Test. CI chỉ là cái máy tự động chạy, Test mới là bộ não phân định code Đúng hay Sai.

Trong thư mục `src/test/java/.../DateValidatorTest.java`, chúng ta đã viết sẵn các kịch bản kiểm thử:
- **Test trường hợp đúng (Valid):** Ngày thường (15/06/2024), Năm nhuận (29/02/2024)...
- **Test trường hợp sai (Invalid):** Ngày 30 tháng 2, ngày 31 tháng 4...
- **Test biên (Boundary):** Vượt quá giới hạn ngày, tháng, năm.

**Nhiệm vụ trong CI:** Khi GitHub Actions gõ lệnh `mvn test`, nó sẽ quét qua toàn bộ các file Test này. Chỉ cần 1 test fail (ví dụ code logic mới làm hỏng việc tính năm nhuận), toàn bộ quá trình CI sẽ báo Đỏ ❌ và dừng lại ngay lập tức. Cập nhật sẽ không được đưa lên mạng.

---

### 2. File cấu hình Docker: `Dockerfile` (Bản vẽ cái hộp)

> **Lý thuyết:** Dockerfile là danh sách các "mệnh lệnh" dạy cho Docker cách đóng gói ứng dụng. Chúng ta dùng kỹ thuật **Multi-stage build** (Build nhiều giai đoạn) để tối ưu dung lượng.

```dockerfile
# ===== Giai đoạn 1: Build (Tạo ra file WAR) =====
FROM maven:3.9-eclipse-temurin-21 AS build
```
- **Ý nghĩa:** Mượn một cái máy chủ ảo có cài sẵn `Maven` và `Java 21`. Ta gọi máy này là `build`.

```dockerfile
WORKDIR /app
COPY pom.xml .
COPY src ./src
```
- **Ý nghĩa:** Tạo thư mục `/app` trong máy ảo. Copy file cấu hình `pom.xml` và toàn bộ thư mục `src` từ máy thật vào trong máy ảo.

```dockerfile
RUN mvn clean package -DskipTests
```
- **Ý nghĩa:** Ra lệnh cho máy ảo chạy lệnh build ra file `.war` (Bỏ qua test vì GitHub Actions đã test trước đó rồi).

```dockerfile
# ===== Giai đoạn 2: Run (Chạy ứng dụng) =====
FROM eclipse-temurin:21-jre
```
- **Ý nghĩa:** Quăng cái máy ảo to đùng ở trên đi (vì Maven rất nặng). Mượn một cái máy ảo khác nhỏ gọn hơn, CHỈ có `Java Runtime (JRE)`. Không cần công cụ build nữa.

```dockerfile
WORKDIR /app
COPY --from=build /app/target/*.war app.war
```
- **Ý nghĩa:** Chỉ copy ĐÚNG 1 file `.war` thành phẩm từ máy `build` (Giai đoạn 1) sang máy này. Nhờ vậy Docker Image cuối cùng siêu nhẹ.

```dockerfile
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]
```
- **Ý nghĩa:** Mở cổng mạng `8080` để bên ngoài chui vào được. Khi Container bật lên, tự động chạy lệnh `java -jar app.war` để khởi động Spring Boot.

---

### 3. File Bộ lọc: `.dockerignore`

> **Lý thuyết:** Giống `.gitignore`, nó bảo Docker đừng copy những file rác (như thư mục `.git`, file cấu hình của VSCode `.idea`) vào bên trong Container. 
- **Tại sao cần?** Giúp build nhanh hơn, Image nhẹ hơn, và bảo mật hơn (không lỡ copy nhầm mật khẩu trên máy thật vào Image).

---

### 4. File cấu hình CI/CD: `.github/workflows/cicd.yml` (Bộ não điều phối)

> **Lý thuyết:** Đây là tờ giấy ghi "Quy trình công việc" đưa cho ông quản gia GitHub Actions. Khi có sự kiện xảy ra, ông quản gia cứ nhìn giấy mà làm.

```yaml
on:
  push:
    branches: [ "main" ]
```
- **Kích hoạt (Trigger):** Quản gia canh me, hễ có ai gõ `git push` lên nhánh `main`, ổng sẽ bắt đầu làm việc.

**Quy trình có 2 Job (Nhiệm vụ):**

#### Job 1: Tích hợp liên tục (CI)
```yaml
  build-and-test:
    runs-on: ubuntu-latest
```
- Lấy một máy chủ chạy `Ubuntu` mới cứng của GitHub.
```yaml
    steps:
    - uses: actions/checkout@v4
    - name: Set up JDK 21
      uses: actions/setup-java@v4
      with:
        java-version: '21'
        distribution: 'temurin'
```
- **Steps:** 
  1. Tải code từ GitHub về máy Ubuntu (`checkout`).
  2. Cài đặt Java 21 (`setup-java`).
```yaml
    - name: Build and Test with Maven
      run: mvn clean test
```
- Chạy lệnh Test. Nếu có lỗi, Job 1 vấp ngã ➔ Dừng toàn bộ. Nếu PASS, chuyển sang Job 2.

#### Job 2: Phân phối liên tục (CD)
```yaml
  docker-build-and-push:
    runs-on: ubuntu-latest
    needs: build-and-test
```
- **needs:** Lệnh sống còn! Bắt buộc Job 1 phải Xanh (Test thành công) thì Job 2 mới được phép chạy. Code lỗi thì không bao giờ được phép đóng gói.

```yaml
    - name: Log in to Docker Hub
      uses: docker/login-action@v3
      with:
        username: johnpham666
        password: ${{ secrets.DOCKERHUB_TOKEN }}
```
- **Bảo mật:** Đăng nhập vào Docker Hub. Chú ý dòng `${{ secrets... }}`. Ta giấu mật khẩu trong két sắt của GitHub (Settings > Secrets). Lúc chạy, quản gia tự vào két lấy ra nhập, không ai đọc được. Tránh việc bị hacker trộm mã.

```yaml
    - name: Build and Push Docker Image
      uses: docker/build-push-action@v6
      with:
        context: .
        push: true
        tags: |
          johnpham666/datetimechecker:latest
          johnpham666/datetimechecker:${{ github.sha }}
```
- **Cuối cùng:** Quản gia nhìn theo `Dockerfile` (đã giải thích ở trên) để đóng gói hộp. Sau khi đóng xong (`push: true`), tải cái hộp đó lên Docker Hub. 
- Đánh 2 nhãn (tags): Nhãn `latest` (bản mới nhất) và nhãn là mã của cái `commit` (để dễ dàng quay lại bản cũ nếu bản mới bị lỗi).

---

## Phần 3: Kết luận (Wrap up

Từ giờ, vòng đời phát triển phần mềm của team chúng ta là:
1. Bạn thay đổi code (VD: Fix một bug của ngày 31/12).
2. Bạn gõ `git push`. Bạn có thể đi uống cafe.
3. GitHub tự động tạo máy chủ ảo, tự động chạy 15 bài unit tests.
4. Test Đạt! GitHub tự động mượn máy khác build Docker Image, ép lại dung lượng nhỏ gọn nhất.
5. GitHub tự động đăng nhập Docker Hub, đẩy image lên.
6. Khi khách hàng (hoặc server AWS/VPS) cần bản cập nhật, họ chỉ cần gõ đúng 1 dòng lệnh:
   `docker run -p 8080:8080 johnpham666/datetimechecker:latest`
   App sẽ tải bản vá lỗi mới nhất và chạy trong 1 nốt nhạc!

Đó chính là sức mạnh tự động hóa của **CI/CD**! Cảm ơn các bạn đã theo dõi.
