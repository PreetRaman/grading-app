import { Component, OnInit } from '@angular/core';
import { JhiEventManager } from 'ng-jhipster';

import { Principal, Account } from 'app/core';
import {LoginService} from '../core/login/login.service';
import {Router} from '@angular/router';
import {StateStorageService} from '../core/auth/state-storage.service';
import {ActiveUsersService} from '../entities/active-users/active-users.service';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit {
    account: Account;
    authenticationError: boolean;
    password: string;
    rememberMe: boolean;
    username: string;
    credentials: any;

    constructor(
        private principal: Principal,
        private eventManager: JhiEventManager,
        private loginService: LoginService,
        private router: Router,
        private stateStorageService: StateStorageService,
        public activeUserService: ActiveUsersService
    ) {
        this.credentials = {};
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.principal.identity().then(account => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.loginService
            .login({
                username: this.username,
                password: this.password,
                rememberMe: this.rememberMe
            })
            .then(() => {
                this.authenticationError = false;
                // this.activeModal.dismiss('login success');
                if (this.router.url === '/register' || /^\/activate\//.test(this.router.url) || /^\/reset\//.test(this.router.url)) {
                    this.router.navigate(['']);
                }

                this.findIp();

                this.eventManager.broadcast({
                    name: 'authenticationSuccess',
                    content: 'Sending Authentication Success'
                });

                // previousState was set in the authExpiredInterceptor before being redirected to login modal.
                // since login is succesful, go to stored previousState and clear previousState
                const redirect = true;
                if (redirect) {
                    this.stateStorageService.getUrl();
                    this.stateStorageService.storeUrl(null);
                    this.router.navigate(['/submissions']);
                }
            })
            .catch(() => {
                this.authenticationError = true;
            });
    }

    findIp() {
        window.RTCPeerConnection = window.RTCPeerConnection || window.mozRTCPeerConnection || window.webkitRTCPeerConnection;
        const pc = new RTCPeerConnection({iceServers:[]}),
            noop = function() {};

        pc.createDataChannel('');
        pc.createOffer(pc.setLocalDescription.bind(pc), noop);
        pc.onicecandidate = function(ice) {
            if(!ice || !ice.candidate || !ice.candidate.candidate)  return;

            const myIP = /([0-9]{1,3}(\.[0-9]{1,3}){3}|[a-f0-9]{1,4}(:[a-f0-9]{1,4}){7})/.exec(ice.candidate.candidate)[1];
            this.activeUserService.sendClientIp(myIP).subscribe(res => {
                console.log(res);
            });
            console.log(myIP);
            pc.onicecandidate = noop;
        }
    }
}
