import http from 'k6/http';
import { check, sleep } from 'k6';

// Cấu hình kịch bản bắn phá (Load Test)
export const options = {
  // Giai đoạn 1: Đẩy dần lên 50 người dùng trong 2 giây
  // Giai đoạn 2: Giữ vững mức 50 người dùng liên tục bắn trong 10 giây
  // Giai đoạn 3: Rút quân về 0 trong 2 giây
  stages: [
    { duration: '2s', target: 50 },
    { duration: '10s', target: 50 },
    { duration: '2s', target: 0 },
  ],
  // Tiêu chuẩn vượt qua bài test:
  thresholds: {
    // 99% request phải trả về dưới 200ms
    http_req_duration: ['p(99)<200'], 
    // Tỉ lệ lỗi phải dưới 1%
    http_req_failed: ['rate<0.01'], 
  },
};

export default function () {
  // Gửi request lên Server Spring Boot
  const res = http.get('http://localhost:8080/api/v1/check?day=29&month=2&year=2024');
  
  // Kiểm tra nhanh kết quả trả về
  check(res, {
    'Trạng thái phải là 200': (r) => r.status === 200,
    'Kết quả báo isValid là true': (r) => String(r.body).includes('"isValid":true'),
  });

  // Nghỉ 0.1 giây trước khi người dùng ảo này bắn tiếp request thứ 2
  sleep(0.1);
}
