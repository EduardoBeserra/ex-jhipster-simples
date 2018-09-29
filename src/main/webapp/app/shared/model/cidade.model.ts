import { IEstado } from 'app/shared/model//estado.model';

export interface ICidade {
    id?: number;
    nome?: string;
    codigoIbge?: string;
    estado?: IEstado;
}

export class Cidade implements ICidade {
    constructor(public id?: number, public nome?: string, public codigoIbge?: string, public estado?: IEstado) {}
}
