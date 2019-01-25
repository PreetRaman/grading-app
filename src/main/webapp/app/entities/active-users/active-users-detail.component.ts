import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IActiveUsers } from 'app/shared/model/active-users.model';

@Component({
    selector: 'jhi-active-users-detail',
    templateUrl: './active-users-detail.component.html'
})
export class ActiveUsersDetailComponent implements OnInit {
    activeUsers: IActiveUsers;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ activeUsers }) => {
            this.activeUsers = activeUsers;
        });
    }

    previousState() {
        window.history.back();
    }
}
