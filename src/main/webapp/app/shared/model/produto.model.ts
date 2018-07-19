import { Moment } from 'moment';

export interface IProduto {
    id?: number;
    nome?: string;
    descricao?: string;
    dataVencimento?: Moment;
    preco?: number;
}

export class Produto implements IProduto {
    constructor(
        public id?: number,
        public nome?: string,
        public descricao?: string,
        public dataVencimento?: Moment,
        public preco?: number
    ) {}
}
