# 📊 NỘI DUNG SLIDE THUYẾT TRÌNH CI/CD
# Project: DateTimeChecker — Spring Boot + Android + Docker + GitHub Actions

---
## HƯỚNG DẪN CHO AI TẠO SLIDE
- File này chứa toàn bộ nội dung để tạo slide thuyết trình CI/CD
- Chia làm 2 phần rõ ràng: **PHẦN 1 — CI** và **PHẦN 2 — CD**
- Mỗi `###` là 1 slide riêng
- Code block = hiển thị code trên slide (highlight syntax)
- Bảng = tạo thành bảng đẹp trên slide
- `> 🗣️` = lời thuyết trình (speaker notes), KHÔNG hiện trên slide chính

---

# ═══════════════════════════════════════
# PHẦN 1: CI — CONTINUOUS INTEGRATION
# ═══════════════════════════════════════

---

### SLIDE 1 — TRANG TIÊU ĐỀ CI

**Tiêu đề lớn:** Continuous Integration (CI)

**Subtitle:** Tự động hóa kiểm tra chất lượng code

**Project:** DateTimeChecker — Kiểm tra ngày hợp lệ

**Tech stack:**
- ☕ Spring Boot 4.0 (Java 21) — Web App
- 🤖 Android (Java 17) — Mobile App
- ⚙️ GitHub Actions — CI/CD Engine

> 🗣️ "CI là nền tảng. Mỗi lần push code, hệ thống tự động kiểm tra xem code có lỗi không trước khi bất kỳ ai có thể dùng nó."

---

### SLIDE 2 — VẤN ĐỀ CI GIẢI QUYẾT

**Tiêu đề:** Vấn đề trước khi có CI

**2 cột:**

| ❌ Không có CI | ✅ Có CI |
|---|---|
| Developer code cả tuần, cuối tuần mới merge | Merge code liên tục, mỗi ngày nhiều lần |
| Bug ẩn phát hiện muộn → tốn nhiều công sửa | Bug phát hiện ngay khi push → sửa nhanh |
| "Code chạy trên máy tôi mà!" | Test tự động trên môi trường chuẩn |
| Deploy thủ công → dễ sai sót | Pipeline tự động → nhất quán |

> 🗣️ "Đây là câu chuyện thực tế: không có CI, một team 5 người merge code cuối sprint thường mất 1-2 ngày chỉ để fix conflict và bug."

---

### SLIDE 3 — TRIGGER: KHI NÀO CI CHẠY?

**Tiêu đề:** Trigger — Cò súng kích hoạt Pipeline

```yaml
# .github/workflows/cicd.yml — Dòng 1-7
name: Java CI/CD with Maven & Docker

on:                           # "on" = KHI NÀO thì chạy
  push:
    branches: [ "main" ]      # ① Khi PUSH code lên branch main
  pull_request:
    branches: [ "main" ]      # ② Khi tạo PULL REQUEST vào main
```

**2 loại trigger:**
- 🔵 **Push to main** → Chạy toàn bộ pipeline (CI + CD)
- 🟡 **Pull Request** → Chỉ chạy CI (test, không deploy)

> 🗣️ "Pull Request trigger quan trọng vì nó ngăn code chưa được review lên main. Nếu test fail trên PR → không cho merge."

---

### SLIDE 4 — CẤU TRÚC PIPELINE TỔNG QUAN

**Tiêu đề:** Cấu trúc Pipeline — 5 Jobs chạy trên máy ảo độc lập

```
git push origin main
        │
        ▼ GitHub Actions đọc cicd.yml
        │
        ├──► [Job 1] build-and-test          ⬜ Ubuntu
        │         │ (pass) ↓
        │         └──► [Job 2] docker-build-and-push  ⬜ Ubuntu
        │                   │ (pass) ↓
        │                   └──► [Job 5] deploy-to-render  ⬜ Ubuntu
        │
        ├──► [Job 3] android-mobile-testing  ⬜ Ubuntu (song song)
        │
        └──► [Job 4] integration-and-performance-testing  ⬜ Ubuntu (song song)
```

