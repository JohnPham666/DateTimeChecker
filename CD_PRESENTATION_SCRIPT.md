# 🎤 SCRIPT CD HOÀN CHỈNH — 2 PHÚT
# (Sau khi CI đã được trình bày xong)

---

## CHUẨN BỊ TRƯỚC KHI LÊN (mở sẵn 4 tab)

| Tab | URL | Mục đích |
|---|---|---|
| Tab 1 | `github.com/JohnPham666/DateTimeChecker/actions` | GitHub Actions pipeline |
| Tab 2 | `hub.docker.com/r/johnpham666/datetimechecker/tags` | Docker Hub image |
| Tab 3 | `dashboard.render.com` → service datetimechecker | Render dashboard |
| Tab 4 | URL app live `xxx.onrender.com` | App đang chạy thật |

> ⚠️ Mở Tab 4 trước 1-2 phút để Render "thức dậy" (free tier ngủ sau 15 phút idle)

---

## SCRIPT NÓI + THAO TÁC

---

### [0:00 — 0:15] Chuyển tiếp

**Nói:**
> "CI xong rồi — code đã pass test.
> Giờ là CD: tự động đóng gói và đưa lên server thật.
> Gồm 3 bước: Docker đóng gói → Docker Hub lưu trữ → Render deploy."

---

### [0:15 — 0:35] Chỉ vào cicd.yml — Job 2 và Job 5

**Thao tác:** Mở file `cicd.yml`, chỉ vào 4 dòng quan trọng

```yaml
needs: build-and-test          # ← Chỉ chạy nếu CI pass
password: ${{ secrets.DOCKERHUB_TOKEN }}  # ← Mật khẩu bí mật, không hardcode
tags: datetimechecker:latest   # ← Đóng gói thành Docker image, push lên Hub
curl -X POST RENDER_DEPLOY_HOOK_URL  # ← 1 lệnh này là Render tự deploy
```

**Nói:**
> "Job 2 đọc Dockerfile, build image, đẩy lên Docker Hub.
> Job 5 chỉ làm 1 việc: gọi curl — gửi tín hiệu cho Render biết có image mới.
> Và `needs` đảm bảo: Job 5 không bao giờ chạy nếu Job 2 chưa xong."

---

### [0:35 — 0:55] Demo Docker Hub

**Thao tác:** Chuyển sang Tab 2 — `hub.docker.com/r/johnpham666/datetimechecker/tags`

**Nói:**
> "Đây là Docker Hub — kho lưu image của mình.
> Thấy 2 tag:
> - `latest` — bản mới nhất, server luôn kéo cái này
> - Tag theo mã commit — ví dụ `4c580a9` — dùng để rollback nếu bản mới bị lỗi
>
> Cả 2 tag này được Job 2 tự động tạo và push — không cần ai làm thủ công."

---

### [0:55 — 1:15] Demo GitHub Actions

**Thao tác:** Chuyển sang Tab 1 → click vào pipeline run mới nhất

**Nói:**
> "Đây là pipeline vừa chạy.
> Thấy Job 2 và Job 5 đều xanh —
> nghĩa là image đã lên Hub, và Render đã nhận lệnh deploy."

**Thao tác:** Click vào Job 5 → show log

**Nói:**
> "Log của Job 5: dòng này — '✅ Deploy triggered' —
> tức là curl đã gọi thành công, Render đang kéo image về."

---

### [1:15 — 1:45] Demo Render Dashboard

**Thao tác:** Chuyển sang Tab 3 — Render dashboard

**Chỉ vào status "Live" màu xanh:**
> "Status Live — container đang chạy."

**Click tab "Events" hoặc "Deploys":**
> "Đây là lịch sử deploy tự động.
> Mỗi dòng là một lần mình push code —
> Render tự nhận lệnh từ Job 5 và deploy lại."

**Click tab "Logs":**
> "Log thật của container — thấy dòng 'Started Application' —
> Spring Boot đã khởi động xong bên trong Docker."

---

### [1:45 — 1:55] Show app live

**Thao tác:** Click vào URL trên đầu Render → mở Tab 4

**Nói:**
> "Và đây là kết quả — app đang chạy thật trên internet.
> Từ git push đến đây: 5 phút, không ai làm gì thêm."

---

### [1:55 — 2:00] Kết

**Nói:**
> "Đó là CD. Kết hợp với CI của bạn mình —
> đây là pipeline CI/CD hoàn chỉnh: code → kiểm tra → đóng gói → deploy → live."

---

## TÓM TẮT THAO TÁC (cheat sheet)

```
0:15 → cicd.yml     "needs / secrets / tags / curl"
0:35 → Docker Hub   "2 tag: latest + SHA → rollback"
0:55 → GH Actions   "Job 2 + Job 5 xanh, xem log Job 5"
1:15 → Render       "Live → Events → Logs"
1:45 → App live     "Kết quả cuối cùng"
```

---

---

# 🔌 NÊN TẮT HAY ĐỂ ON RENDER?

## Câu trả lời: ĐỂ ON — không cần tắt

**Lý do:**

| | Giải thích |
|---|---|
| **Miễn phí hoàn toàn** | Free tier không tốn tiền dù để on 24/7 |
| **Tự ngủ** | Render tự động ngủ sau 15 phút không có request — không tốn tài nguyên |
| **Tự thức** | Khi có người vào URL, Render tự thức dậy sau ~30 giây |
| **Cần để demo** | Nếu tắt trước khi thuyết trình → không demo được live |

## Nếu muốn tắt thật sự (sau khi thi xong)

```
Render Dashboard → Service → Settings
→ Kéo xuống cuối → "Suspend Service" (tạm dừng)
hoặc
→ "Delete Service" (xóa hẳn)
```

> ✅ **Kết luận: Cứ để ON. Render free tier tự quản lý, không tốn gì cả.
> Chỉ cần nhớ mở URL trước 1-2 phút trước khi demo để nó kịp thức dậy.**
