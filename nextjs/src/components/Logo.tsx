import React from "react";
import Image from "next/image";
import Link from "next/link";

export type LogoProps = React.HTMLAttributes<Element>;

export default function Logo(props: LogoProps) {
  let className = "h-12 relative w-24 block";

  if (props.className) className += " " + props.className;

  return (
    <Link {...props} className={className} href="/">
      <Image
        src="/logo.png"
        alt="Logo"
        layout="fill"
        className="object-contain"
      />
    </Link>
  );
}