> 🗣️ "Mỗi Job chạy trên một máy ảo Ubuntu riêng biệt, hoàn toàn sạch. Job 1, 3, 4 chạy song song để tiết kiệm thời gian. Job 2 và 5 phải đợi Job trước pass mới chạy."

---

### SLIDE 5 — JOB 1: BUILD & UNIT TEST (Web)

**Tiêu đề:** Job 1 — Build & Unit Test (Spring Boot)

```yaml
# cicd.yml — Dòng 10-23
build-and-test:
  runs-on: ubuntu-latest       # Máy ảo Ubuntu mới tinh, sạch hoàn toàn

  steps:
  - uses: actions/checkout@v4  # Clone code từ GitHub về máy ảo

  - name: Set up JDK 21
    uses: actions/setup-java@v4
    with:
      java-version: '21'       # Cài đúng Java 21 (Spring Boot 4 yêu cầu)
      distribution: 'temurin'  # Bản Java miễn phí của Adoptium

  - name: Build and Test with Maven
    run: mvn clean test        # Compile code + chạy 30 Unit Test
```

**Ý nghĩa từng bước:**
| Step | Làm gì |
|---|---|
| `checkout@v4` | Kéo code từ GitHub về máy ảo |
| `setup-java@v4` | Cài Java 21 lên máy ảo trắng |
| `mvn clean test` | Build + chạy toàn bộ Unit Test |

> 🗣️ "mvn clean test là lệnh quan trọng nhất. Nếu có test nào fail, lệnh này trả về lỗi, GitHub Actions dừng ngay, không cho deploy code lỗi."

---

### SLIDE 6 — UNIT TEST: 30 TEST CASES

**Tiêu đề:** Unit Test — 30 Test Cases tự động

**File:** `src/test/java/.../DateValidatorTest.java`

```java
// Ví dụ: Test ngày nhuận
@Test
@DisplayName("CheckDate 4: 29/2/2024 - Năm nhuận hợp lệ")
void testCheckDate_29_2_2024() {
    assertTrue(validator.isValidDate(29, 2, 2024));  // Phải trả về TRUE
}

@Test
@DisplayName("CheckDate 2: 29/2/2009 - Năm KHÔNG nhuận")
void testCheckDate_29_2_2009() {
    assertFalse(validator.isValidDate(29, 2, 2009)); // Phải trả về FALSE
}
```

**Phân loại 30 test cases:**

| Loại | Số lượng | Ví dụ |
|---|---|---|
| Normal (N) | 17 | 29/2/2024 → valid |
| Abnormal (A) | 9 | 29/2/2023 → invalid |
| Boundary (B) | 4 | 1/1/1000 → min date |

> 🗣️ "30 test case bao phủ các trường hợp biên: năm nhuận thế kỷ (2000), năm không nhuận, tháng 30 ngày, tháng 31 ngày, input null, input chữ thay vì số."

---

### SLIDE 7 — JOB 3: ANDROID MOBILE CI

**Tiêu đề:** Job 3 — Android Mobile Testing (chạy song song)

**File:** `.github/workflows/android-cicd.yml`

```yaml
# android-cicd.yml — Dòng 9-39
jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v4

    - name: set up JDK 17          # Android dùng Java 17 (khác Web dùng Java 21)
      uses: actions/setup-java@v4
      with:
        java-version: '17'

    - name: Setup Gradle           # Gradle = Maven của thế giới Android
      uses: gradle/actions/setup-gradle@v3
      with:
        gradle-version: '8.4'

    - name: Run Unit Tests         # Chạy test bằng Robolectric (không cần emulator)
      working-directory: ./android-app
      run: gradle test

    - name: Build APK              # Đóng gói thành file .apk
      run: gradle assembleDebug

    - name: Upload APK             # Lưu APK vào GitHub Artifacts để download
      uses: actions/upload-artifact@v4
      with:
        name: app-debug
        path: .../app-debug.apk
```

