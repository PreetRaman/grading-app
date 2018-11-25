import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ISubmissions } from 'app/shared/model/submissions.model';
import { SubmissionsService } from './submissions.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-submissions-update',
    templateUrl: './submissions-update.component.html'
})
export class SubmissionsUpdateComponent implements OnInit {
    submissions: ISubmissions;
    isSaving: boolean;

    users: IUser[];

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private submissionsService: SubmissionsService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ submissions }) => {
            this.submissions = submissions;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.submissions.id !== undefined) {
            this.subscribeToSaveResponse(this.submissionsService.update(this.submissions));
        } else {
            this.subscribeToSaveResponse(this.submissionsService.create(this.submissions));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISubmissions>>) {
        result.subscribe((res: HttpResponse<ISubmissions>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
