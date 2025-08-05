"use client";

import React from "react";
import ButtonPrimary from "@/components/common/ButtonPrimary";
import Input from "@/components/common/Input/Input";
import { FaUserAlt } from "react-icons/fa";
import { FaLock } from "react-icons/fa6";
import Link from "next/link";
import AuthWithGoogleButton from "../AuthWithGoogleButton";
import { useFormik } from "formik";
import LoginSchema from "@/schema/Login.schema";

export default function LoginForm() {
  const formik = useFormik({
    initialValues: {
      email: "",
      password: "",
    },
    onSubmit: (values) => {
      // Handle form submission logic here
      console.log("Form submitted with values:", values);
    },
    validationSchema: LoginSchema,
  });

  return (
    <form onSubmit={formik.handleSubmit}>
      {/* Username */}
      <Input
        name="email"
        onChange={formik.handleChange}
        onBlur={formik.handleBlur}
        value={formik.values.email}
        errorMessage={formik.touched.email ? formik.errors.email : ""}
        icon={FaUserAlt}
      >
        Email đăng nhập
      </Input>

      {/* Password */}
      <Input
        name="password"
        icon={FaLock}
        togglePassword
        onChange={formik.handleChange}
        onBlur={formik.handleBlur}
        value={formik.values.password}
        type="password"
        errorMessage={formik.touched.password ? formik.errors.password : ""}
      >
        Mật khẩu
      </Input>

      {/* Forgot password link */}
      <Link
        className="block mt-4 text-sm text-gray-600 hover:underline text-right"
        href="/auth/forgot-password"
      >
        Quên mật khẩu?
      </Link>

      {/* Login button */}
      <ButtonPrimary className="mt-6 w-full" type="submit">
        Đăng nhập
      </ButtonPrimary>

      {/* Or with Google */}
      <span className="text-sm italic block text-center my-4">Hoặc</span>

      {/* Google authentication button */}
      <AuthWithGoogleButton>
        Đăng nhập bằng tài khoản Google
      </AuthWithGoogleButton>
    </form>
  );
}