**Điểm nổi bật:**
- 🤖 **Robolectric**: Chạy Android test trên JVM, **không cần emulator** → nhanh hơn 10x
- 📦 **Artifact**: APK được lưu lại 7 ngày, developer có thể download thử

> 🗣️ "Robolectric là điểm hay nhất của phần Android CI. Thay vì khởi động emulator tốn 3-5 phút, Robolectric chạy test trực tiếp trên JVM, chỉ mất vài giây."

---

### SLIDE 8 — JOB 4 (PHẦN 1): API TEST & E2E TEST

**Tiêu đề:** Job 4 — Testing Nâng Cao (API + E2E + Visual)

**Bước chuẩn bị — Khởi động server thật:**

```yaml
# cicd.yml — Dòng 93-100
- name: Start Spring Boot Server in Background
  run: |
    mvn spring-boot:run &                    # "&" = chạy nền, không block terminal
    while ! curl -s http://localhost:8080/api/v1/check?day=1&month=1&year=2024; do
      echo "Waiting for Spring Boot..."
      sleep 3                                # Thử lại mỗi 3 giây
    done
    echo "Spring Boot is UP!"               # Xác nhận server sẵn sàng
```

**API Test — Kiểm tra REST endpoint:**

```typescript
// e2e-tests/tests/api-checker.spec.ts
test('Valid Date API Test (29/02/2024)', async ({ request }) => {
  const response = await request.get('http://localhost:8080/api/v1/check', {
    params: { day: '29', month: '2', year: '2024' }
  });
  expect(response.ok()).toBeTruthy();         // HTTP 200 OK
  const body = await response.json();
  expect(body.isValid).toBe(true);            // JSON: isValid = true
  expect(body.message).toBe('29/02/2024 is correct date time!');
});
```

> 🗣️ "API Test khác Unit Test ở chỗ: nó test qua HTTP thật, kiểm tra cả tầng Controller, Service, Validator cùng lúc. Đây là Integration Test."

---

### SLIDE 9 — JOB 4 (PHẦN 2): E2E TEST GIAO DIỆN

**Tiêu đề:** E2E Test — Giả lập người dùng thật

```typescript
// e2e-tests/tests/date-checker.spec.ts
test('should validate a correct leap year date', async ({ page }) => {
  await page.goto('/');                              // Mở trình duyệt ảo

  await page.locator('#dayInput').fill('29');        // Gõ 29 vào ô Ngày
  await page.locator('#monthInput').fill('2');       // Gõ 2 vào ô Tháng
  await page.locator('#yearInput').fill('2024');     // Gõ 2024 vào ô Năm

  await page.locator('#checkBtn').click();           // Click nút "Check"

  // Kiểm tra kết quả hiển thị đúng
  const result = page.locator('#resultMessage');
  await expect(result).toContainText('29/02/2024 is correct date time!');
  await expect(result).toHaveClass(/result--success/); // Màu xanh lá ✅
});
```

**3 test cases E2E:**
| Test | Input | Kết quả mong đợi |
|---|---|---|
| Năm nhuận hợp lệ | 29/02/2024 | ✅ "correct date time!" |
| Năm không nhuận | 29/02/2023 | ❌ "not a valid date!" |
| Input chữ | abc/12/2023 | ❌ "Please enter integers only" |

> 🗣️ "Playwright điều khiển trình duyệt Chromium thật — gõ phím, click chuột, đọc kết quả. Đây là test gần nhất với trải nghiệm người dùng thực tế."

---

### SLIDE 10 — JOB 4 (PHẦN 3): VISUAL REGRESSION TEST

**Tiêu đề:** Visual Regression — Phát hiện UI bị vỡ

