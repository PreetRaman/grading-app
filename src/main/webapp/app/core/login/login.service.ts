import { Injectable } from '@angular/core';

import { Principal } from '../auth/principal.service';
import { AuthServerProvider } from '../auth/auth-jwt.service';
import {Observable} from 'rxjs/index';
import {SERVER_API_URL} from '../../app.constants';
import {HttpClient} from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class LoginService {
    public resourceUrl = SERVER_API_URL + 'api/active-users/logout';

    constructor(
        private principal: Principal,
        private authServerProvider: AuthServerProvider,
        private http: HttpClient
    ) {}

    login(credentials, callback?) {
        const cb = callback || function() {};

        return new Promise((resolve, reject) => {
            this.authServerProvider.login(credentials).subscribe(
                data => {
                    this.principal.identity(true).then(account => {
                        resolve(data);
                    });
                    return cb();
                },
                err => {
                    this.logout();
                    reject(err);
                    return cb(err);
                }
            );
        });
    }

    loginWithToken(jwt, rememberMe) {
        return this.authServerProvider.loginWithToken(jwt, rememberMe);
    }

    logout() {
        this.authServerProvider.logout().subscribe();
        this.principal.authenticate(null);
    }

    backendLogout(): Observable<any> {
        return this.http.get(this.resourceUrl);

    }
}
