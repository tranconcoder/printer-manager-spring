import React from "react";

export interface DropdownListProps
  extends React.HTMLAttributes<HTMLUListElement> {
  items: Array<{ label: string; value: string }>;
}

export default function DropdownList({ items, ...props }: DropdownListProps) {
  return (
    <div className="w-full h-12 rounded-lg ring-2 ring-offset-2 ring-gray-300"></div>
  );
}
