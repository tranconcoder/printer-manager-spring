import { Metadata } from "next";
import React from "react";
import RegisterForm from "./RegisterForm";

export const metadata: Metadata = {
  title: "Đăng ký - Welcome to tvconss!",
};

export interface RegisterPageProps {
  children: React.ReactNode;
}

export default function RegisterPage() {
  return <RegisterForm />;
}
