export interface IEstado {
    id?: number;
    sigla?: string;
    nomeEstado?: string;
}

export class Estado implements IEstado {
    constructor(public id?: number, public sigla?: string, public nomeEstado?: string) {}
}
