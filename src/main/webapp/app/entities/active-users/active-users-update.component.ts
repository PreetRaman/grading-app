import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IActiveUsers } from 'app/shared/model/active-users.model';
import { ActiveUsersService } from './active-users.service';

@Component({
    selector: 'jhi-active-users-update',
    templateUrl: './active-users-update.component.html'
})
export class ActiveUsersUpdateComponent implements OnInit {
    activeUsers: IActiveUsers;
    isSaving: boolean;
    login_time: string;
    logout_time: string;

    constructor(private activeUsersService: ActiveUsersService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ activeUsers }) => {
            this.activeUsers = activeUsers;
            this.login_time = this.activeUsers.login_time != null ? this.activeUsers.login_time.format(DATE_TIME_FORMAT) : null;
            this.logout_time = this.activeUsers.logout_time != null ? this.activeUsers.logout_time.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.activeUsers.login_time = this.login_time != null ? moment(this.login_time, DATE_TIME_FORMAT) : null;
        this.activeUsers.logout_time = this.logout_time != null ? moment(this.logout_time, DATE_TIME_FORMAT) : null;
        if (this.activeUsers.id !== undefined) {
            this.subscribeToSaveResponse(this.activeUsersService.update(this.activeUsers));
        } else {
            this.subscribeToSaveResponse(this.activeUsersService.create(this.activeUsers));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IActiveUsers>>) {
        result.subscribe((res: HttpResponse<IActiveUsers>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
