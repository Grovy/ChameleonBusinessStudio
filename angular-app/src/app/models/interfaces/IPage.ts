import { IPageSort } from "./IPageSort";


export interface IPage { 
    content: any[],
    pageable: string,
    last: boolean,
    totalPages: number,
    totalElements: number,
    size: number,
    number: number,
    sort: IPageSort,
    first: boolean,
    numberOfElements: number,
    empty: boolean
}