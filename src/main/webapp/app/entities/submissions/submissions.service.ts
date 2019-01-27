import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
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
    public downloadUrl = SERVER_API_URL + 'api/submissions/download';

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

    exportCSV() {
        const headers =  new HttpHeaders({
            'Content-type': 'text/csv'
        });
        return this.http.get(this.downloadUrl, { responseType:'text', headers: headers })
            .toPromise()
            .then(res => {
                if(res) {
                    this.downloadFile(res);
                }
            })
            .catch(err => {
                this.downloadFile(err.error.text);
            });
    }

    downloadFile(data) {
        let blob = new Blob([data], { type: 'text/csv;charset=utf-8;' });
        let dwldLink = document.createElement("a");
        let url = URL.createObjectURL(blob);
        let isSafariBrowser = navigator.userAgent.indexOf('Safari') != -1 && navigator.userAgent.indexOf('Chrome') == -1;
        if (isSafariBrowser) {  //if Safari open in new window to save file with random filename.
            dwldLink.setAttribute("target", "_blank");
        }
        dwldLink.setAttribute("href", url);
        dwldLink.setAttribute("download", "Enterprise.csv");
        dwldLink.style.visibility = "hidden";
        document.body.appendChild(dwldLink);
        dwldLink.click();
        document.body.removeChild(dwldLink);
    }

    queryByLadmin(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get<ISubmissions[]>(this.ladminUrl, { params: options, observe: 'response' });
    }
}
