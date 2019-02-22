import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IActiveUsers } from 'app/shared/model/active-users.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { ActiveUsersService } from './active-users.service';

@Component({
    selector: 'jhi-active-users',
    templateUrl: './active-users.component.html'
})
export class ActiveUsersComponent implements OnInit, OnDestroy {
    currentAccount: any;
    activeUsers: IActiveUsers[];
    nonAssignedActiveUser: IActiveUsers[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    constructor(
        private activeUsersService: ActiveUsersService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll() {
        this.activeUsersService.getUser().subscribe(
            res => {
                this.activeUsers = res;
                console.log(this.activeUsers);
            }
        );
    }

    getNonAssignedUser() {
        this.activeUsersService.getUnassignedUser().subscribe(
            res => {
                this.nonAssignedActiveUser = res;
                console.log(this.nonAssignedActiveUser);
            }
        );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/active-users'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
        this.getNonAssignedUser();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/active-users',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
        this.getNonAssignedUser();
    }

    ngOnInit() {
        this.loadAll();
        this.getNonAssignedUser();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInActiveUsers();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IActiveUsers) {
        return item.id;
    }

    registerChangeInActiveUsers() {
        this.eventSubscriber = this.eventManager.subscribe('activeUsersListModification',
            response => {
                this.loadAll();
                this.getNonAssignedUser();
            }
        );
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateActiveUsers(data: IActiveUsers[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.activeUsers = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