```typescript
// e2e-tests/tests/visual-checker.spec.ts
test('Giao diện phải khớp với bản gốc', async ({ page }) => {
  await page.goto('http://localhost:8080');
  await page.waitForLoadState('networkidle');   // Đợi trang load hoàn toàn

  await expect(page).toHaveScreenshot('homepage-baseline.png', {
    fullPage: true,
    maxDiffPixels: 100,   // Cho phép sai ≤ 100 pixel (font rendering)
  });
});
```

**Cơ chế hoạt động:**

```
Lần 1:  Chụp ảnh → Lưu làm "baseline" ✅
Lần 2+: Chụp ảnh → So sánh với baseline
         Lệch ≤ 100px → PASS ✅
         Lệch > 100px → FAIL ❌ → Tự update baseline → Tự commit → Thông báo dev
```

**Cơ chế tự động thông minh:**
```yaml
- name: Update Snapshots (If Failed)
  if: steps.playwright.outcome == 'failure'
  run: npx playwright test --update-snapshots  # Chụp ảnh mới làm baseline

- name: Auto Commit Snapshots
  uses: stefanzweifel/git-auto-commit-action@v5  # Tự commit lên repo

- name: Fail Pipeline (thông báo dev)
  run: exit 1   # Pipeline fail → dev biết pull code mới về
```

> 🗣️ "Đây là tính năng nâng cao nhất. Nếu dev đổi màu nút, pipeline sẽ tự phát hiện, tự cập nhật ảnh chuẩn mới, tự commit, và nhắc dev pull code về."

---

### SLIDE 11 — JOB 4 (PHẦN 4): PERFORMANCE TEST

**Tiêu đề:** Performance Test — Chịu tải 50 người cùng lúc

```javascript
// performance_test.js
export const options = {
  stages: [
    { duration: '2s',  target: 50 },   // Tăng dần: 0 → 50 users
    { duration: '10s', target: 50 },   // Duy trì: 50 users liên tục bắn
    { duration: '2s',  target: 0 },    // Giảm dần: 50 → 0
  ],
  thresholds: {
    http_req_duration: ['p(99)<200'],   // 99% request phải < 200ms
    http_req_failed:   ['rate<0.01'],   // Tỉ lệ lỗi phải < 1%
  },
};
```

**Kịch bản test:**
- 🔴 Ramp up 2s: Đẩy dần lên 50 user ảo
- 🟡 Sustain 10s: Giữ 50 user bắn liên tục
- 🟢 Ramp down 2s: Giảm về 0

**Nếu server FAIL tiêu chí → Pipeline dừng → Không deploy!**

> 🗣️ "k6 là công cụ load testing mã nguồn mở của Grafana. Nó giả lập 50 người dùng đồng thời gửi request. Nếu server phản hồi > 200ms ở 99% request hoặc lỗi > 1%, pipeline fail, không deploy lên production."

---

### SLIDE 12 — TỔNG KẾT PHẦN CI

**Tiêu đề:** Tổng kết CI — Những gì tự động hóa được

| # | Giai đoạn | Tool | File |
|---|---|---|---|
| 1 | Source Control + Trigger | GitHub | `cicd.yml` on: push/PR |
| 2 | Build & Compile | Maven | `mvn clean test` |
| 3 | Unit Test (Web — 30 cases) | JUnit 5 | `DateValidatorTest.java` |
| 4 | Unit Test (Android) | Robolectric | `android-app/...` |
| 5 | Build APK | Gradle | `gradle assembleDebug` |
| 6 | API / Integration Test | Playwright | `api-checker.spec.ts` |
| 7 | E2E UI Test | Playwright | `date-checker.spec.ts` |
| 8 | Visual Regression | Playwright | `visual-checker.spec.ts` |
| 9 | Performance Test | k6 | `performance_test.js` |

**Kết quả: Code chỉ được phép đi tiếp nếu PASS toàn bộ 9 giai đoạn trên**

---

