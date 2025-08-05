"use client";

import type { IconType } from "react-icons";

import React from "react";
import styles from "./styles.module.css";
import classNames from "classnames/bind";
import { FaRegEye, FaRegEyeSlash } from "react-icons/fa6";

const cx = classNames.bind(styles);

export interface InputProps
  extends React.InputHTMLAttributes<HTMLInputElement> {
  children: string;
  togglePassword?: boolean;
  icon?: IconType;
  errorMessage?: string;
}

export default function Input({
  children,
  togglePassword,
  icon: Icon,
  ...props
}: InputProps) {
  const [inputType, setInputType] = React.useState(props.type || "text");

  //
  // Toggle password visibility
  //
  const isPasswordInput = props.type === "password";
  const [visiblePassword, setVisiblePassword] = React.useState(false);
  const EyeIcon = visiblePassword ? FaRegEye : FaRegEyeSlash;

  const handleTogglePassword = () => {
    setVisiblePassword(!visiblePassword);
    setInputType(visiblePassword ? "password" : "text");
  };

  return (
    <div className={"relative mt-10 mb-4" + " " + (props.className || "")}>
      {/* Toggle password icon */}
      {togglePassword && isPasswordInput && (
        <EyeIcon
          className="absolute right-3 top-1/2 transform -translate-y-1/2 text-lg"
          onClick={handleTogglePassword}
        />
      )}

      <input
        {...props}
        placeholder=" "
        className={
          cx("input-elm") +
          " w-full p-2 rounded-md outline-none ring-2 ring-gray-300 ring-offset-0 transition-all duration-200 ease-in-out focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
        }
        type={inputType}
      />

      <p
        className={
          cx("placeholder-elm") +
          " absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500 transition-all duration-200 ease-in-out bg-white px-2 pointer-events-none select-none"
        }
      >
        {/* Icon if provided */}
        {Icon && <Icon className="inline-block mr-2 -mt-1" />}

        {children}
      </p>

      {/* Error message */}
      {props.errorMessage && (
        <p className="absolute top-full text-red-500 text-sm mt-1">{props.errorMessage}</p>
      )}
    </div>
  );
}
