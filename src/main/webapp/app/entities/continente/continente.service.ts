import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IContinente } from 'app/shared/model/continente.model';

type EntityResponseType = HttpResponse<IContinente>;
type EntityArrayResponseType = HttpResponse<IContinente[]>;

@Injectable({ providedIn: 'root' })
export class ContinenteService {
    private resourceUrl = SERVER_API_URL + 'api/continentes';

    constructor(private http: HttpClient) {}

    create(continente: IContinente): Observable<EntityResponseType> {
        return this.http.post<IContinente>(this.resourceUrl, continente, { observe: 'response' });
    }

    update(continente: IContinente): Observable<EntityResponseType> {
        return this.http.put<IContinente>(this.resourceUrl, continente, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IContinente>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IContinente[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