# ═══════════════════════════════════════
# PHẦN 2: CD — CONTINUOUS DEPLOYMENT
# ═══════════════════════════════════════

---

### SLIDE 13 — TRANG TIÊU ĐỀ CD

**Tiêu đề lớn:** Continuous Deployment (CD)

**Subtitle:** Tự động hóa triển khai lên server thật

**Luồng CD trong project:**
```
Code (đã pass CI) → Docker Image → Docker Hub → Render.com → 🌐 Live
```

> 🗣️ "Nếu CI là kiểm tra chất lượng, thì CD là dây chuyền đưa sản phẩm đến tay khách hàng. Sau khi CI pass, không cần ai làm gì thêm — app tự lên server."

---

### SLIDE 14 — CD LÀ GÌ?

**Tiêu đề:** Continuous Delivery vs Continuous Deployment

| | Continuous Delivery | Continuous Deployment |
|---|---|---|
| **Định nghĩa** | Luôn sẵn sàng deploy | Tự động deploy hoàn toàn |
| **Cần người** | Cần bấm nút cuối | Không cần ai |
| **Rủi ro** | Thấp hơn | Cần test rất tốt |
| **Project này** | ❌ | ✅ **Đang dùng** |

**Pipeline CD có 3 giai đoạn:**
1. 📦 **CONTAINERIZE** — Đóng gói app bằng Docker
2. 🚀 **PUBLISH** — Đẩy image lên Docker Hub
3. 🌐 **DEPLOY** — Triển khai tự động lên Render.com

---

### SLIDE 15 — GIAI ĐOẠN 1: CONTAINERIZE — DOCKERFILE

**Tiêu đề:** Containerize — Đóng gói app thành Docker Image

**File:** `Dockerfile`

```dockerfile
# ===== Stage 1: BUILD =====
FROM maven:3.9-eclipse-temurin-21 AS build   # Image có sẵn Maven + JDK 21
WORKDIR /app
COPY pom.xml .                                # Copy file cấu hình Maven
COPY src ./src                                # Copy toàn bộ source code
RUN mvn clean package -DskipTests            # Build ra file .war

# ===== Stage 2: RUN =====
FROM eclipse-temurin:21-jre                  # Image NHỎ HƠN — chỉ có JRE
WORKDIR /app
COPY --from=build /app/target/*.war app.war  # Chỉ lấy file .war từ Stage 1
EXPOSE 8080                                   # Khai báo port lắng nghe
ENTRYPOINT ["java", "-jar", "app.war"]        # Lệnh khởi động app
```

**Tại sao Multi-stage Build?**

| | Stage 1 (Build) | Stage 2 (Run) |
|---|---|---|
| **Image** | maven:3.9-temurin-21 | eclipse-temurin:21-jre |
| **Kích thước** | ~500 MB | ~100 MB |
| **Mục đích** | Compile code | Chạy app |
| **Có trong image cuối?** | ❌ Bỏ đi | ✅ Giữ lại |

**Kết quả: Image cuối nhỏ hơn 5 lần → Deploy nhanh hơn**

> 🗣️ "Multi-stage build là best practice của Docker. Chúng ta dùng máy to để xây nhà (Stage 1), nhưng chỉ cần giữ lại ngôi nhà (Stage 2), bỏ hết công cụ xây dựng đi."

---

### SLIDE 16 — TẠI SAO CẦN DOCKER?

**Tiêu đề:** Docker giải quyết vấn đề gì?

**Vấn đề kinh điển:**
```
Developer A:  Java 21, Ubuntu 22
Developer B:  Java 17, Windows 11
Server:       Java 11, CentOS 7
→ "Works on my machine!" 😤
```

**Giải pháp Docker:**
```
Docker Image = App + Java 21 + mọi dependencies
→ Chạy giống hệt nhau ở BẤT KỲ đâu có Docker
→ Không còn "works on my machine" nữa ✅
```

