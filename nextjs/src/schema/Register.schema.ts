import { object, string, ref, boolean } from "yup";

export default object({
  email: string().email("Email không hợp lệ").required("Email là bắt buộc"),
  password: string()
    .required("Mật khẩu là bắt buộc")
    .min(8, "Mật khẩu phải có ít nhất 8 ký tự")
    .max(32, "Mật khẩu không được quá 32 ký tự")
    .matches(
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^a-zA-Z0-9]).{8,32}$/,
      "Mật khẩu phải bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt"
    ),

  confirmPassword: string()
    .oneOf([ref("password")], "Mật khẩu không khớp")
    .required("Xác nhận mật khẩu là bắt buộc"),
  firstName: string().required("Tên là bắt buộc"),
  lastName: string().required("Họ là bắt buộc"),
  confirmedTerms: boolean().oneOf(
    [true],
    "Bạn phải đồng ý với các điều khoản và điều kiện"
  ),
}).required("Thông tin đăng ký là bắt buộc");
