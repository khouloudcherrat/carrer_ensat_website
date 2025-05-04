export interface SignUpRequest {
    id?: string;
    firstName: string;
    lastName: string;
    email: string;
    cinCard: string;
    branch: string;
    role: string;
    formDetails: { [key: string]: any };
    createdAt?: string;
  }