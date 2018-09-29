export interface IContinente {
    id?: number;
    nome?: string;
}

export class Continente implements IContinente {
    constructor(public id?: number, public nome?: string) {}
}
