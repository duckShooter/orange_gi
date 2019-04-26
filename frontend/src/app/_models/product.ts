import { Category } from "./category";

export class Product {
    id: number;
    name: string;
    description: string;
    vendor: string;
    category: Category;
    price: number;
}