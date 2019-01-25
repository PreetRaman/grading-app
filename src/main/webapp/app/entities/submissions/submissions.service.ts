import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISubmissions } from 'app/shared/model/submissions.model';

type EntityResponseType = HttpResponse<ISubmissions>;
type EntityArrayResponseType = HttpResponse<ISubmissions[]>;

@Injectable({ providedIn: 'root' })
export class SubmissionsService {
    public resourceUrl = SERVER_API_URL + 'api/submissions';
    public ladminUrl = SERVER_API_URL + 'api/submissions/ladmin';

    constructor(private http: HttpClient) {}

    create(submissions: ISubmissions): Observable<EntityResponseType> {
        return this.http.post<ISubmissions>(this.resourceUrl, submissions, { observe: 'response' });
    }

    update(submissions: ISubmissions): Observable<EntityResponseType> {
        return this.http.put<ISubmissions>(this.resourceUrl, submissions, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISubmissions>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISubmissions[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    deleteAll(): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}`, { observe: 'response' });
    }

    exportCSV(): Observable<any> {
        return this.http.get<any>(`${this.resourceUrl}/download`);
    }

    queryByLadmin(): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get<ISubmissions[]>(this.ladminUrl, { params: options, observe: 'response' });
    }
}
