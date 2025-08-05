import Link from "next/link";
import React from "react";

export interface ButtonSecondaryProps extends React.HTMLAttributes<Element> {
  children: React.ReactNode;
  href?: string;
}

export default function ButtonSecondary({
  children,
  href,
  ...props
}: ButtonSecondaryProps) {
  const Button = href ? Link : "button";

  return (
    <Button
      {...props}
      href={href || ""}
      className={
        "rounded-full block px-5 py-3 text-base text-gray-700 bg-white font-medium hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-gray-300 focus:ring-offset-2 transition-colors duration-100" +
        " " +
        (props.className || "")
      }
    >
      <div className="-mt-px">{children}</div>
    </Button>
  );
}
