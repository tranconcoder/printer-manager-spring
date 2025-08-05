import React from "react";
import Link from "next/link";

export interface ButtonPrimaryProps
  extends React.HTMLAttributes<HTMLAnchorElement & HTMLButtonElement> {
  children: React.ReactNode;
  href?: string;
  type?: "button" | "submit" | "reset";
}

export default function ButtonPrimary({
  children,
  href,
  ...props
}: ButtonPrimaryProps) {
  const Button = href ? Link : "button";

  return (
    <Button
      {...props}
      href={href || ""}
      className={
        "rounded-full block px-5 py-3 text-base text-white bg-gray-700 font-medium hover:bg-gray-500 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2 transition-colors duration-100" +
        " " +
        (props.className || "")
      }
    >
      <div className="-mt-px">{children}</div>
    </Button>
  );
}
