import { test, expect } from '@playwright/test';

test.describe('Visual Regression Testing', () => {

  test('Giao diện trang chủ phải khớp chính xác với bản gốc (Baseline)', async ({ page }) => {
    // 1. Mở trang chủ
    await page.goto('http://localhost:8080');

    // 2. Chờ cho trang load xong và render đầy đủ
    await page.waitForLoadState('networkidle');

    // 3. Chụp ảnh màn hình toàn bộ trang và so sánh với Baseline
    // Lần chạy đầu tiên sẽ lỗi và tự sinh ra ảnh chuẩn.
    // Các lần sau nếu lệnh này chạy, nó sẽ báo FAILED nếu chênh lệch dù chỉ 1 pixel.
    await expect(page).toHaveScreenshot('homepage-baseline.png', {
      fullPage: true,
      maxDiffPixels: 100, // Cho phép lệch tối đa 100 pixel (do anti-aliasing)
    });
  });

});
