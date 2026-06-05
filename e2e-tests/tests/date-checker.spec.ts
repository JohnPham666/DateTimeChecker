import { test, expect } from '@playwright/test';

test.describe('DateTime Checker E2E Tests', () => {

  test('should validate a correct leap year date', async ({ page }) => {
    // 1. Mở trang web (chạy ở localhost:8080)
    await page.goto('/');

    // 2. Nhập dữ liệu ngày 29/02/2024 (Năm nhuận hợp lệ)
    await page.locator('#dayInput').fill('29');
    await page.locator('#monthInput').fill('2');
    await page.locator('#yearInput').fill('2024');

    // 3. Bấm nút Check
    await page.locator('#checkBtn').click();

    // 4. Kiểm tra kết quả trả về
    const resultMessage = page.locator('#resultMessage');
    await expect(resultMessage).toBeVisible();
    await expect(resultMessage).toContainText('29/02/2024 is correct date time!');
    
    // Đảm bảo chữ hiển thị màu xanh (success)
    await expect(resultMessage).toHaveClass(/result--success/);
  });

  test('should show error for invalid leap year date', async ({ page }) => {
    // 1. Mở trang web
    await page.goto('/');

    // 2. Nhập dữ liệu ngày 29/02/2023 (Năm KHÔNG nhuận - Không hợp lệ)
    await page.locator('#dayInput').fill('29');
    await page.locator('#monthInput').fill('2');
    await page.locator('#yearInput').fill('2023');

    // 3. Bấm nút Check
    await page.locator('#checkBtn').click();

    // 4. Kiểm tra kết quả trả về
    const resultMessage = page.locator('#resultMessage');
    await expect(resultMessage).toBeVisible();
    await expect(resultMessage).toContainText('29/02/2023 is not a valid date!');
    
    // Đảm bảo chữ hiển thị màu đỏ (error)
    await expect(resultMessage).toHaveClass(/result--error/);
  });

  test('should show error for alphabet characters', async ({ page }) => {
    await page.goto('/');

    await page.locator('#dayInput').fill('abc');
    await page.locator('#monthInput').fill('12');
    await page.locator('#yearInput').fill('2023');

    await page.locator('#checkBtn').click();

    const resultMessage = page.locator('#resultMessage');
    await expect(resultMessage).toBeVisible();
    await expect(resultMessage).toContainText('Invalid input! Please enter integers only.');
    await expect(resultMessage).toHaveClass(/result--error/);
  });

});
