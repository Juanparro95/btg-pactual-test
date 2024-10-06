export interface Fund {
    id: number;
    name: string;
    minimumAmount: number;
    category: string;
    isSubscribed?: boolean; // Añadimos 'isSubscribed'
  }
  