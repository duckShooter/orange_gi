export class Api {
    static readonly base: string = "http://localhost:8080"; 
    static readonly login: string = `${Api.base}/login`;
    static readonly products: string = `${Api.base}/api/products`;
    static readonly categories: string = `${Api.base}/api/categories`
}