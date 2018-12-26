import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFdaiNummer } from 'app/shared/model/fdai-nummer.model';

type EntityResponseType = HttpResponse<IFdaiNummer>;
type EntityArrayResponseType = HttpResponse<IFdaiNummer[]>;

@Injectable({ providedIn: 'root' })
export class FdaiNummerService {
    public resourceUrl = SERVER_API_URL + 'api/fdai-nummers';

    constructor(private http: HttpClient) {}

    create(fdaiNummer: IFdaiNummer): Observable<EntityResponseType> {
        return this.http.post<IFdaiNummer>(this.resourceUrl, fdaiNummer, { observe: 'response' });
    }

    update(fdaiNummer: IFdaiNummer): Observable<EntityResponseType> {
        return this.http.put<IFdaiNummer>(this.resourceUrl, fdaiNummer, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IFdaiNummer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IFdaiNummer[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
