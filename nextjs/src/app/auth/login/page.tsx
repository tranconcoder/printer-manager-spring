import type { Metadata } from "next";

import React, { Fragment } from "react";
import LoginForm from "./LoginForm";

export const metadata: Metadata = {
  title: "Đăng nhập - Welcome to tvconss!",
  description: "Logo component for the application",
};

export default function LoginPage() {
  return (
    <Fragment>
      <LoginForm />
    </Fragment>
  );
}
