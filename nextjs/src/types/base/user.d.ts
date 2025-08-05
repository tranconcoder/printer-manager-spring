import Gender from "@/enums/gender.enum";

export default interface User {
  userId: number;
  email: string;
  firstName: string;
  lastName: string;
  gender: Gender;
}
