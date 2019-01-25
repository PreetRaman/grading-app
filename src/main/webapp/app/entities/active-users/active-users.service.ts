import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IActiveUsers } from 'app/shared/model/active-users.model';

type EntityResponseType = HttpResponse<IActiveUsers>;
type EntityArrayResponseType = HttpResponse<IActiveUsers[]>;

@Injectable({ providedIn: 'root' })
export class ActiveUsersService {
    public resourceUrl = SERVER_API_URL + 'api/active-users';

    constructor(private http: HttpClient) {}

    create(activeUsers: IActiveUsers): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(activeUsers);
        return this.http
            .post<IActiveUsers>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(activeUsers: IActiveUsers): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(activeUsers);
        return this.http
            .put<IActiveUsers>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IActiveUsers>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IActiveUsers[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getUser(): Observable<any> {
        return this.http.get(`${this.resourceUrl}/ladmin`);
    }

    protected convertDateFromClient(activeUsers: IActiveUsers): IActiveUsers {
        const copy: IActiveUsers = Object.assign({}, activeUsers, {
            login_time: activeUsers.login_time != null && activeUsers.login_time.isValid() ? activeUsers.login_time.toJSON() : null,
            logout_time: activeUsers.logout_time != null && activeUsers.logout_time.isValid() ? activeUsers.logout_time.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.login_time = res.body.login_time != null ? moment(res.body.login_time) : null;
            res.body.logout_time = res.body.logout_time != null ? moment(res.body.logout_time) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((activeUsers: IActiveUsers) => {
                activeUsers.login_time = activeUsers.login_time != null ? moment(activeUsers.login_time) : null;
                activeUsers.logout_time = activeUsers.logout_time != null ? moment(activeUsers.logout_time) : null;
            });
        }
        return res;
    }
}
