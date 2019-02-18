import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class HomeService {
    public resourceUrl = SERVER_API_URL + 'api/active-users';

    constructor(private http: HttpClient) {}

    sendClientIp(ip: string) {
        return this.http.get(`${this.resourceUrl}/ip/${ip}`);
    }
}
