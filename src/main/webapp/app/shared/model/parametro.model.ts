export interface IParametro {
    id?: number;
    nome?: string;
    conteudo?: string;
}

export class Parametro implements IParametro {
    constructor(public id?: number, public nome?: string, public conteudo?: string) {}
}
