export default interface User {
    id: string;
    name: string;
    email: string;
    enabled: boolean;
    image:string;
    updatedAt: String;
    createdAt: String;  
    provider: String;
  }