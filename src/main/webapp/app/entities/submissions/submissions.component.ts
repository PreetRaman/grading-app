import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ISubmissions } from 'app/shared/model/submissions.model';
import { Principal } from 'app/core';
import { SubmissionsService } from './submissions.service';

@Component({
    selector: 'jhi-submissions',
    templateUrl: './submissions.component.html'
})
export class SubmissionsComponent implements OnInit, OnDestroy {
    submissions: ISubmissions[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private submissionsService: SubmissionsService,
        private jhiAlertService: JhiAlertService,
        private dataUtils: JhiDataUtils,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.submissionsService.queryByLadmin().subscribe(
            (res: HttpResponse<ISubmissions[]>) => {
                this.submissions = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSubmissions();
    }

    isLadmin(): boolean {
        return this.principal.userIdentity.authorities.indexOf('ROLE_LADMIN') > -1;
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISubmissions) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    printPDF() {
        console.log('Hello PDF');
    }

    clearData() {
        this.submissionsService.deleteAll().subscribe(response => {
            this.eventManager.broadcast({
                name: 'submissionsListModification',
                content: 'Deleted an submissions'
            });
        });
    }

    exportData() {
        this.submissionsService.exportCSV().subscribe(data => {
            let parsedResponse = data.text();
            console.log(parsedResponse);
            this.downloadFile(parsedResponse);
        },
        error => {
            console.log(error.text);
        });
    }

    downloadFile(data: any){
        var blob = new Blob([data], { type: 'text/csv' });
        var url= window.URL.createObjectURL(blob);
        window.open(url);
    }

    registerChangeInSubmissions() {
        this.eventSubscriber = this.eventManager.subscribe('submissionsListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
