import { object, string } from "yup";

export default object({
  email: string().email("Email không hợp lệ").required("Email là bắt buộc"),
  password: string().required("Mật khẩu là bắt buộc"),
}).required("Thông tin đăng nhập là bắt buộc");