**Container vs máy ảo (VM):**
| | VM | Docker Container |
|---|---|---|
| Khởi động | ~1-2 phút | ~1-3 giây |
| Dung lượng | ~GBs | ~MBs-100MB |
| Cô lập | Hoàn toàn | Process-level |

---

### SLIDE 17 — GIAI ĐOẠN 2: PUBLISH LÊN DOCKER HUB

**Tiêu đề:** Job 2 — Build & Push Docker Image

```yaml
# cicd.yml — Dòng 25-46
docker-build-and-push:
  runs-on: ubuntu-latest
  needs: build-and-test    # CHỈ chạy nếu Job 1 (CI) PASS
  if: github.event_name == 'push' && github.ref == 'refs/heads/main'
  # → Chỉ build Docker khi push lên main (không phải Pull Request)

  steps:
  - name: Log in to Docker Hub
    uses: docker/login-action@v3
    with:
      username: johnpham666
      password: ${{ secrets.DOCKERHUB_TOKEN }}  # ← Mật khẩu bí mật

  - name: Build and Push Docker Image
    uses: docker/build-push-action@v6
    with:
      context: .       # Dùng Dockerfile ở thư mục hiện tại
      push: true       # Push lên Hub sau khi build
      tags: |
        johnpham666/datetimechecker:latest            # Bản mới nhất
        johnpham666/datetimechecker:${{ github.sha }} # VD: johnpham666/...:a3f2c1b
```

**Giải thích `${{ github.sha }}`:**
- Mỗi commit có 1 mã SHA duy nhất (VD: `a3f2c1b`)
- Nhờ tag SHA → có thể **rollback** về bất kỳ version cũ
- VD: `docker pull johnpham666/datetimechecker:a3f2c1b`

> 🗣️ "${{ secrets.DOCKERHUB_TOKEN }} là GitHub Secret — biến được mã hóa, không hiển thị trong log dù ai xem. Đây là cách đúng để lưu mật khẩu trong CI/CD."

---

### SLIDE 18 — GITHUB SECRETS — BẢO MẬT CREDENTIALS

**Tiêu đề:** GitHub Secrets — Không bao giờ hardcode mật khẩu

**Cú pháp:**
```yaml
password: ${{ secrets.TÊN_SECRET }}
```

**Các secrets trong project này:**

| Secret Name | Dùng ở đâu | Chứa gì |
|---|---|---|
| `DOCKERHUB_TOKEN` | Job 2 | Token đăng nhập Docker Hub |
| `RENDER_DEPLOY_HOOK_URL` | Job 5 | URL bí mật để trigger deploy |

**Cách thêm Secret vào GitHub:**
```
GitHub Repo → Settings → Secrets and variables → Actions
→ New repository secret → Điền Name + Value → Add secret
```

**Tại sao KHÔNG được làm thế này?**
```yaml
password: "my_actual_password_123"  # ❌ NGUY HIỂM — lộ lên GitHub công khai
password: ${{ secrets.DOCKERHUB_TOKEN }}  # ✅ AN TOÀN — mã hóa, ẩn trong log
```

---

### SLIDE 19 — GIAI ĐOẠN 3: DEPLOY LÊN RENDER.COM

**Tiêu đề:** Job 5 — Deploy tự động lên Render.com

```yaml
# cicd.yml — Dòng 144-154
deploy-to-render:
  runs-on: ubuntu-latest
  needs: docker-build-and-push  # Chỉ deploy sau khi image đã push lên Hub ✅
  if: github.event_name == 'push' && github.ref == 'refs/heads/main'

  steps:
  - name: Trigger Render Deploy Hook
    run: |
      echo "🚀 Triggering deploy on Render.com..."
      curl -f -X POST "${{ secrets.RENDER_DEPLOY_HOOK_URL }}"
      # curl -f : nếu HTTP error thì fail ngay (không im lặng)
      # -X POST : gửi HTTP POST request đến Deploy Hook URL
      echo "✅ Deploy triggered successfully!"
```

