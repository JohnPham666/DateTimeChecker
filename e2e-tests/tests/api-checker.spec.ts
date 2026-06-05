import { test, expect } from '@playwright/test';

test.describe('API Testing with Playwright', () => {

  const baseURL = 'http://localhost:8080/api/v1/check';

  test('Valid Date API Test (29/02/2024)', async ({ request }) => {
    const response = await request.get(baseURL, {
      params: { day: '29', month: '2', year: '2024' }
    });
    
    expect(response.ok()).toBeTruthy();
    
    const body = await response.json();
    expect(body.isValid).toBe(true);
    expect(body.message).toBe('29/02/2024 is correct date time!');
  });

  test('Invalid Date API Test (29/02/2023)', async ({ request }) => {
    const response = await request.get(baseURL, {
      params: { day: '29', month: '2', year: '2023' }
    });
    
    expect(response.ok()).toBeTruthy();
    
    const body = await response.json();
    expect(body.isValid).toBe(false);
    expect(body.message).toBe('29/02/2023 is not a valid date!');
  });

  test('Invalid Format API Test (abc instead of numbers)', async ({ request }) => {
    const response = await request.get(baseURL, {
      params: { day: 'abc', month: '2', year: '2023' }
    });
    
    expect(response.status()).toBe(400);
    
    const body = await response.json();
    expect(body.isValid).toBe(false);
    expect(body.message).toBe('Invalid input! Please enter integers only.');
  });

});
