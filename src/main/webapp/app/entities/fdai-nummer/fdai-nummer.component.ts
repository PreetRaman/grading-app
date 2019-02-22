import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IFdaiNummer } from 'app/shared/model/fdai-nummer.model';
import { Principal } from 'app/core';
import { FdaiNummerService } from './fdai-nummer.service';
import {ActiveUsersService} from '../active-users/active-users.service';

@Component({
    selector: 'jhi-fdai-nummer',
    templateUrl: './fdai-nummer.component.html'
})
export class FdaiNummerComponent implements OnInit, OnDestroy {
    fdaiNummers: IFdaiNummer[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private fdaiNummerService: FdaiNummerService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal,
        private activeUsersService: ActiveUsersService
    ) {}

    loadAll() {
        this.fdaiNummerService.query().subscribe(
            (res: HttpResponse<IFdaiNummer[]>) => {
                this.fdaiNummers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInFdaiNummers();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IFdaiNummer) {
        return item.id;
    }

    registerChangeInFdaiNummers() {
        this.eventSubscriber = this.eventManager.subscribe('fdaiNummerListModification', response => this.loadAll());
    }

    clearData() {
        this.fdaiNummerService.deleteAll().subscribe(response => {
            this.eventManager.broadcast({
                name: 'fdaiNummerListModification',
                content: 'Deleted fdaiNummers'
            });
        });
        this.activeUsersService.deleteAll().subscribe(res => {
            console.log(res);
        });
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