**Điều gì xảy ra khi Render nhận được request?**
```
GitHub Actions gọi curl POST → Render nhận
    → 1. Kéo image mới từ Docker Hub
    → 2. Dừng container cũ
    → 3. Khởi động container mới với image mới
    → 4. App live tại: your-app.onrender.com
```

> 🗣️ "Chỉ một lệnh curl duy nhất — đây là sức mạnh của Deploy Hook. Render làm tất cả phần còn lại: kéo image, swap container, không downtime."

---

### SLIDE 20 — LUỒNG CD HOÀN CHỈNH

**Tiêu đề:** Toàn bộ vòng đời CD — Từ code đến production

```
Developer: git push origin main
                  │
                  ▼
        [CI Pass ✅ — Job 1]
        Build + 30 Unit Tests
                  │
                  ▼
        [Job 2: Containerize + Publish]
        Docker build (multi-stage)
        Tag: latest + SHA commit
        Push → docker.io/johnpham666/datetimechecker
                  │
                  ▼
        [Job 5: Deploy]
        curl POST → Render Deploy Hook
                  │
                  ▼
        Render.com:
        ① Pull image mới từ Docker Hub
        ② Stop container cũ
        ③ Start container mới
                  │
                  ▼
        🌐 APP LIVE — your-app.onrender.com
        
Tổng thời gian: ~3-5 phút sau git push
```

---

### SLIDE 21 — TỔNG KẾT PHẦN CD

**Tiêu đề:** Tổng kết CD — 3 giai đoạn, hoàn toàn tự động

| # | Giai đoạn | Tool | Kết quả |
|---|---|---|---|
| 1 | **Containerize** | Docker (multi-stage) | Image nhỏ gọn, portable |
| 2 | **Publish** | Docker Hub | Image sẵn sàng pull từ mọi nơi |
| 3 | **Deploy** | Render.com Deploy Hook | App live trên internet |

**Bảo mật:**
| Secret | Mục đích |
|---|---|
| `DOCKERHUB_TOKEN` | Đăng nhập Docker Hub |
| `RENDER_DEPLOY_HOOK_URL` | Trigger deploy Render |

---

### SLIDE 22 — TỔNG KẾT TOÀN BỘ CI/CD

**Tiêu đề:** Toàn bộ Pipeline — 13 giai đoạn tự động hoàn toàn

| # | Giai đoạn | Phần | Tool |
|---|---|---|---|
| 1 | Source Control + Trigger | CI | GitHub |
| 2 | Build & Compile | CI | Maven |
| 3 | Unit Test Web (30 cases) | CI | JUnit 5 |
| 4 | Unit Test Android | CI | Robolectric |
| 5 | Build APK Android | CI | Gradle |
| 6 | Publish APK Artifact | CI | GitHub Artifacts |
| 7 | API / Integration Test | CI | Playwright |
| 8 | E2E UI Test | CI | Playwright |
| 9 | Visual Regression Test | CI | Playwright |
| 10 | Performance Test | CI | k6 |
| 11 | Containerize | **CD** | Docker |
| 12 | Publish Docker Image | **CD** | Docker Hub |
| 13 | Deploy to Server | **CD** | Render.com |

**Kết quả:** Từ lúc `git push` → App live: **~5 phút, 0 can thiệp thủ công**

---

### SLIDE 23 — SLIDE KẾT THÚC

**Tiêu đề:** CI/CD — Nền tảng của DevOps hiện đại

**3 điều mang về:**
1. 🔄 **CI** = Kiểm tra tự động mỗi khi push code → Phát hiện bug sớm
2. 🚀 **CD** = Triển khai tự động sau khi kiểm tra → Nhanh, nhất quán, không lỗi người
3. 🐳 **Docker** = "Build once, run anywhere" → Cô lập môi trường

**Demo project:** [github.com/JohnPham666/DateTimeChecker](https://github.com/JohnPham666/DateTimeChecker)

**Q&A**
